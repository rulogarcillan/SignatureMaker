package com.signaturemaker.app.application.features.main

import com.signaturemaker.app.application.ui.navigation.SignatureMakerDestination

/*
 * Destination for the Main Screen
 */
data object MainDestination : SignatureMakerDestination {
    override val route: String = "mainscreen"
}

