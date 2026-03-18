package com.signaturemaker.app.application.ui.ads

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
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
            .build()

        val consentInformation = UserMessagingPlatform.getConsentInformation(activity)

        // Solicitar actualización de información de consentimiento
        consentInformation.requestConsentInfoUpdate(
            activity,
            params,
            {
                // Success
                "GDPR: Consent information updated successfully".logd()
                "GDPR: Consent status: ${consentInformation.consentStatus}".logd()
                "GDPR: Is form available: ${consentInformation.isConsentFormAvailable}".logd()

                // Verificar si podemos mostrar anuncios
                if (consentInformation.canRequestAds()) {
                    canShowAds = true
                    consentStatus = ConsentStatus.OBTAINED
                    "GDPR: Can request ads = true".logd()
                }

                // Cargar y mostrar formulario si es necesario
                if (consentInformation.isConsentFormAvailable) {
                    UserMessagingPlatform.loadAndShowConsentFormIfRequired(activity) { formError ->
                        if (formError != null) {
                            "GDPR: Error showing consent form: ${formError.message}".loge()
                            consentStatus = ConsentStatus.ERROR
                        } else {
                            "GDPR: Consent form shown successfully".logd()
                            // Verificar nuevamente después de mostrar el formulario
                            if (consentInformation.canRequestAds()) {
                                canShowAds = true
                                consentStatus = ConsentStatus.OBTAINED
                                "GDPR: User gave consent, can show ads".logd()
                            } else {
                                canShowAds = false
                                consentStatus = ConsentStatus.REQUIRED
                                "GDPR: User did not give consent".logd()
                            }
                        }
                    }
                } else {
                    "GDPR: Consent form not available, consent not required".logd()
                    consentStatus = ConsentStatus.NOT_REQUIRED
                }
            },
            { error ->
                // Error
                "GDPR: Error requesting consent information: ${error.message}".loge()
                "GDPR: Error code: ${error.errorCode}".loge()
                consentStatus = ConsentStatus.ERROR

                // En caso de error, permitir anuncios si ya tenemos consentimiento previo
                if (consentInformation.canRequestAds()) {
                    canShowAds = true
                    "GDPR: Error but can request ads from previous consent".logd()
                }
            }
        )

        // Verificar estado inicial
        if (consentInformation.canRequestAds()) {
            canShowAds = true
            consentStatus = ConsentStatus.OBTAINED
            "GDPR: Initial check - can request ads".logd()
        }
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
