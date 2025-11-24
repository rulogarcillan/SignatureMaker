package com.signaturemaker.app.ui.designsystem.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import com.signaturemaker.app.ui.designsystem.SMTheme

/**
 * A circular color selector component.
 */
@Composable
fun SMColorSelector(
    color: Color,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: (Color) -> Unit = {}
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(color, CircleShape)
            .clickable(onClick = { onClick(color) }),
            // .background(SMTheme.material.colorScheme.primary, CircleShape)
            // .then(if (selected) Modifier.padding(SMTheme.spacing.spacing100) else Modifier)

        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        if (selected) {
            SMIcon(imageVector = Icons.Default.Check, tint = SMTheme.color.selector, contentDescription = null)
        }
    }
}

/**
 * A circular image selector component.
 */
@Composable
fun SMImageSelector(
    image: Painter,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: (Painter) -> Unit = {}
) {
    Box(modifier = modifier, contentAlignment = androidx.compose.ui.Alignment.BottomEnd) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .clickable(onClick = { onClick(image) }),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            Image(
                painter = image,
                contentDescription = null,
                Modifier
                    .padding(SMTheme.spacing.spacing100)
                    .clip(CircleShape)
                    .fillMaxSize()
            )
        }
        if (selected) {
            Box(
                modifier = Modifier
                    .size(SMTheme.size.size200)
                    .background(SMTheme.color.pen4, CircleShape)
            )
        }
    }
}
