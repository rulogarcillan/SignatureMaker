package com.signaturemaker.app.domain.models

import com.google.gson.annotations.SerializedName
import com.signaturemaker.app.domain.models.Change
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Date

data class Changelog(
    @SerializedName("change")
    val change: List<Change> = listOf(),
    @SerializedName("date")
    val date: String,
    @SerializedName("versionCode")
    val versionCode: Int,
    @SerializedName("versionName")
    val versionName: String
) : Serializable {

    fun getDateInLong(): Long {
        val formatter = SimpleDateFormat("dd/MM/yyyy")
        val d = formatter.parse(date) as Date
        return d.time
    }
}
