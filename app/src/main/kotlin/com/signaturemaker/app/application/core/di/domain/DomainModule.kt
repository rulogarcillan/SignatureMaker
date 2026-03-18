package com.signaturemaker.app.application.core.di.domain

import com.signaturemaker.app.domain.usecase.GetAllFilesUseCase
import com.signaturemaker.app.domain.usecase.RemoveAllFilesUseCase
import com.signaturemaker.app.domain.usecase.RemoveFileUseCase
import com.signaturemaker.app.domain.usecase.SaveBitmapUseCase
import com.signaturemaker.app.domain.usecase.SendSuggestUseCase
import org.koin.dsl.module

val domainModule = module {

    // Use Cases
    factory {
        SaveBitmapUseCase(get())
    }

    factory {
        SendSuggestUseCase(get())
    }

    factory {
        GetAllFilesUseCase(get())
    }

    factory {
        RemoveFileUseCase(get())
    }

    factory {
        RemoveAllFilesUseCase(get(), get())
    }
}
