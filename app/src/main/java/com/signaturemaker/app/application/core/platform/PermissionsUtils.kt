/*
 __ _                   _                                 _
/ _(_) __ _ _ __   __ _| |_ _   _ _ __ ___    /\/\   __ _| | _____ _ __
\ \| |/ _` | '_ \ / _` | __| | | | '__/ _ \  /    \ / _` | |/ / _ \ '__|
_\ \ | (_| | | | | (_| | |_| |_| | | |  __/ / /\/\ \ (_| |   <  __/ |
\__/_|\__, |_| |_|\__,_|\__|\__,_|_|  \___| \/    \/\__,_|_|\_\___|_|
      |___/

Copyright (C) 2018  Raúl Rodríguez Concepción www.tuppersoft.com

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
package com.signaturemaker.app.application.core.platform

import android.Manifest.permission
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.CompositeMultiplePermissionsListener
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.multi.SnackbarOnAnyDeniedMultiplePermissionsListener
import com.karumi.dexter.listener.single.BasePermissionListener
import com.karumi.dexter.listener.single.CompositePermissionListener
import com.karumi.dexter.listener.single.PermissionListener
import com.karumi.dexter.listener.single.SnackbarOnDeniedPermissionListener
import com.signaturemaker.app.R

object PermissionsUtils {

    /**
     * Request permission for write
     */
    @JvmOverloads
    fun callRequestPermissionWrite(
        mActivity: Activity,
        myPermissionListener: PermissionListener = BasePermissionListener()
    ) {

        //This listener only call when permission is denied
        val snackbarPermissionListener = SnackbarOnDeniedPermissionListener.Builder.with(
            mActivity.window.decorView.rootView,
            R.string.body_permissions
        )
            .withOpenSettingsButton(R.string.title_setting)
            .build()

        //listener of actions
        val permissionListener = object : PermissionListener {
            override fun onPermissionDenied(response: PermissionDeniedResponse) {
                myPermissionListener.onPermissionDenied(response)
            }

            override fun onPermissionGranted(response: PermissionGrantedResponse) {
                myPermissionListener.onPermissionGranted(response)
            }

            override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest, token: PermissionToken) {
                // shoDialogInformation(token, mActivity);
                token.continuePermissionRequest()
                myPermissionListener.onPermissionRationaleShouldBeShown(permission, token)
            }
        }

        //create compositer with all listener
        val compositePermissionListener = CompositePermissionListener(
            snackbarPermissionListener,
            permissionListener
        )

        Dexter.withContext(mActivity)
            .withPermission(permission.WRITE_EXTERNAL_STORAGE)
            .withListener(compositePermissionListener).onSameThread().check()
    }


    /**
     * @return Is permission acepted
     */
    fun hasPermissionWriteRead(mContext: Context): Boolean {
        return ActivityCompat.checkSelfPermission(
            mContext,
            permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
            mContext,
            permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }
}
