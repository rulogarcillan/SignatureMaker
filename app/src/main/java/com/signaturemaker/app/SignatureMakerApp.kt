package com.signaturemaker.app

import android.app.Application
import com.signaturemaker.app.di.app.SignatureMakerDiImpl
import org.koin.android.ext.koin.androidContext

class SignatureMakerApp : Application() {


    override fun onCreate() {
        super.onCreate()
        SignatureMakerDiImpl().start {
            androidContext(this@SignatureMakerApp)
        }
    }
}