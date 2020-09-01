package com.signaturemaker.app.application.core.extensions

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.google.android.material.chip.Chip
import com.signaturemaker.app.R

fun ViewGroup.inflate(@LayoutRes id: Int, attachToRoot: Boolean = false): View =
    LayoutInflater.from(context).inflate(id, this, attachToRoot)

fun ViewGroup.setCategoryChips(
    mActivity: Activity?,
    layout: Int = R.layout.custom_chip,
    tags: List<String> = listOf()
) {
    mActivity?.let {
        for (tag in tags) {
            val mChip = it.layoutInflater.inflate(layout, this, false) as Chip
            mChip.text = tag
            var exist = false
            for (i in 0 until this.childCount) {
                val chip = this.getChildAt(i) as Chip
                if (chip.text == tag) {
                    exist = true
                }
            }
            if (!exist) {
                this.addView(mChip)
            }
        }
    }
}
