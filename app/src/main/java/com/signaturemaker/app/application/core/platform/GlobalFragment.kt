package com.signaturemaker.app.application.core.platform

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.signaturemaker.app.application.features.menu.SettingViewModel

/**
 * Created by Raúl Rodríguez Concepción on 2019-09-27.

 * raulrcs@gmail.com
 */
abstract class GlobalFragment : Fragment() {

    private val settingViewModel: SettingViewModel by activityViewModels()
    abstract val toolbarTitle: String
    abstract val showBackButton: Boolean
    open val showToolbar: Boolean = true

    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)
        configToolbar()
    }

    private fun configToolbar() {
        settingViewModel.toolBarTitle(toolbarTitle)
        settingViewModel.backButton(showBackButton)
        settingViewModel.showToolbar(showToolbar)
    }
}
