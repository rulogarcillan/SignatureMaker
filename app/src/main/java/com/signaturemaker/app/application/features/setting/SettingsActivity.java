package com.signaturemaker.app.application.features.setting;/*
 __ _                   _                                 _
/ _(_) __ _ _ __   __ _| |_ _   _ _ __ ___    /\/\   __ _| | _____ _ __
\ \| |/ _` | '_ \ / _` | __| | | | '__/ _ \  /    \ / _` | |/ / _ \ '__|
_\ \ | (_| | | | | (_| | |_| |_| | | |  __/ / /\/\ \ (_| |   <  __/ |
\__/_|\__, |_| |_|\__,_|\__|\__,_|_|  \___| \/    \/\__,_|_|\_\___|_|
      |___/

Copyright (C) 2018  Raúl Rodríguez Concepción www.wepica.com

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.*/

import android.app.Activity;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.signaturemaker.app.R;
import com.signaturemaker.app.application.core.extensions.Utils;
import com.signaturemaker.app.application.core.platform.FilesUtils;
import com.signaturemaker.app.application.core.platform.PermissionsUtils;
import com.signaturemaker.app.application.utils.Constants;
import com.signaturemaker.app.data.repositories.SharedPreferencesRepository;
import src.chooser.ChooseFolder;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends AppCompatPreferenceActivity {


    public static class GeneralPreferenceFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValue(findPreference(Constants.ID_PREF_PATH));
            bindPreferenceSummaryToValue(findPreference(Constants.ID_PREF_GALLERY));

            CheckBoxPreference prefAds = (CheckBoxPreference) findPreference(Constants.ID_PREF_ADVERTISING);
            prefAds.setSummary(Html.fromHtml(
                    "<font color='red'>" + getResources().getString(R.string.title_pref_advertising_sum)
                            + "</font>"));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                // startActivity(new Intent(getActivity(), SettingsActivity.class));
                getActivity().onBackPressed();
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

        @Override
        public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {

            switch (preference.getKey()) {
                case Constants.ID_PREF_PATH:
                    PermissionsUtils.Companion.getInstance()
                            .callRequestPermissionWrite(getActivity(), new PermissionListener() {
                                @Override
                                public void onPermissionDenied(PermissionDeniedResponse response) {
                                }

                                @Override
                                public void onPermissionGranted(PermissionGrantedResponse response) {
                                    showDialogPath();
                                }

                                @Override
                                public void onPermissionRationaleShouldBeShown(PermissionRequest permission,
                                        PermissionToken token) {
                                }
                            });

                    break;
                case Constants.ID_PREF_RESET:
                    setDefaultPreferences();
                case Constants.ID_PREF_DELETE:
                case Constants.ID_PREF_NAME:
                case Constants.ID_PREF_ADVERTISING:
                case Constants.ID_PREF_WALLPAPER:
                case Constants.ID_PREF_COLOR:
                case Constants.ID_PREF_STROKE:

                    Utils.INSTANCE.loadAllPreferences(getActivity());
                    break;
                default:
                    break;
            }
            return super.onPreferenceTreeClick(preferenceScreen, preference);
        }

        private void setDefaultPreferences() {

            findPreference(Constants.ID_PREF_PATH).setDefaultValue(Constants.DEFAULT_PATH);
            String oldPath = Utils.INSTANCE.getPath();
            Utils.INSTANCE.defaultPath();
            SharedPreferencesRepository.INSTANCE
                    .savePreference(findPreference(Constants.ID_PREF_PATH).getContext(), Constants.ID_PREF_PATH,
                            Utils.INSTANCE.getPath());
            bindPreferenceSummaryToValue(findPreference(Constants.ID_PREF_PATH));
            FilesUtils.INSTANCE.moveFiles(oldPath, getActivity());
            ((CheckBoxPreference) findPreference(Constants.ID_PREF_DELETE)).setChecked(false);
            ((CheckBoxPreference) findPreference(Constants.ID_PREF_GALLERY)).setChecked(true);
            FilesUtils.INSTANCE.noMedia(getActivity());
            //  FilesUtils.moveFiles(oldPath, getActivity());
            // FilesUtils.reScan(getActivity());
            ((CheckBoxPreference) findPreference(Constants.ID_PREF_NAME)).setChecked(false);
            ((CheckBoxPreference) findPreference(Constants.ID_PREF_COLOR)).setChecked(false);
            ((CheckBoxPreference) findPreference(Constants.ID_PREF_STROKE)).setChecked(false);
            ((CheckBoxPreference) findPreference(Constants.ID_PREF_ADVERTISING)).setChecked(false);
            ((CheckBoxPreference) findPreference(Constants.ID_PREF_WALLPAPER)).setChecked(false);
            Utils.INSTANCE.saveAllPreferences(getActivity());
            Utils.INSTANCE.defaultValues();


        }

        private void showDialogPath() {
            View view = getActivity().getLayoutInflater().inflate(R.layout.chooser_path, null);
            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            final ChooseFolder chos = view.findViewById(R.id.chooserview);
            chos.setPath(Utils.INSTANCE.getPath());

            dialog.setPositiveButton(android.R.string.ok, (dialog1, id) -> {
                String oldPath = Utils.INSTANCE.getPath();
                Utils.INSTANCE.setPath(chos.getPath());
                SharedPreferencesRepository.INSTANCE
                        .savePreference(findPreference(Constants.ID_PREF_PATH).getContext(),
                                Constants.ID_PREF_PATH,
                                Utils.INSTANCE.getPath());
                bindPreferenceSummaryToValue(findPreference(Constants.ID_PREF_PATH));
                FilesUtils.INSTANCE.moveFiles(oldPath, getActivity());
            });

            dialog.setView(view);
            dialog.show();
        }
    }

    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener
            = (preference, value) -> {
        String stringValue = value.toString();

        if (preference instanceof ListPreference) {
            // For list preferences, look up the correct display value in
            // the preference's 'entries' list.
            ListPreference listPreference = (ListPreference) preference;
            int index = listPreference.findIndexOfValue(stringValue);

            // Set the summary to reflect the new value.
            preference.setSummary(
                    index >= 0
                            ? listPreference.getEntries()[index]
                            : null);


        } else {
            // For all other preferences, set the summary to the value's
            // simple string representation.
            preference.setSummary(stringValue);
        }
        return true;
    };

    private static Preference.OnPreferenceChangeListener sBindPreferenceGalleryListener
            = (preference, o) -> {
        if ((Boolean) o) {
            FilesUtils.INSTANCE.noMedia((Activity) preference.getContext());
        } else {
            FilesUtils.INSTANCE.noMediaRemove((Activity) preference.getContext());
        }
        return true;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || GeneralPreferenceFragment.class.getName().equals(fragmentName);
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {

        if (preference.getKey().equalsIgnoreCase(Constants.ID_PREF_PATH)) {

            // Set the listener to watch for value changes.
            preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

            // Trigger the listener immediately with the preference's
            // current value.
            sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                    (PreferenceManager
                            .getDefaultSharedPreferences(preference.getContext())
                            .getString(preference.getKey(), Utils.INSTANCE.getPath()))
                            .replace(Constants.ROOT, "/sdcard"));
        } else if (preference.getKey().equalsIgnoreCase(Constants.ID_PREF_GALLERY)) {
            preference.setOnPreferenceChangeListener(sBindPreferenceGalleryListener);
        }
    }

}
