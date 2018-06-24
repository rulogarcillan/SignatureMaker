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
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.signaturemaker.app.R;

import static com.signaturemaker.app.utils.Utils.getAppTimeStamp;


public class BaseActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        switch (menuItem.getItemId()) {

            case R.id.settings:
                break;
            case R.id.action_settings:
                break;
            case R.id.rate:
                startRate();
                break;
            case R.id.more:
                startMoreApps();
                break;
            case R.id.changelog:
                //new LanzaChangelog(BaseActivity.this).getFullLogDialog().show();
                break;
            case R.id.license:
                startLicense();
                break;
            default:
                onBackPressed();
        }
        return true;
    }

    /**
     * Star Activity to rate it
     */
    private void startRate() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=com.signaturemaker.app"));
        startActivity(intent);
    }

    /**
     * Start activity my others play store apps
     */
    private void startMoreApps() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:Raúl R."));
        startActivity(intent);
    }

    /**
     * Start licenses
     */
    private void startLicense() {

        String dateCompilation = getAppTimeStamp(getApplicationContext());
        new LibsBuilder()
                .withFields(R.string.class.getFields())
                .withAutoDetect(true)
                .withVersionShown(true)
                .withLicenseShown(true)
                .withAboutIconShown(true)
                .withAboutVersionShown(true)
                .withAboutDescription(getResources().getString(R.string.app_written) + "&emsp;<a href='https://www.linkedin.com/in/raul-rodriguez-concepcion/'>Linkedin</a>" + "<br/><i>" + dateCompilation + "</i></b>" + "<br/><br/><b>License GNU GPL V3.0</b><br/><br/><a href=\"https://github.com/rulogarcillan/signaturemaker\">Project in Github</a>")
                .withAboutAppName(getString(R.string.title_appName))
                .withActivityTitle(getResources().getString(R.string.title_appName))
                .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
                .start(BaseActivity.this);
    }
}


