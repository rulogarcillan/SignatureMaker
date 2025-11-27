package com.signaturemaker.app.application.core.di.app

import org.koin.core.KoinApplication
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration

/**
 * SignatureMaker Dependency Injection interface
 */
interface SignatureMakerDi {
    fun start(appDeclaration: KoinAppDeclaration): KoinApplication
    fun getModules() = listOf<Module>()
}
