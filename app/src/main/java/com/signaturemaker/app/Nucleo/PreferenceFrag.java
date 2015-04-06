package com.signaturemaker.app.Nucleo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.view.View;

import com.signaturemaker.app.Constantes.PreferencesCons;
import com.signaturemaker.app.Ficheros.Ficheros;
import com.signaturemaker.app.R;

import src.chooser.ChooseFolder;

public class PreferenceFrag extends android.preference.PreferenceFragment {

    private ChooseFolder chos;
    SharedPreferences prefs;
    CheckBoxPreference pref2, pref3, pref4, pref5;
    android.preference.Preference pref1, pref6;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        pref1 = findPreference("opcion1");
        pref2 = (CheckBoxPreference) findPreference("opcion2");
        pref3 = (CheckBoxPreference) findPreference("opcion3");
        pref4 = (CheckBoxPreference) findPreference("opcion4");
        pref5 = (CheckBoxPreference) findPreference("opcion5");
        pref6 = findPreference("opcion6");

        PreferencesCons.pathFiles = prefs.getString(PreferencesCons.OP1, PreferencesCons.pathFiles);
        pref2.setChecked(prefs.getBoolean(PreferencesCons.OP2, false));
        pref3.setChecked(prefs.getBoolean(PreferencesCons.OP3, true));
        pref4.setChecked(prefs.getBoolean(PreferencesCons.OP4, false));
        pref5.setChecked(prefs.getBoolean(PreferencesCons.OP5, false));

        pref1.setSummary(PreferencesCons.pathFiles.replace(PreferencesCons.ROOT, "/sdcard"));


        //  pref3.setSummary(Html.fromHtml( "<font color='red'>" +  getResources().getString(R.string.prefcautionRed) +"</font>" + " " + getResources().getString(R.string.prefcaution)));

    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, final android.preference.Preference preference) {

        final SharedPreferences.Editor editor = prefs.edit();

        switch (preference.getKey()) {


            case "opcion1":


                final View view = getActivity().getLayoutInflater().inflate(R.layout.chooser, null);
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                chos = (ChooseFolder) view.findViewById(R.id.chooserview);
                chos.setPath(PreferencesCons.pathFiles);

                dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {


                    public void onClick(DialogInterface dialog, int id) {


                        PreferencesCons.pathFiles = chos.getPath();

                        editor.putString(PreferencesCons.OP1, PreferencesCons.pathFiles);
                        editor.commit();

                        preference.setSummary(PreferencesCons.pathFiles.replace(PreferencesCons.ROOT, "/sdcard"));

                    }
                });

                dialog.setView(view);
                dialog.show();


                break;
            case "opcion2":

                editor.putBoolean(PreferencesCons.OP2, pref2.isChecked());
                editor.commit();
                break;

            case "opcion3":

                editor.putBoolean(PreferencesCons.OP3, pref3.isChecked());
                editor.commit();

                if (pref3.isChecked()) {
                    Ficheros.nomedia();
                } else {
                    Ficheros.nomediaRemove();
                }

                break;
            case "opcion4":

                editor.putBoolean(PreferencesCons.OP4, pref4.isChecked());
                editor.commit();
                break;
            case "opcion5":

                editor.putBoolean(PreferencesCons.OP5, pref5.isChecked());
                editor.commit();
                break;
            case "opcion6":

                editor.putBoolean(PreferencesCons.OP2, false);
                editor.putBoolean(PreferencesCons.OP3, true);
                editor.putBoolean(PreferencesCons.OP4, false);
                editor.putBoolean(PreferencesCons.OP5, false);

                editor.putInt(PreferencesCons.STROKE, PreferencesCons.TRAZO_GROSOR_ORIGINAL);
                editor.putInt(PreferencesCons.COLOR, PreferencesCons.TRAZO_COLOR_ORIGINAL);

                pref2.setChecked(false);
                pref3.setChecked(true);
                pref4.setChecked(false);
                pref5.setChecked(false);

                editor.putString(PreferencesCons.OP1, PreferencesCons.PATH_SAVE_ORIGINAL);
                pref1.setSummary(PreferencesCons.PATH_SAVE_ORIGINAL.replace(PreferencesCons.ROOT, "/sdcard"));
                editor.commit();

                break;
            default:
                break;

        }


        return super.onPreferenceTreeClick(preferenceScreen, preference);


    }
}