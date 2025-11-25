package com.signaturemaker.app.domain.models

import androidx.annotation.Keep
import com.squareup.moshi.Json
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Date

@Keep
data class Changelog(
    @Json(name = "change")
    val change: List<Change> = listOf(),
    @Json(name = "date")
    val date: String?,
    @Json(name = "versionCode")
    val versionCode: Int,
    @Json(name = "versionName")
    val versionName: String
) : Serializable {

    fun getDateInLong(): Long? {
        return if (date == null) {
            null
        } else {
            val formatter = SimpleDateFormat("dd/MM/yyyy")
            val d = formatter.parse(date) as Date
            d.time
        }
    }
}
