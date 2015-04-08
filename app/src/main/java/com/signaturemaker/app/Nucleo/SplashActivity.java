package com.signaturemaker.app.Nucleo;


import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.signaturemaker.app.Ficheros.Ficheros;
import com.signaturemaker.app.R;

import java.io.File;

import me.relex.circleindicator.CircleIndicator;

import static com.signaturemaker.app.Constantes.PreferencesCons.OP1;
import static com.signaturemaker.app.Constantes.PreferencesCons.OP3;
import static com.signaturemaker.app.Constantes.PreferencesCons.OP4;
import static com.signaturemaker.app.Constantes.PreferencesCons.OP5;
import static com.signaturemaker.app.Constantes.PreferencesCons.TRAZO_COLOR_ORIGINAL;
import static com.signaturemaker.app.Constantes.PreferencesCons.TRAZO_GROSOR_ORIGINAL;
import static com.signaturemaker.app.Constantes.PreferencesCons.colorTrazo;
import static com.signaturemaker.app.Constantes.PreferencesCons.pathFiles;
import static com.signaturemaker.app.Constantes.PreferencesCons.strokeTrazo;

public class SplashActivity extends FragmentActivity {


    ViewPager pager = null;
    PagerAdapter pagerAdapter;
    Boolean pref3, pref4, pref5;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        this.setContentView(R.layout.splash);

        CircleIndicator defaultIndicator = (CircleIndicator) findViewById(R.id.indicator_default);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        //cargamos la ruta
        pathFiles = prefs.getString(OP1, pathFiles);

        //creamos nomedia o borramos segun preferencia.
        pref3 = prefs.getBoolean(OP3, true);
        if (pref3) {
            Ficheros.nomedia();
        } else {
            Ficheros.nomediaRemove();
        }
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(pathFiles + ".nomedia"))));
        //fin nomedia

        pref4 = prefs.getBoolean(OP4, false);
        if (!pref4)
            colorTrazo = TRAZO_COLOR_ORIGINAL;

        pref5 = prefs.getBoolean(OP5, false);
        if (!pref5)
            strokeTrazo = TRAZO_GROSOR_ORIGINAL;

        Ficheros.CreaPath();

        //no es la primera vez
        if (!prefs.getBoolean("first_time", true)) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            finish();
        }

        //gestión de ficheros antiguos de la version 1.0
        Ficheros.moveFilesVersionAntigua(this);

        // Instantiate a ViewPager
        this.pager = (ViewPager) this.findViewById(R.id.viewpager);

        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new SplashFragment());
        pagerAdapter.addFragment(new SplashFragment());
        pagerAdapter.addFragment(new SplashFragment());
        pagerAdapter.addFragment(new SplashFragment());

        pagerAdapter.getItem(0).setPos(1);

        int i = 0;
        for (SplashFragment foo : pagerAdapter.getAllFragment()) {
            i++;
            foo.setPos(i);
        }

        this.pager.setAdapter(pagerAdapter);
        defaultIndicator.setViewPager(pager);

    }

    @Override
    public void onBackPressed() {


        if (this.pager.getCurrentItem() == 0)
            super.onBackPressed();
        else
            this.pager.setCurrentItem(this.pager.getCurrentItem() - 1);

    }

}