package com.signaturemaker.app.Nucleo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.signaturemaker.app.Constantes.PreferencesCons;
import com.signaturemaker.app.Ficheros.Ficheros;
import com.signaturemaker.app.R;

import de.cketti.library.changelog.ChangeLog;


public class MainActivity extends BaseActivity {

    GestureSignature fgestos;
    ListadoFiles fListado;
    FrameLayout container2;
    AdView mAdView, mAdView2;
    ViewGroup a;
    private Boolean prefPub;
    private SharedPreferences prefs;


    @Override
    protected void onDestroy() {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if (prefs.getBoolean(PreferencesCons.OP2, false)) {
            Ficheros.deleteAllFiles(this);
        }

        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefPub = prefs.getBoolean(PreferencesCons.OPPUB, false);

        mAdView = (AdView) findViewById(R.id.adView);

        if (prefPub) {
            if (mAdView != null)
                a.removeView(mAdView);

        } else {
            if (mAdView == null) {
                Toast.makeText(this, "A", Toast.LENGTH_SHORT).show();

                RelativeLayout.LayoutParams rLParams =
                        new RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                rLParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 1);

                mAdView = new AdView(this);
                mAdView.setAdSize(AdSize.BANNER);
                mAdView.setAdUnitId(getResources().getString(R.string.banner_ad_unit_id));

                AdRequest adRequest = new AdRequest.Builder().build();
                mAdView.loadAd(adRequest);


                a.addView(mAdView, rLParams);

            }


        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        load();
        a = (RelativeLayout) findViewById(R.id.lay);
        mAdView = (AdView) findViewById(R.id.adView);

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
