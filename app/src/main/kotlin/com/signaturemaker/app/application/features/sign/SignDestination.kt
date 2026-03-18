package com.signaturemaker.app.application.features.sign

import com.signaturemaker.app.application.ui.navigation.SignatureMakerDestination

/*
 * Destination for the Sign Screen
 */
data object SignDestination : SignatureMakerDestination {
    override val route: String = "signscreen"
}
