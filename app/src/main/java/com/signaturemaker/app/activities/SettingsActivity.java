package com.signaturemaker.app.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.signaturemaker.app.R;
import com.signaturemaker.app.utils.Constants;
import com.signaturemaker.app.utils.FilesUtils;
import com.signaturemaker.app.utils.PermissionsUtils;
import com.signaturemaker.app.utils.Utils;

import java.util.List;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
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

    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
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
        }
    };


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
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                (String) (PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), Utils.path)).replace(Constants.ROOT, "/sdcard"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
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
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || GeneralPreferenceFragment.class.getName().equals(fragmentName);
    }


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
            //  bindPreferenceSummaryToValue(findPreference("example_list"));

            CheckBoxPreference prefAds = (CheckBoxPreference) findPreference(Constants.ID_PREF_ADVERTISING);
            prefAds.setSummary(Html.fromHtml("<font color='red'>" + getResources().getString(R.string.title_pref_advertising_sum) + "</font>"));
        }

        @Override
        public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {

            switch (preference.getKey()) {

                case Constants.ID_PREF_PATH:

                    PermissionsUtils.getInstance().callRequestPermissions(getActivity(), PermissionsUtils.permissionsReadWrite, new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            showDialogPath();
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        }
                    });
                    break;
                case Constants.ID_PREF_RESET:
                    setDefaultPreferences();
                default:
                    break;
            }
            return super.onPreferenceTreeClick(preferenceScreen, preference);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }


        private void showDialogPath() {
            View view = getActivity().getLayoutInflater().inflate(R.layout.chooser_path, null);
            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            final ChooseFolder chos = (ChooseFolder) view.findViewById(R.id.chooserview);
            chos.setPath(Utils.path);

            dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int id) {
                    String oldPath = Utils.path;
                    Utils.path = chos.getPath();
                    Utils.savePreference(findPreference(Constants.ID_PREF_PATH).getContext(), Constants.ID_PREF_PATH, Utils.path);
                    bindPreferenceSummaryToValue(findPreference(Constants.ID_PREF_PATH));
                    FilesUtils.moveFiles(oldPath, getActivity());
                }
            });

            dialog.setView(view);
            dialog.show();
        }

        private void setDefaultPreferences() {

            findPreference(Constants.ID_PREF_PATH).setDefaultValue(Constants.DEFAULT_PATH);
            String oldPath = Utils.path;
            Utils.defaultPath();
            Utils.savePreference(findPreference(Constants.ID_PREF_PATH).getContext(), Constants.ID_PREF_PATH, Utils.path);
            bindPreferenceSummaryToValue(findPreference(Constants.ID_PREF_PATH));
            FilesUtils.moveFiles(oldPath, getActivity());

            ((CheckBoxPreference) findPreference(Constants.ID_PREF_DELETE)).setChecked(false);
            ((CheckBoxPreference) findPreference(Constants.ID_PREF_GALLERY)).setChecked(true);
            ((CheckBoxPreference) findPreference(Constants.ID_PREF_NAME)).setChecked(false);
            ((CheckBoxPreference) findPreference(Constants.ID_PREF_COLOR)).setChecked(false);
            ((CheckBoxPreference) findPreference(Constants.ID_PREF_STROKE)).setChecked(false);
            ((CheckBoxPreference) findPreference(Constants.ID_PREF_ADVERTISING)).setChecked(false);
            Utils.saveAllPreferences(getActivity());
            Utils.defaultValues();


        }
    }

}
