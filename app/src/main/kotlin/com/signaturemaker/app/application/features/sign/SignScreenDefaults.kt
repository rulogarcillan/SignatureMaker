package com.signaturemaker.app.application.features.sign

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.signaturemaker.app.application.core.extensions.Utils
import com.signaturemaker.app.application.ui.designsystem.SMTheme

object SignScreenDefaults {

    /**
     * Get default pen color from Utils (respects user preferences)
     * Falls back to theme color if no preference set
     */
    val defaultPenColor: Color
        @Composable
        get() = Color(Utils.penColor)

    /**
     * Get default background image from Utils (respects user preferences)
     * Falls back to theme color if no preference set
     */
    val defaultBackgroundImage: Int
        get() = Utils.wallpaper

    /**
     * Get default minimum stroke width from Utils (respects user preferences)
     */
    val defaultMinStrokeWidth: Float
        get() = Utils.minStroke

    /**
     * Get default maximum stroke width from Utils (respects user preferences)
     */
    val defaultMaxStrokeWidth: Float
        get() = Utils.maxStroke

    @Composable
    fun availablePenColors() = listOf(
        SMTheme.color.pen1,
        SMTheme.color.pen2,
        SMTheme.color.pen3,
        SMTheme.color.pen4
    )
}
