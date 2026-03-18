
package com.signaturemaker.app.domain.models

import android.net.Uri
import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class ItemFile(
    val uri: Uri = Uri.EMPTY,
    val name: String,
    val date: String = "",
    val size: String = "",
    val shimmer: Boolean = false
) : Parcelable
