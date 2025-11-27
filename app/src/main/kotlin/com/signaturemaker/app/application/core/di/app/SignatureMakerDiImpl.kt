package com.signaturemaker.app.application.core.di.app

import com.signaturemaker.app.application.core.di.data.dataModule
import com.signaturemaker.app.application.core.di.domain.domainModule
import com.signaturemaker.app.application.core.di.presentation.presentationModule
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

    override fun getModules() = listOf(
        dataModule,
        domainModule,
        presentationModule
    )
}
