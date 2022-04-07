package com.signaturemaker.app.domain.models

import androidx.annotation.Keep
import com.squareup.moshi.Json
import java.io.Serializable

@Keep
data class Change(
    @Json(name = "text")
    val text: String,
    @Json(name = "type")
    val type: String
) : Serializable
