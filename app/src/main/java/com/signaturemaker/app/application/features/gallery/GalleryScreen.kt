package com.signaturemaker.app.application.features.gallery

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.signaturemaker.app.application.ui.designsystem.components.SMText

/**
 * Gallery screen.
 *
 * @param modifier Modifier to be applied to the root element
 */
@Composable
fun GalleryScreen(
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        SMText(text = "Gallery")
    }
}

@Preview
@Composable
private fun GalleryScreenPreview() {
    GalleryScreen()
}
