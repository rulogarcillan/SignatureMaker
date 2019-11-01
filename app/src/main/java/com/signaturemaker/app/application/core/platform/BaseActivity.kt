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

package com.signaturemaker.app.application.core.platform

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.preference.PreferenceActivity
import android.view.ContextThemeWrapper
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.LibsBuilder
import com.signaturemaker.app.R
import com.signaturemaker.app.application.core.extensions.Utils.getAppTimeStamp
import com.signaturemaker.app.application.features.setting.SettingsActivity
import com.signaturemaker.app.application.features.setting.SettingsActivity.GeneralPreferenceFragment
import de.cketti.library.changelog.ChangeLog

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {

        when (menuItem.itemId) {

            R.id.settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                intent.putExtra(
                    PreferenceActivity.EXTRA_SHOW_FRAGMENT,
                    GeneralPreferenceFragment::class.java.name
                )
                intent.putExtra(PreferenceActivity.EXTRA_NO_HEADERS, true)
                startActivity(intent)
            }
            R.id.action_settings -> {
            }
            R.id.rate -> startRate()
            R.id.more -> startMoreApps()
            R.id.changelog -> {
                //startChangelog(true)
            }
            R.id.license -> startLicense()
            R.id.privacy_policy -> openPrivacy()
            else -> onBackPressed()
        }
        return true
    }

    private fun openPrivacy() {
        val browserIntent =
            Intent(Intent.ACTION_VIEW, Uri.parse("https://info.tuppersoft.com/privacy/privacy_policy_signature.html"))
        startActivity(browserIntent)
    }

    /**
     * Star Activity to rate it
     */
    private fun startRate() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("market://details?id=com.signaturemaker.app")
        startActivity(intent)
    }

    /**
     * Start activity my others play store apps
     */
    private fun startMoreApps() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:Raúl R."))
        startActivity(intent)
    }

    /**
     * Start licenses
     */
    private fun startLicense() {

        val dateCompilation = getAppTimeStamp(applicationContext)
        LibsBuilder()
            .withFields(R.string::class.java.fields)
            .withAutoDetect(true)
            .withVersionShown(true)
            .withLicenseShown(true)
            .withAboutIconShown(true)
            .withAboutVersionShown(true)
            .withAboutDescription(resources.getString(R.string.app_written) + "&emsp;<a href='https://www.linkedin.com/in/raul-rodriguez-concepcion/'>Linkedin</a>" + "<br/><i>" + dateCompilation + "</i></b>" + "<br/><br/><b>License GNU GPL V3.0</b><br/><br/><a href=\"https://github.com/rulogarcillan/signaturemaker\">Project in Github</a>")
            .withAboutAppName(getString(R.string.title_appName))
            .withActivityTitle(resources.getString(R.string.title_appName))
            .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
            .start(this@BaseActivity)
    }

    /**
     * Start changelog
     *
     * @param forceStart force start, if params is false only start the firts time
     */
    protected fun startChangelog(forceStart: Boolean) {

        val cl = MyChangeLog(this)
        if (cl.isFirstRun) {
            cl.logDialog.show()
        } else if (forceStart) {
            cl.fullLogDialog.show()
        }
    }

    class MyChangeLog(context: Context) : ChangeLog(
        ContextThemeWrapper(context, R.style.AppTheme),
        DEFAULT_CSS
    ) {

        companion object {
            const val DEFAULT_CSS = (

                    "body {                                                           " + "	font-family: Verdana, Helvetica, Arial, sans-serif;   " + "	font-size: 11px;                                      " + "	color: #000000;                                       " + "	background-color: #ffffff;                            " + "	margin: 0px;                                          " + "	padding: 0px;                                         " + "}                                                        "
                            + "h1 {                                                     " + "	font-size: 14px;                                      " + "	font-weight: bold;                                    " + "	text-transform: uppercase;                            " + "	color: #000000;                                       " + "	margin: 0px;                                          " + "	padding: 10px 0px 0px 8px;                            " + "}                                                        "
                            + "h2 {                                                     " + "	font-size: 10px;                                      " + "	color: #999999;                                       " + "	font-weight: normal;                                  " + "	margin: 0px 0px 0px 8px;                              " + "	padding: 0px;                                         " + "}                                                        " + "ul {                                                     "
                            + "	margin: 0px 0px 10px 15px;                            " + "	padding-left: 15px;                                " + "	padding-top: 8px;                                     " + "	list-style-type: square;                              " + "	color: #999999;                                       " + "}")
        }
    }
}


