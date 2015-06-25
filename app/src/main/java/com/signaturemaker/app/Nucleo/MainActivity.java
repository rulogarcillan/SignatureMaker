package com.signaturemaker.app.Nucleo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.signaturemaker.app.Constantes.PreferencesCons;
import com.signaturemaker.app.Ficheros.Ficheros;
import com.signaturemaker.app.R;

import de.cketti.library.changelog.ChangeLog;


public class MainActivity extends BaseActivity {

    GestureSignature fgestos;
    ListadoFiles fListado;
    FrameLayout container2;
    AdView mAdView;
    ViewGroup a;
    private Boolean prefPub, prefAnt;
    private SharedPreferences prefs;


    @Override
    protected void onDestroy() {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if (prefs.getBoolean(PreferencesCons.OP2, false)) {
            Ficheros.deleteAllFiles(this);
        }

        super.onDestroy();
    }

    public void onRestart() {
        super.onRestart();
        prefPub = prefs.getBoolean(PreferencesCons.OPPUB, false);
        if ((prefPub != prefAnt) && (prefPub == false)){
            Intent intent = new Intent();
            intent.setClass(this, this.getClass());
            finish();
            this.startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        prefPub = prefs.getBoolean(PreferencesCons.OPPUB, false);
        prefAnt = prefPub;

        //mAdView = (AdView) findViewById(R.id.adView);

        if ((prefPub) && (mAdView != null))
                a.removeView(mAdView);



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        load();
        a = (RelativeLayout) findViewById(R.id.lay);
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }


    private void load() {

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

                if (tag.equals("SAVE") && (container2 != null)) {
                    fListado.carga();


                } else if (tag.equals("LISTADO")) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, fListado, "LISTADO")
                            .addToBackStack("LISTADO")
                            .commit();

                }
            }
        });
    }


}
