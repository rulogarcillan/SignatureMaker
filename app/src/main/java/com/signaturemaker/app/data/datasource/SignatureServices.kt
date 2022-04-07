package com.signaturemaker.app.data.datasource

import com.signaturemaker.app.BuildConfig
import com.signaturemaker.app.domain.models.SuggestMessage
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface SignatureServices {

    @POST(BuildConfig.END_POINT + "/public/v1/suggest")
    @Headers("app-name: ${BuildConfig.APP_NAME}", "version-name: ${BuildConfig.VERSION_NAME}")
    suspend fun sendSuggest(@Body suggestMessage: SuggestMessage): Response<None>

    class None
}
