package com.signaturemaker.app.domain.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Change(
    @SerializedName("text")
    val text: String,
    @SerializedName("type")
    val type: String
) : Serializable
