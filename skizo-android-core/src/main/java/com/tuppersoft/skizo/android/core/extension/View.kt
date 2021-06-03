package com.tuppersoft.skizo.android.core.extension

import android.content.Context
import android.view.View
import android.view.View.MeasureSpec
import android.view.ViewGroup.LayoutParams
import android.view.animation.Animation
import android.view.animation.Transformation
import android.view.inputmethod.InputMethodManager

/**
 * Created by Raúl Rodríguez Concepción on 2019-06-16.
 * raulrcs@gmail.com
 */

/**
 * return if a view is visible or not
 */
fun View.isVisible() = this.visibility == View.VISIBLE

/**
 * Show view
 */
fun View.visible() {
    this.post { this.visibility = View.VISIBLE }
}

/**
 * Hide view
 */
fun View.gone() {
    this.post { this.visibility = View.GONE }
}

/**
 * Hide view
 */
fun View.invisible() {
    this.post { this.visibility = View.INVISIBLE }
}

/**
 * Hide keyboard
 */
fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

/**
 * Expand a view
 */
fun View.expand() {
    val v = this
    val matchParentMeasureSpec = MeasureSpec.makeMeasureSpec((this.parent as View).width, MeasureSpec.EXACTLY)
    val wrapContentMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
    v.measure(matchParentMeasureSpec, wrapContentMeasureSpec)
    val targetHeight = this.measuredHeight

    // Older versions of android (pre API 21) cancel animations for views with a height of 0.
    this.layoutParams.height = 1
    this.visibility = View.VISIBLE
    val a: Animation = object : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            v.layoutParams.height =
                if (interpolatedTime == 1f) LayoutParams.WRAP_CONTENT else (targetHeight * interpolatedTime).toInt()
            v.requestLayout()
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }

    // Expansion speed of 1dp/ms
    a.duration = (targetHeight / this.context.resources.displayMetrics.density).toLong()
    this.startAnimation(a)
}

/**
 * Collapse a view
 */
fun View.collapse() {
    val v = this
    val initialHeight = this.measuredHeight
    val a: Animation = object : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            if (interpolatedTime == 1f) {
                v.visibility = View.GONE
            } else {
                v.layoutParams.height = initialHeight - (initialHeight * interpolatedTime).toInt()
                v.requestLayout()
            }
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }

    // Collapse speed of 1dp/ms
    a.duration = (initialHeight / this.context.resources.displayMetrics.density).toLong()
    this.startAnimation(a)
}
