package com.signaturemaker.app.application.ui.ads

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.signaturemaker.app.R


/**
 * Banner de Publicidad de Google AdMob para Compose
 *
 * Características:
 * - Se integra nativamente en Compose
 * - Maneja el lifecycle automáticamente
 * - Carga el banner de forma asíncrona
 * - Limpia recursos automáticamente
 * - Siempre activo (no se puede desactivar)
 *
 * @param modifier Modificador para personalizar el diseño
 * @param adUnitId ID del banner ad (por defecto usa el de strings.xml)
 * @param onAdLoaded Callback cuando el anuncio se carga exitosamente
 * @param onAdFailedToLoad Callback cuando falla la carga
 */
@Composable
fun AdBanner(
    modifier: Modifier = Modifier,
    adUnitId: String? = null,
    onAdLoaded: () -> Unit = {},
    onAdFailedToLoad: (LoadAdError) -> Unit = {}
) {
    val context = LocalContext.current


    // Obtener el AdUnitId desde strings.xml si no se proporciona
    val finalAdUnitId = adUnitId ?: context.getString(R.string.banner_ad_unit_id)

    // Estado para controlar la visibilidad del banner
    var isAdLoaded by remember { mutableStateOf(false) }

    // Crear y recordar el AdView
    val adView = remember {
        AdView(context).apply {
            setAdSize(AdSize.BANNER)
            this.adUnitId = finalAdUnitId

            // Configurar listener
            adListener = object : AdListener() {
                override fun onAdLoaded() {
                    isAdLoaded = true
                    onAdLoaded()
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    isAdLoaded = false
                    onAdFailedToLoad(error)
                }

                override fun onAdOpened() {
                    // Anuncio abierto en pantalla completa
                }

                override fun onAdClosed() {
                    // Usuario cerró el anuncio
                }
            }
        }
    }

    // Cargar el anuncio cuando se monta el composable
    LaunchedEffect(finalAdUnitId) {
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }

    // Limpiar recursos cuando el composable se destruya
    DisposableEffect(adView) {
        onDispose {
            adView.destroy()
        }
    }

    // Solo mostrar el banner si el anuncio está cargado
    if (isAdLoaded) {
        AndroidView(
            factory = { adView },
            modifier = modifier.fillMaxWidth()
        )
    }
}

/**
 * Banner de Publicidad con gestión de lifecycle de Activity
 *
 * Esta versión maneja pause/resume automáticamente
 * Es la versión recomendada para usar en pantallas principales
 * Siempre activo (no se puede desactivar)
 *
 * @param adUnitId ID del banner ad - REQUERIDO para identificar qué banner estás usando
 * @param modifier Modificador para personalizar el diseño
 * @param onAdLoaded Callback cuando el anuncio se carga exitosamente
 * @param onAdFailedToLoad Callback cuando falla la carga
 */
@Composable
fun AdBannerWithLifecycle(
    adUnitId: String,
    modifier: Modifier = Modifier,
    onAdLoaded: () -> Unit = {},
    onAdFailedToLoad: (LoadAdError) -> Unit = {}
) {
    val context = LocalContext.current
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current

    val finalAdUnitId = adUnitId
    var isAdLoaded by remember { mutableStateOf(false) }

    val adView = remember {
        AdView(context).apply {
            setAdSize(AdSize.BANNER)
            this.adUnitId = finalAdUnitId

            adListener = object : AdListener() {
                override fun onAdLoaded() {
                    isAdLoaded = true
                    onAdLoaded()
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    isAdLoaded = false
                    onAdFailedToLoad(error)
                }
            }
        }
    }

    // Cargar el anuncio
    LaunchedEffect(finalAdUnitId) {
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }

    // Manejar lifecycle: pause/resume/destroy
    DisposableEffect(lifecycleOwner) {
        val lifecycleObserver = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> adView.pause()
                Lifecycle.Event.ON_RESUME -> adView.resume()
                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
            adView.destroy()
        }
    }

    if (isAdLoaded) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.navigationBars),
            contentAlignment = Alignment.CenterEnd
        ) {
            AndroidView(
                factory = { adView },
                modifier = Modifier.wrapContentWidth()
            )
        }
    }
}
