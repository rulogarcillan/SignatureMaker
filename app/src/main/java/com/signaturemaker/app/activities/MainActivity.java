/*
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
package com.signaturemaker.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.signaturemaker.app.R;
import com.signaturemaker.app.fragments.ListFilesFragment;
import com.signaturemaker.app.fragments.SingBoardFragment;
import com.signaturemaker.app.interfaces.ClickInterface;
import com.signaturemaker.app.utils.FilesUtils;
import com.signaturemaker.app.utils.PermissionsUtils;
import com.signaturemaker.app.utils.Utils;

import java.util.List;

import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends BaseActivity implements ClickInterface {

    private AdView mAdView;
    private RelativeLayout layoutMain;
    private boolean flagAdvertising;
    private FrameLayout containerFiles;
    private SingBoardFragment signBoard;
    private ListFilesFragment listFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //ButterKnife.bind(this);
        Utils.loadAllPreferences(this);

        containerFiles = (FrameLayout) findViewById(R.id.containerFiles);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, signBoard = new SingBoardFragment(), SingBoardFragment.class.getSimpleName());
        ft.commit();
        signBoard.setInterface(this);

        createTableView();

        startChangelog(false);

        mAdView = findViewById(R.id.adView);
        layoutMain = findViewById(R.id.layoutMain);


        initAdvertising();


    }

    private void createTableView() {
        if (containerFiles != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.containerFiles, listFragment = new ListFilesFragment(), ListFilesFragment.class.getSimpleName())
                    .commit();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (Utils.deleteExit) {
            PermissionsUtils.getInstance().callRequestPermissionWrite(this);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Utils.deleteExit && PermissionsUtils.hasPermissionWriteRead(this)) {
            FilesUtils.deleteAllFiles(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        flagAdvertising = Utils.disableAds;
        hideAdvertising();
    }


    @Override
    public void onRestart() {
        super.onRestart();
        showAdvertising();
    }


    /**
     * show advertising if options change
     */
    private void showAdvertising() {
        if ((Utils.disableAds != flagAdvertising) && (Utils.disableAds == false)) {
            Intent intent = new Intent();
            intent.setClass(this, this.getClass());
            finish();
            this.startActivity(intent);
        }
    }


    /**
     * hide advertising if options is selected
     */
    private void hideAdvertising() {
        if ((Utils.disableAds) && (mAdView != null))
            layoutMain.removeView(mAdView);
    }

    /**
     * Init advertising
     */
    private void initAdvertising() {
        mAdView.setVisibility(View.GONE);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                mAdView.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void buttonClicked() {
        if (containerFiles != null) {
            listFragment.reloadFiles();
        }
    }
}
