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
package com.signaturemaker.app.utils;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.os.Build;
import android.view.ViewGroup;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.BaseMultiplePermissionsListener;
import com.karumi.dexter.listener.multi.CompositeMultiplePermissionsListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.multi.SnackbarOnAnyDeniedMultiplePermissionsListener;
import com.karumi.dexter.listener.single.BasePermissionListener;
import com.karumi.dexter.listener.single.CompositePermissionListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.karumi.dexter.listener.single.SnackbarOnDeniedPermissionListener;
import com.signaturemaker.app.R;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import androidx.appcompat.app.AlertDialog;


public class PermissionsUtils {

    public static final List<String> permissionsReadWrite = Arrays.asList( Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE);

    private static PermissionsUtils INSTANCE = null;

    private PermissionsUtils() {
    }

    private synchronized static void createInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PermissionsUtils();
        }
    }

    public static PermissionsUtils getInstance() {
        if (INSTANCE == null) {
            createInstance();
        }
        return INSTANCE;
    }

    /**
     * Request permission for record audio without listener
     * @param mActivity
     */
    public void callRequestPermissionrecordAudio(final Activity mActivity) {
        callRequestPermissionWrite(mActivity, new BasePermissionListener());
    }

    /**
     * Request permission for multiple permission without listener
     * @param mActivity
     */
    public void callRequestPermissions(final Activity mActivity, Collection<String> permissions) {
        callRequestPermissions(mActivity, permissions, new BaseMultiplePermissionsListener());
    }

    /**
     * Request permission for record audio
     * @param mActivity
     * @param myPermissionListener
     */
    public void callRequestPermissionWrite(final Activity mActivity, final PermissionListener myPermissionListener) {

        //This listener only call when permission is denied
        PermissionListener snackbarPermissionListener = SnackbarOnDeniedPermissionListener.Builder.
                 with((ViewGroup) mActivity.getWindow().getDecorView().getRootView(), R.string.body_permissions)
                .withOpenSettingsButton(R.string.title_setting)
                .build();

        //listener of actions
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                myPermissionListener.onPermissionGranted(response);
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                myPermissionListener.onPermissionDenied(response);
            }
            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                shoDialogInformation(token, mActivity);
                myPermissionListener.onPermissionRationaleShouldBeShown(permission, token);
                token.continuePermissionRequest();
            }
        };

        //create compositer with all listener
        PermissionListener compositePermissionListener = new CompositePermissionListener(snackbarPermissionListener, permissionListener);

        Dexter.withActivity(mActivity)
                .withPermission(Manifest.permission.RECORD_AUDIO)
                .withListener(compositePermissionListener).onSameThread().check();
    }


    public void callRequestPermissions(final Activity mActivity, Collection<String> permissions, final MultiplePermissionsListener myPermissionListener) {

        //This listener only call when permission is denied
        MultiplePermissionsListener snackbarPermissionListener = SnackbarOnAnyDeniedMultiplePermissionsListener.Builder.
                with((ViewGroup) mActivity.getWindow().getDecorView().getRootView(), R.string.body_permissions)
                .withOpenSettingsButton(R.string.title_setting)
                .build();

        //listener of actions
        MultiplePermissionsListener permissionListener = new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                myPermissionListener.onPermissionsChecked(report);
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                myPermissionListener.onPermissionRationaleShouldBeShown(permissions, token);
            }
        };
        MultiplePermissionsListener compositePermissionsListener = new CompositeMultiplePermissionsListener(snackbarPermissionListener, permissionListener);
        Dexter.withActivity(mActivity)
                .withPermissions(permissions)
                .withListener(compositePermissionsListener).onSameThread().check();
    }

    /**
     * Show a dialog with informations of permission
     * @param token
     * @param mActivity
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void shoDialogInformation(final PermissionToken token, Activity mActivity) {
        new AlertDialog.Builder(mActivity).setTitle(R.string.title_request_permision)
                .setMessage(R.string.body_request_permision)
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        token.cancelPermissionRequest();
                    }
                })
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        token.continuePermissionRequest();
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        token.cancelPermissionRequest();
                    }
                })
                .show();
    }
}
