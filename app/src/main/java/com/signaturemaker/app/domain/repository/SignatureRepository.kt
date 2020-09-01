package com.signaturemaker.app.domain.repository

import com.signaturemaker.app.domain.models.SuggestMessage
import com.tuppersoft.skizo.core.domain.response.Response
import kotlinx.coroutines.flow.Flow

interface SignatureRepository {

    suspend fun sendSuggest(suggestMessage: SuggestMessage): Flow<Response<Boolean>>
}
