package com.signaturemaker.app.di.presentation

import com.signaturemaker.app.features.sign.SignBoardViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val presentationModule = module {
    // Sign Feature ViewModels
    viewModelOf(::SignBoardViewModel)
}
