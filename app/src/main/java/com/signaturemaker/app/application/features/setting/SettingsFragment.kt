package com.signaturemaker.app.application.features.setting

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.text.HtmlCompat
import androidx.fragment.app.activityViewModels
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.signaturemaker.app.R
import com.signaturemaker.app.R.string
import com.signaturemaker.app.application.core.extensions.Utils.defaultValues
import com.signaturemaker.app.application.core.extensions.Utils.loadAllPreferences
import com.signaturemaker.app.application.core.extensions.Utils.saveAllPreferences
import com.signaturemaker.app.application.features.menu.SettingViewModel
import com.signaturemaker.app.application.utils.Constants
import com.tuppersoft.skizo.core.extension.loadSharedPreference

class SettingsFragment : PreferenceFragmentCompat() {

    private val settingViewModel: SettingViewModel by activityViewModels()
    private val toolbarTitle: String = "Setting"
    private val showBackButton: Boolean = true
    private val showToolbar: Boolean = true

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

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        val countingPreference: androidx.preference.CheckBoxPreference? =
            findPreference(Constants.ID_PREF_ADVERTISING)

        countingPreference?.summaryProvider = Preference.SummaryProvider<androidx.preference.CheckBoxPreference> {
            HtmlCompat.fromHtml(
                "<font color='#d32f2f'>" + resources.getString(string.title_pref_advertising_sum)
                        + "</font>", HtmlCompat.FROM_HTML_MODE_LEGACY
            )
        }
    }

    override fun onPreferenceTreeClick(preference: Preference?): Boolean {

        when (preference?.key) {
            Constants.ID_PREF_RESET -> {
                setDefaultPreferences()
                loadAllPreferences(activity)
            }
            Constants.ID_THEME_MODE->{
               // changeTheme()
                loadAllPreferences(activity)
            }
            else -> {
                loadAllPreferences(activity)
            }
        }
        return super.onPreferenceTreeClick(preference)
    }

    private fun setDefaultPreferences() {
        (findPreference(Constants.ID_PREF_DELETE) as androidx.preference.CheckBoxPreference?)?.isChecked = false
        (findPreference(Constants.ID_PREF_NAME) as androidx.preference.CheckBoxPreference?)?.isChecked = false
        (findPreference(Constants.ID_PREF_COLOR) as androidx.preference.CheckBoxPreference?)?.isChecked = false
        (findPreference(Constants.ID_PREF_STROKE) as androidx.preference.CheckBoxPreference?)?.isChecked = false
        (findPreference(Constants.ID_PREF_ADVERTISING) as androidx.preference.CheckBoxPreference?)?.isChecked = false
        (findPreference(Constants.ID_PREF_WALLPAPER) as androidx.preference.CheckBoxPreference?)?.isChecked = false
        (findPreference(Constants.ID_THEME_MODE) as androidx.preference.ListPreference?)?.setDefaultValue(
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        )
        saveAllPreferences(activity)
        defaultValues()
    }

    private fun changeTheme() {
        (requireActivity() as AppCompatActivity).apply {
            delegate.localNightMode = loadSharedPreference(
                Constants.ID_THEME_MODE,
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            )
        }
    }
}
