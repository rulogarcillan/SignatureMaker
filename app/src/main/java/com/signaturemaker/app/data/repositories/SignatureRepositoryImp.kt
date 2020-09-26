package com.signaturemaker.app.data.repositories

import com.signaturemaker.app.data.datasource.SignatureServices
import com.signaturemaker.app.domain.models.SuggestMessage
import com.signaturemaker.app.domain.repository.SignatureRepository
import com.tuppersoft.skizo.kotlin.core.domain.exception.Failure
import com.tuppersoft.skizo.kotlin.core.domain.response.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SignatureRepositoryImp @Inject constructor(
    private val api: SignatureServices
) : SignatureRepository {

    override suspend fun sendSuggest(suggestMessage: SuggestMessage): Flow<Response<Boolean>> {
        return flow {

            try {
                val response = api.sendSuggest(suggestMessage)
                if (response.isSuccessful) {
                    emit(Response.onSuccess(true))
                } else {
                    emit(Response.onFailure(Failure.ServerError))
                }
            } catch (t: Exception) {
                emit(Response.onFailure(Failure.ServerError))
            }

        }
    }
}
