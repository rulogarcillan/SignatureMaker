package com.signaturemaker.app.Nucleo;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.FrameLayout;

import com.signaturemaker.app.Constantes.PreferencesCons;
import com.signaturemaker.app.Ficheros.Ficheros;
import com.signaturemaker.app.R;

import de.cketti.library.changelog.ChangeLog;


public class MainActivity extends BaseActivity {

    GestureSignature fgestos;
    ListadoFiles fListado;
    FrameLayout container2;


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
        load();
    }


    private void load(){

        fgestos = new GestureSignature();
        fListado = new ListadoFiles();
        setContentView(R.layout.activity_main);

        container2 = (FrameLayout) findViewById(R.id.container2);

        ChangeLog cl = new ChangeLog(this);
        if (cl.isFirstRun()) {
            new BaseActivity.LanzaChangelog(this).getLogDialog().show();
        }


        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fgestos, "GESTOS")
                .commit();
        if (container2 != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container2, fListado, "LISTADO")
                    .commit();
        }


        fgestos.SetOnItemClickListener(new GestureSignature.OnListadoClickListener() {
            @Override
            public void onItemClick(View view, String tag) {

                if (tag.equals("SAVE") && (container2 != null)){
                    fListado.carga();



                }else if (tag.equals("LISTADO")){
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, fListado, "LISTADO")
                            .addToBackStack("LISTADO")
                            .commit();

                }
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        load();
    }

}
