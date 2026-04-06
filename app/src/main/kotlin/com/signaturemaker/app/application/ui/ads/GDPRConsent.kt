package com.signaturemaker.app.application.ui.ads

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.google.android.ump.ConsentDebugSettings
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import com.signaturemaker.app.BuildConfig
import com.tuppersoft.skizo.android.core.extension.logd
import com.tuppersoft.skizo.android.core.extension.loge

/**
 * Estado del consentimiento de GDPR
 */
enum class ConsentStatus {
    UNKNOWN,
    REQUIRED,
    NOT_REQUIRED,
    OBTAINED,
    ERROR
}

/**
 * Gestor de Consentimiento GDPR/UMP para Google Ads
 *
 * Este composable maneja automáticamente:
 * - Verificación de consentimiento requerido
 * - Mostrar formulario de consentimiento si es necesario
 * - Determinar si se pueden mostrar anuncios
 *
 * Uso:
 * ```kotlin
 * val canShowAds = rememberGDPRConsent()
 *
 * if (canShowAds) {
 *     AdBanner()
 * }
 * ```
 */
@Composable
fun rememberGDPRConsent(): Boolean {
    val context = LocalContext.current
    val activity = context as? Activity

    var canShowAds by remember { mutableStateOf(false) }
    var consentStatus by remember { mutableStateOf(ConsentStatus.UNKNOWN) }

    LaunchedEffect(Unit) {
        if (activity == null) {
            "GDPR: Activity is null, cannot request consent".loge()
            return@LaunchedEffect
        }

        val params = ConsentRequestParameters.Builder()
            .setTagForUnderAgeOfConsent(false)
            .apply {
                if (BuildConfig.DEBUG) {
                    val debugSettings = ConsentDebugSettings.Builder(activity)
                        .setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
                        .build()
                    setConsentDebugSettings(debugSettings)
                }
            }
            .build()

        val consentInformation = UserMessagingPlatform.getConsentInformation(activity)

        // En debug, resetear consent para forzar que el formulario aparezca cada vez
        if (BuildConfig.DEBUG) {
            consentInformation.reset()
            "GDPR: Debug mode - consent reset".logd()
        }

        // Solicitar actualización de información de consentimiento
        consentInformation.requestConsentInfoUpdate(
            activity,
            params,
            {
                // Success
                "GDPR: Consent information updated successfully".logd()
                "GDPR: Consent status: ${consentInformation.consentStatus}".logd()
                "GDPR: Is form available: ${consentInformation.isConsentFormAvailable}".logd()

                // Cargar y mostrar formulario si es necesario
                if (consentInformation.isConsentFormAvailable) {
                    UserMessagingPlatform.loadAndShowConsentFormIfRequired(activity) { formError ->
                        if (formError != null) {
                            "GDPR: Error showing consent form: ${formError.message}".loge()
                            consentStatus = ConsentStatus.ERROR
                        } else {
                            "GDPR: Consent form completed".logd()
                        }

                        // Siempre verificar después del intento de formulario (éxito o error)
                        if (consentInformation.canRequestAds()) {
                            canShowAds = true
                            consentStatus = ConsentStatus.OBTAINED
                            "GDPR: Can request ads after form attempt".logd()
                        }
                    }
                } else {
                    "GDPR: Consent form not available".logd()
                }

                // Verificar si podemos mostrar anuncios (fuera de EEA o consent ya obtenido)
                if (consentInformation.canRequestAds()) {
                    canShowAds = true
                    consentStatus = ConsentStatus.NOT_REQUIRED
                    "GDPR: Can request ads = true".logd()
                }
            },
            { error ->
                // Error de red o configuración - no bloquear ads innecesariamente
                "GDPR: Error requesting consent info: ${error.message} (code: ${error.errorCode})".loge()
                consentStatus = ConsentStatus.ERROR

                // Si ya tenemos consentimiento previo, seguir mostrando ads
                if (consentInformation.canRequestAds()) {
                    canShowAds = true
                    "GDPR: Error but can request ads from previous consent".logd()
                } else {
                    // Fallback: si no hay info de consent, permitir ads con personalización limitada
                    // Google Ads SDK maneja internamente el nivel de personalización
                    canShowAds = true
                    "GDPR: Error and no prior consent, allowing ads with limited personalization".logd()
                }
            }
        )
    }

    return canShowAds
}

/**
 * Versión más simple que solo verifica si puede mostrar anuncios
 * sin manejar el formulario de consentimiento
 */
@Composable
fun rememberCanShowAds(): Boolean {
    val context = LocalContext.current
    val activity = context as? Activity

    var canShowAds by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (activity == null) return@LaunchedEffect

        val consentInformation = UserMessagingPlatform.getConsentInformation(activity)
        canShowAds = consentInformation.canRequestAds()

        "Ads: Can show ads = $canShowAds".logd()
    }

    return canShowAds
}
