package com.signaturemaker.app.domain.models

import androidx.annotation.Keep
import com.squareup.moshi.Json
import java.io.Serializable

@Keep
data class ChangelogDto(
    @Json(name = "changelog")
    val changelog: List<Changelog> = listOf()
) : Serializable
