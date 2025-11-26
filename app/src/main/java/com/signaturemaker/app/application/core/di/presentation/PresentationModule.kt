package com.signaturemaker.app.application.core.di.presentation

import com.signaturemaker.app.application.features.sign.SignBoardViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val presentationModule = module {
    // Sign Feature ViewModels
    viewModelOf(::SignBoardViewModel)
}
