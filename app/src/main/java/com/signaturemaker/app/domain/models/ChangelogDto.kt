package com.signaturemaker.app.domain.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ChangelogDto(
    @SerializedName("changelog")
    val changelog: List<Changelog> = listOf()
) : Serializable
