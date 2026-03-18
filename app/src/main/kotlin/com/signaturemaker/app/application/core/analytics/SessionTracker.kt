package com.signaturemaker.app.application.core.analytics

import android.util.Log

/**
 * Session tracker - mantiene el estado de la sesión del usuario
 * Persiste durante toda la vida de la app
 */
object SessionTracker {

    private const val TAG = "SessionTracker"

    // Signature tracking
    private var signaturesCount = 0
    private var lastSaveTimestamp = 0L

    // Screen tracking
    private var currentScreenStartTime = 0L
    private var currentScreenName = ""

    /**
     * Reset all session data (llamar cuando la app se cierra o manualmente)
     */
    fun reset() {
        signaturesCount = 0
        lastSaveTimestamp = 0L
        currentScreenStartTime = 0L
        currentScreenName = ""
        Log.i(TAG, "Session reset")
    }

    // ============================================
    // SIGNATURE TRACKING
    // ============================================

    /**
     * Incrementa el contador de firmas guardadas
     */
    fun incrementSignatureCount() {
        signaturesCount++
        Log.i(TAG, "Signatures count: $signaturesCount")
    }

    /**
     * Obtiene el número total de firmas guardadas en esta sesión
     */
    fun getSignaturesCount(): Int = signaturesCount

    /**
     * Actualiza el timestamp del último guardado
     */
    fun updateLastSaveTime() {
        lastSaveTimestamp = System.currentTimeMillis()
    }

    /**
     * Calcula los segundos desde el último guardado
     * @return -1 si es la primera firma, o los segundos transcurridos
     */
    fun getSecondsSinceLastSave(): Long {
        return if (lastSaveTimestamp == 0L) {
            -1L // Primera firma
        } else {
            (System.currentTimeMillis() - lastSaveTimestamp) / 1000
        }
    }

    // ============================================
    // SCREEN TRACKING
    // ============================================

    /**
     * Marca el inicio de una pantalla
     */
    fun startScreen(screenName: String) {
        currentScreenName = screenName
        currentScreenStartTime = System.currentTimeMillis()
        Log.i(TAG, "Screen started: $screenName")
    }

    /**
     * Calcula la duración en segundos de la pantalla actual
     */
    fun getCurrentScreenDuration(): Long {
        return if (currentScreenStartTime == 0L) {
            0L
        } else {
            (System.currentTimeMillis() - currentScreenStartTime) / 1000
        }
    }

    /**
     * Obtiene el nombre de la pantalla actual
     */
    fun getCurrentScreenName(): String = currentScreenName

    // ============================================
    // UTILITY METHODS
    // ============================================

    /**
     * Obtiene un resumen del estado actual de la sesión
     */
    fun getSessionSummary(): String {
        return """
            Session Summary:
            - Signatures: $signaturesCount
            - Last save: ${if (lastSaveTimestamp == 0L) "never" else "${getSecondsSinceLastSave()}s ago"}
            - Current screen: $currentScreenName
            - Screen duration: ${getCurrentScreenDuration()}s
        """.trimIndent()
    }

    /**
     * Imprime el resumen de la sesión en el log
     */
    fun logSessionSummary() {
        Log.i(TAG, getSessionSummary())
    }
}

