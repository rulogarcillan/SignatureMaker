package com.signaturemaker.app.features.sign

import com.signaturemaker.app.navigation.SignatureMakerDestination

/*
 * Destination for the Sign Screen
 */
data object SignDestination : SignatureMakerDestination {
    override val route: String = "signscreen"
}
