package com.signaturemaker.app.application.core.di.domain

import com.signaturemaker.app.domain.usecase.GetAllFiles
import com.signaturemaker.app.domain.usecase.MoveAllFiles
import com.signaturemaker.app.domain.usecase.RemoveAllFiles
import com.signaturemaker.app.domain.usecase.RemoveFile
import com.signaturemaker.app.domain.usecase.SaveBitmap
import com.signaturemaker.app.domain.usecase.SendSuggest
import org.koin.dsl.module

val domainModule = module {

    // Use Cases
    factory {
        SaveBitmap(get())
    }

    factory {
        SendSuggest(get())
    }

    factory {
        GetAllFiles(get())
    }

    factory {
        RemoveFile(get())
    }

    factory {
        RemoveAllFiles(get(), get())
    }

    factory {
        MoveAllFiles(get())
    }
}

