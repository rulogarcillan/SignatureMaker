package com.signaturemaker.app.application.features.gallery

import com.signaturemaker.app.application.ui.navigation.SignatureMakerDestination

/*
 * Destination for the Gallery Screen
 */
data object GalleryDestination : SignatureMakerDestination {
    override val route: String = "galleryscreen"
}
