package com.signaturemaker.app.application.ui.designsystem.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import com.signaturemaker.app.application.ui.designsystem.SMTheme

@Composable
fun SMColorSelector(
    color: Color,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: (Color) -> Unit = {}
) {
    // Animaciones (igual que SMImageSelector para consistencia)
    val scale by animateFloatAsState(
        targetValue = if (selected) 1.08f else 1f,
        animationSpec = spring(
            dampingRatio = 0.6f,
            stiffness = 300f
        ),
        label = "colorScale"
    )

    val elevation by animateDpAsState(
        targetValue = if (selected) SMTheme.size.size50 else SMTheme.size.size00,
        animationSpec = tween(durationMillis = 200),
        label = "colorElevation"
    )

    val borderWidth by animateDpAsState(
        targetValue = if (selected) SMTheme.size.size50 else SMTheme.size.size20,
        animationSpec = tween(durationMillis = 200),
        label = "colorBorderWidth"
    )

    val badgeScale by animateFloatAsState(
        targetValue = if (selected) 1f else 0f,
        animationSpec = spring(
            dampingRatio = 0.5f,
            stiffness = 500f
        ),
        label = "colorBadgeScale"
    )

    Box(
        modifier = modifier.scale(scale),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .shadow(
                    elevation = elevation,
                    shape = CircleShape
                )
                .clip(CircleShape)
                .border(
                    width = borderWidth,
                    color = if (selected)
                        SMTheme.material.colorScheme.primary
                    else
                        SMTheme.material.colorScheme.outline,
                    shape = CircleShape
                )
                .clickable(onClick = { onClick(color) }),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color, CircleShape)
            )
        }

        // Badge con check (igual que SMImageSelector)
        if (selected) {
            Box(
                modifier = Modifier
                    .size(SMTheme.size.size250)
                    .scale(badgeScale)
                    .align(androidx.compose.ui.Alignment.BottomEnd)
                    .shadow(SMTheme.size.size20, CircleShape)
                    .background(SMTheme.material.colorScheme.primary, CircleShape)
                    .border(
                        width = SMTheme.size.size20,
                        color = SMTheme.material.colorScheme.surface,
                        shape = CircleShape
                    ),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                SMIcon(
                    imageVector = Icons.Default.Check,
                    tint = SMTheme.material.colorScheme.onPrimary,
                    contentDescription = null,
                    modifier = Modifier.size(SMTheme.size.size150)
                )
            }
        }
    }
}

@Composable
fun SMImageSelector(
    image: Painter,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: (Painter) -> Unit = {}
) {
    // Animaciones
    val scale by animateFloatAsState(
        targetValue = if (selected) 1.08f else 1f,
        animationSpec = spring(
            dampingRatio = 0.6f,
            stiffness = 300f
        ),
        label = "imageScale"
    )

    val elevation by animateDpAsState(
        targetValue = if (selected) SMTheme.size.size50 else SMTheme.size.size00,
        animationSpec = tween(durationMillis = 200),
        label = "imageElevation"
    )

    val borderWidth by animateDpAsState(
        targetValue = if (selected) SMTheme.size.size50 else SMTheme.size.size20,
        animationSpec = tween(durationMillis = 200),
        label = "imageBorderWidth"
    )

    val badgeScale by animateFloatAsState(
        targetValue = if (selected) 1f else 0f,
        animationSpec = spring(
            dampingRatio = 0.5f,
            stiffness = 500f
        ),
        label = "badgeScale"
    )

    Box(
        modifier = modifier.scale(scale),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .shadow(
                    elevation = elevation,
                    shape = CircleShape
                )
                .clip(CircleShape)
                .border(
                    width = borderWidth,
                    color = if (selected)
                        SMTheme.material.colorScheme.primary
                    else
                        SMTheme.material.colorScheme.outline,
                    shape = CircleShape
                )
                .clickable(onClick = { onClick(image) }),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            Image(
                painter = image,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
            )
        }

        if (selected) {
            Box(
                modifier = Modifier
                    .size(SMTheme.size.size250)
                    .scale(badgeScale)
                    .align(androidx.compose.ui.Alignment.BottomEnd)
                    .shadow(SMTheme.size.size20, CircleShape)
                    .background(SMTheme.material.colorScheme.primary, CircleShape)
                    .border(
                        width = SMTheme.size.size20,
                        color = SMTheme.material.colorScheme.surface,
                        shape = CircleShape
                    ),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                SMIcon(
                    imageVector = Icons.Default.Check,
                    tint = SMTheme.material.colorScheme.onPrimary,
                    contentDescription = null,
                    modifier = Modifier.size(SMTheme.size.size150)
                )
            }
        }
    }
}
