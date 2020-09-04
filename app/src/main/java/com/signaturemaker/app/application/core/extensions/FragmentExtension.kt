package com.signaturemaker.app.application.core.extensions

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.NavOptions.Builder
import androidx.navigation.fragment.NavHostFragment
import com.signaturemaker.app.R

/**
 * Created by Raúl Rodríguez Concepción on 27/08/2020.

 * raulrcs@gmail.com
 */

inline fun <reified T : Fragment> Fragment.navigate(
    fragmentTransaction: FragmentTransaction,
    container: Int,
    addToBackStack: Boolean = true
): Int {

    if (addToBackStack) {
        fragmentTransaction.addToBackStack(T::class.java.simpleName)
    }

    fragmentTransaction.setCustomAnimations(
        R.anim.enter_from_left,
        R.anim.exit_to_right,
        R.anim.pop_enter_from_right,
        R.anim.pop_exit_to_left
    )
    fragmentTransaction.replace(container, this, T::class.java.simpleName)
    return fragmentTransaction.commitAllowingStateLoss()
}

fun getNavOptions(): NavOptions? {
    return Builder()
        .setEnterAnim(R.anim.enter_from_left)
        .setExitAnim(R.anim.exit_to_right)
        .setPopEnterAnim(R.anim.pop_enter_from_right)
        .setPopExitAnim(R.anim.pop_exit_to_left)
        .build()
}
