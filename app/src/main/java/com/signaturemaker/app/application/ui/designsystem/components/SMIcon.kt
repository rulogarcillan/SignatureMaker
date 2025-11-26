package com.signaturemaker.app.application.ui.designsystem.components

import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource

/**
 * SMIcon displays a simple icon passed by parameter.
 *
 * @param imageVector Resource to display as the icon.
 * @param contentDescription Content description of the [SMIcon].
 * @param modifier modifier Modifier applied to [SMIcon].
 * @param tint Tint of the [imageVector].
 *
 **/
@Composable
fun SMIcon(
    imageVector: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current
) {
    Icon(
        imageVector = imageVector,
        contentDescription = contentDescription,
        modifier = modifier,
        tint = tint
    )
}

/**
 * SMIcon displays a simple icon passed by parameter.
 *
 * @param painter Resource to display as the icon.
 * @param contentDescription Content description of the [SMIcon].
 * @param modifier modifier Modifier applied to [SMIcon].
 * @param tint Tint of the [Painter].
 *
 **/
@Composable
fun SMIcon(
    painter: Painter,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current
) {
    Icon(
        painter = painter,
        contentDescription = contentDescription,
        modifier = modifier,
        tint = tint
    )
}
