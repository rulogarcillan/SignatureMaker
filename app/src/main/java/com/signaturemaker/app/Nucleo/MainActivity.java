package com.signaturemaker.app.Nucleo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import com.signaturemaker.app.Constantes.PreferencesCons;
import com.signaturemaker.app.Ficheros.Ficheros;
import com.signaturemaker.app.R;

import de.cketti.library.changelog.ChangeLog;


public class MainActivity extends BaseActivity {

    GestureSignature fgestos = new GestureSignature();
    ListadoFiles fListado = new ListadoFiles();


    @Override
    protected void onDestroy() {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if (prefs.getBoolean(PreferencesCons.OP2, false)) {
            Ficheros.deleteAllFiles(this);
        }

        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ChangeLog cl = new ChangeLog(this);
        if (cl.isFirstRun()) {
            new BaseActivity.LanzaChangelog(this).getLogDialog().show();
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fgestos, "GESTOS")

                    .commit();
        }
        fgestos.SetOnItemClickListener(new GestureSignature.OnListadoClickListener() {
            @Override
            public void onItemClick(View view) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fListado, "LISTADO")
                        .addToBackStack("LISTADO")
                        .commit();
            }
        });

    }

}
