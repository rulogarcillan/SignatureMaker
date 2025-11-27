package com.signaturemaker.app.application.ui.ads

import android.app.Activity
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.tuppersoft.skizo.android.core.extension.loadSharedPreference
import com.tuppersoft.skizo.android.core.extension.saveSharedPreference

/**
 * Gestor de Anuncios Intersticiales (Full Screen)
 *
 * Características:
 * - Carga previa del anuncio
 * - Frecuencia controlada (evita spam)
 * - Respeta consentimiento GDPR
 * - No molesta al usuario
 *
 * Mejores prácticas implementadas:
 * - Máximo 1 intersticial por sesión
 * - Mínimo 5 minutos entre anuncios
 * - Solo después de acciones exitosas
 * - Precarga el siguiente anuncio
 */
class InterstitialAdManager(private val context: Context) {

    private var interstitialAd: InterstitialAd? = null
    private var isLoading = false
    private var onAdDismissed: (() -> Unit)? = null

    companion object {
        private const val PREF_LAST_INTERSTITIAL_TIME = "last_interstitial_time"
        private const val MIN_TIME_BETWEEN_ADS_MS = 5 * 60 * 1000L // 5 minutos
        private const val PREF_SESSION_INTERSTITIAL_SHOWN = "session_interstitial_shown"
    }

    /**
     * Cargar el intersticial de forma anticipada
     */
    fun loadAd(adUnitId: String) {
        if (isLoading || interstitialAd != null) {
            android.util.Log.d("InterstitialAd", "Already loading or loaded")
            return
        }

        isLoading = true
        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(
            context,
            adUnitId,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    android.util.Log.d("InterstitialAd", "Ad loaded successfully")
                    interstitialAd = ad
                    isLoading = false

                    // Configurar callbacks
                    ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {
                            android.util.Log.d("InterstitialAd", "Ad dismissed")
                            interstitialAd = null
                            onAdDismissed?.invoke()

                            // Precargar el siguiente anuncio
                            loadAd(adUnitId)
                        }

                        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                            android.util.Log.e("InterstitialAd", "Ad failed to show: ${adError.message}")
                            interstitialAd = null
                        }

                        override fun onAdShowedFullScreenContent() {
                            android.util.Log.d("InterstitialAd", "Ad showed full screen")
                            // Registrar que se mostró
                            context.saveSharedPreference(PREF_LAST_INTERSTITIAL_TIME, System.currentTimeMillis())
                            context.saveSharedPreference(PREF_SESSION_INTERSTITIAL_SHOWN, true)
                        }
                    }
                }

                override fun onAdFailedToLoad(loadError: LoadAdError) {
                    android.util.Log.e("InterstitialAd", "Failed to load: ${loadError.message}")
                    interstitialAd = null
                    isLoading = false
                }
            }
        )
    }

    /**
     * Mostrar el intersticial si cumple las condiciones
     *
     * @return true si se mostró, false si no cumplía condiciones
     */
    fun showIfReady(activity: Activity, onDismissed: () -> Unit = {}): Boolean {
        // Verificar condiciones
        if (!canShowAd()) {
            android.util.Log.d("InterstitialAd", "Cannot show ad - frequency limit")
            onDismissed()
            return false
        }

        // Verificar que el anuncio está cargado
        if (interstitialAd == null) {
            android.util.Log.d("InterstitialAd", "Ad not loaded yet")
            onDismissed()
            return false
        }

        // Mostrar anuncio
        onAdDismissed = onDismissed
        interstitialAd?.show(activity)
        return true
    }

    /**
     * Verificar si se puede mostrar el anuncio según frecuencia
     */
    private fun canShowAd(): Boolean {
        // Verificar si ya se mostró en esta sesión
        val shownInSession = context.loadSharedPreference(PREF_SESSION_INTERSTITIAL_SHOWN, false)
        if (shownInSession) {
            android.util.Log.d("InterstitialAd", "Already shown in this session")
            return false
        }

        // Verificar tiempo mínimo entre anuncios
        val lastTime = context.loadSharedPreference(PREF_LAST_INTERSTITIAL_TIME, 0L)
        val currentTime = System.currentTimeMillis()
        val timeSinceLastAd = currentTime - lastTime

        if (timeSinceLastAd < MIN_TIME_BETWEEN_ADS_MS) {
            val remainingMinutes = (MIN_TIME_BETWEEN_ADS_MS - timeSinceLastAd) / 60000
            android.util.Log.d("InterstitialAd", "Too soon, wait $remainingMinutes more minutes")
            return false
        }

        return true
    }

    /**
     * Verificar si el anuncio está cargado
     */
    fun isAdLoaded(): Boolean = interstitialAd != null

    /**
     * Resetear el flag de sesión (llamar en onCreate de Application)
     */
    fun resetSessionFlag() {
        context.saveSharedPreference(PREF_SESSION_INTERSTITIAL_SHOWN, false)
    }
}

/**
 * Hook de Compose para gestionar intersticiales
 *
 * Uso:
 * ```kotlin
 * val interstitialManager = rememberInterstitialAd(
 *     adUnitId = stringResource(R.string.interstitial_ad_unit_id)
 * )
 *
 * // Mostrar después de guardar firma
 * Button(onClick = {
 *     saveSignature()
 *     interstitialManager.showIfReady()
 * })
 * ```
 */
@Composable
fun rememberInterstitialAd(
    adUnitId: String,
    preload: Boolean = true
): InterstitialAdManager {
    val context = LocalContext.current
    val hasGDPRConsent = rememberGDPRConsent()

    val manager = remember(context) {
        InterstitialAdManager(context)
    }

    // Precargar el anuncio si hay consentimiento
    LaunchedEffect(adUnitId, hasGDPRConsent, preload) {
        if (hasGDPRConsent && preload) {
            manager.loadAd(adUnitId)
        }
    }

    return manager
}

/**
 * Extensión para mostrar intersticial de forma simple
 */
fun InterstitialAdManager.showIfReady(context: Context, onDismissed: () -> Unit = {}) {
    val activity = context as? Activity
    if (activity != null) {
        showIfReady(activity, onDismissed)
    } else {
        android.util.Log.e("InterstitialAd", "Context is not an Activity")
        onDismissed()
    }
}

