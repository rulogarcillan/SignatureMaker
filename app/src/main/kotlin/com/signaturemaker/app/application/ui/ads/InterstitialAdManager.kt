package com.signaturemaker.app.application.ui.ads

import android.app.Activity
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

/**
 * Gestor de Anuncios Intersticiales (Full Screen)
 *
 * Características:
 * - Carga previa del anuncio
 * - Respeta consentimiento GDPR
 * - Precarga el siguiente anuncio
 */
class InterstitialAdManager(private val context: Context) {

    private var interstitialAd: InterstitialAd? = null
    private var isLoading = false
    private var onAdDismissed: (() -> Unit)? = null
    private var currentAdUnitId: String? = null

    /**
     * Cargar el intersticial de forma anticipada
     */
    fun loadAd(adUnitId: String) {
        if (isLoading || interstitialAd != null) {
            android.util.Log.d("InterstitialAd", "Already loading or loaded")
            return
        }

        // Guardar el adUnitId para poder recargar después
        currentAdUnitId = adUnitId
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

                            // Precargar el siguiente anuncio usando el adUnitId guardado
                            currentAdUnitId?.let { savedAdUnitId ->
                                android.util.Log.d("InterstitialAd", "Preloading next ad")
                                loadAd(savedAdUnitId)
                            }
                        }

                        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                            android.util.Log.e("InterstitialAd", "Ad failed to show: ${adError.message}")
                            interstitialAd = null

                            // Intentar recargar si falló al mostrar
                            currentAdUnitId?.let { savedAdUnitId ->
                                android.util.Log.d("InterstitialAd", "Reloading after show failure")
                                loadAd(savedAdUnitId)
                            }
                        }

                        override fun onAdShowedFullScreenContent() {
                            android.util.Log.d("InterstitialAd", "Ad showed full screen")
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
     * Mostrar el intersticial si está cargado, o intentar cargarlo si no lo está
     *
     * @return true si se mostró, false si no estaba cargado
     */
    fun showIfReady(activity: Activity, onDismissed: () -> Unit = {}): Boolean {
        // Si el anuncio está cargado, mostrarlo inmediatamente
        if (interstitialAd != null) {
            onAdDismissed = onDismissed
            interstitialAd?.show(activity)
            return true
        }

        // Si no está cargado, intentar cargarlo ahora (para el siguiente intento)
        android.util.Log.d("InterstitialAd", "Ad not loaded yet, attempting to load for next time")
        currentAdUnitId?.let { adUnitId ->
            loadAd(adUnitId)
        }

        onDismissed()
        return false
    }

    /**
     * Verificar si el anuncio está cargado
     */
    fun isAdLoaded(): Boolean = interstitialAd != null
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
