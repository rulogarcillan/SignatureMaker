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

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.preference.PreferenceActivity;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;

import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.signaturemaker.app.R;

import androidx.appcompat.app.AppCompatActivity;
import de.cketti.library.changelog.ChangeLog;

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
                Intent intent = new Intent(this, SettingsActivity.class);
                intent.putExtra(PreferenceActivity.EXTRA_SHOW_FRAGMENT, SettingsActivity.GeneralPreferenceFragment.class.getName());
                intent.putExtra(PreferenceActivity.EXTRA_NO_HEADERS, true);
                startActivity(intent);
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
                startChangelog(true);
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

    /**
     * Start changelog
     *
     * @param forceStart force start, if params is false only start the firts time
     */
    protected void startChangelog(Boolean forceStart) {

        myChangeLog cl = new myChangeLog(this);
        if (cl.isFirstRun()) {
            cl.getLogDialog().show();
        } else if (forceStart) {
            cl.getFullLogDialog().show();
        }


    }


    public static class myChangeLog extends ChangeLog {


        public static final String DEFAULT_CSS =

                "body {                                                           " + "	font-family: Verdana, Helvetica, Arial, sans-serif;   " + "	font-size: 11px;                                      " + "	color: #000000;                                       " + "	background-color: #ffffff;                            " + "	margin: 0px;                                          " + "	padding: 0px;                                         " + "}                                                        "
                        + "h1 {                                                     " + "	font-size: 14px;                                      " + "	font-weight: bold;                                    " + "	text-transform: uppercase;                            " + "	color: #000000;                                       " + "	margin: 0px;                                          " + "	padding: 10px 0px 0px 8px;                            " + "}                                                        "
                        + "h2 {                                                     " + "	font-size: 10px;                                      " + "	color: #999999;                                       " + "	font-weight: normal;                                  " + "	margin: 0px 0px 0px 8px;                              " + "	padding: 0px;                                         " + "}                                                        " + "ul {                                                     "
                        + "	margin: 0px 0px 10px 15px;                            " + "	padding-left: 15px;                                " + "	padding-top: 8px;                                     " + "	list-style-type: square;                              " + "	color: #999999;                                       " + "}";

        public myChangeLog(Context context) {
            super(new ContextThemeWrapper(context, R.style.AppTheme), DEFAULT_CSS);
        }
    }
}

