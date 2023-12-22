package com.signaturemaker.app.features.sign

import com.signaturemaker.app.navigation.SignatureMakerDestination


data object SignDestination : SignatureMakerDestination {
    override val route: String = "signscreen"
}
