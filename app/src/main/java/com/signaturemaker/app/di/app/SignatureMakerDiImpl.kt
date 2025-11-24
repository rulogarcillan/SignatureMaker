package com.signaturemaker.app.di.app

import com.signaturemaker.app.di.presentation.presentationModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

/*
 * SignatureMaker Dependency Injection implementation
 */
open class SignatureMakerDiImpl : SignatureMakerDi {
    override fun start(appDeclaration: KoinAppDeclaration) = startKoin {
        appDeclaration()
        modules(getModules())
    }

    override fun getModules() = listOf(presentationModule)
}
