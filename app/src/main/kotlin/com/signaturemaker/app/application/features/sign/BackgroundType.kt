package com.signaturemaker.app.application.features.sign

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.imageResource
import com.signaturemaker.app.R
import com.signaturemaker.app.application.ui.designsystem.SMTheme

// ============================================
// BACKGROUND TYPE SYSTEM
// ============================================

/**
 * Sealed class representing the background type for signature canvas
 */
sealed class BackgroundType {
    /**
     * Use theme color as background (adapts to light/dark theme)
     */
    data object ThemeColor : BackgroundType()

    /**
     * Use an image pattern as background
     */
    data class Image(@DrawableRes val resourceId: Int) : BackgroundType()

    companion object {
        // ID para identificar cada tipo
        const val THEME_COLOR_ID = 0
        const val MASK_1_ID = 1
        const val MASK_2_ID = 2

        // Conversión de ID a BackgroundType
        fun fromId(id: Int): BackgroundType = when (id) {
            THEME_COLOR_ID -> ThemeColor
            MASK_1_ID -> Image(R.drawable.mascara1)
            MASK_2_ID -> Image(R.drawable.mascara2)
            else -> ThemeColor
        }

        // Conversión de BackgroundType a ID
        fun toId(type: BackgroundType): Int = when (type) {
            is ThemeColor -> THEME_COLOR_ID
            is Image -> when (type.resourceId) {
                R.drawable.mascara1 -> MASK_1_ID
                R.drawable.mascara2 -> MASK_2_ID
                else -> THEME_COLOR_ID
            }
        }
    }
}

// ============================================
// BACKGROUND MANAGER
// ============================================

/**
 * Manager for handling background rendering
 */
object BackgroundManager {

    /**
     * Create a Brush based on the background type
     */
    @Composable
    fun createBrush(type: BackgroundType): Brush {
        return when (type) {
            is BackgroundType.ThemeColor -> {
                val color = SMTheme.color.backgroundSheet
                Brush.linearGradient(listOf(color, color))
            }

            is BackgroundType.Image -> {
                // Imagen tileada
                val imageBitmap = ImageBitmap.imageResource(LocalResources.current, type.resourceId)
                val shader = ImageShader(imageBitmap, TileMode.Repeated, TileMode.Repeated)
                ShaderBrush(shader)
            }
        }
    }

    /**
     * Get available background options as IDs
     */
    fun getAvailableOptions(): List<Int> = listOf(
        BackgroundType.THEME_COLOR_ID,
        BackgroundType.MASK_1_ID,
        BackgroundType.MASK_2_ID
    )
}
