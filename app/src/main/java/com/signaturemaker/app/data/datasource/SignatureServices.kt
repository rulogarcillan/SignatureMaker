package com.signaturemaker.app.data.datasource

import com.signaturemaker.app.BuildConfig
import com.signaturemaker.app.domain.models.SuggestMessage
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SignatureServices {

    @POST(BuildConfig.END_POINT + "/public/v1/suggest")
    suspend fun sendSuggest(@Body suggestMessage: SuggestMessage): Response<None>

    class None
}
