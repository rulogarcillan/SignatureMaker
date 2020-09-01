package com.signaturemaker.app.domain.usecase

import com.signaturemaker.app.domain.models.SuggestMessage
import com.signaturemaker.app.domain.repository.SignatureRepository
import com.tuppersoft.skizo.core.domain.baseusecase.FlowGlobalUseCase
import com.tuppersoft.skizo.core.domain.response.Response
import kotlinx.coroutines.flow.Flow

/**
 * Created by Raúl Rodríguez Concepción on 2019-12-23.
 * raulrcs@gmail.com
 */
class SendSuggest constructor(private val repository: SignatureRepository) :
    FlowGlobalUseCase<Boolean, SendSuggest.Params>() {

    data class Params(
        var suggestMessage: SuggestMessage
    )

    override suspend fun run(params: Params): Flow<Response<Boolean>> {
        return repository.sendSuggest(params.suggestMessage)
    }
}
