package com.signaturemaker.app.application.core.extensions

import androidx.navigation.NavOptions
import androidx.navigation.NavOptions.Builder
import com.signaturemaker.app.R

/**
 * Created by Raúl Rodríguez Concepción on 27/08/2020.

 * raulrcs@gmail.com
 */

fun getNavOptions(): NavOptions? {
    return Builder()
        .setEnterAnim(R.anim.enter_from_left)
        .setExitAnim(R.anim.exit_to_right)
        .setPopEnterAnim(R.anim.pop_enter_from_right)
        .setPopExitAnim(R.anim.pop_exit_to_left)
        .build()
}
