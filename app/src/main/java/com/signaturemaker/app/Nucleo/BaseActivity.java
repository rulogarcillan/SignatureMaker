package com.signaturemaker.app.Nucleo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;

import com.mikepenz.aboutlibraries.LibsBuilder;
import com.signaturemaker.app.R;

import de.cketti.library.changelog.ChangeLog;


/**
 * Created by raul.rodriguezconcep on 20/02/15.
 */
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

                Intent intent = new Intent(this, PreferencesAct.class);
                startActivity(intent);
                break;
            case R.id.action_settings:
                break;
            case R.id.puntuar:

                LanzaRate();

                break;

            case R.id.more:

                LanzaMore();

                break;
            case R.id.changelog:

                new LanzaChangelog(BaseActivity.this).getFullLogDialog().show();
                break;
            case R.id.license:
                //Create an intent with context and the Activity class

                new LibsBuilder()
                        //Pass the fields of your application to the lib so it can find all external lib information
                        .withFields(R.string.class.getFields())
                        .withVersionShown(true)
                        .withLicenseShown(true)
                        .withAutoDetect(true)
                        .withLibraries("CircleIndicator")
                        .withActivityTitle(getResources().getString(R.string.license))
                        .withAboutDescription(getResources().getString(R.string.escrita) + "<br/><br/><b>License GNU GPL V3.0</b><br/><br/><a href=\"https://github.com/rulogarcillan/SignatureMaker\">Project in Github</a>")
                        .withActivityTheme(R.style.AppTheme)
                                //start the activity
                        .start(this);
                break;


            default:
                onBackPressed();
        }

        return true;
    }

    private void LanzaRate() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=com.signaturemaker.app"));
        startActivity(intent);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }


    private void LanzaMore() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:Raúl R."));

        startActivity(intent);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }


    public static class LanzaChangelog extends ChangeLog {


        public static final String DEFAULT_CSS =

                "body {                                                           " + "	font-family: Verdana, Helvetica, Arial, sans-serif;   " + "	font-size: 11px;                                      " + "	color: #000000;                                       " + "	background-color: #ffffff;                            " + "	margin: 0px;                                          " + "	padding: 0px;                                         " + "}                                                        "
                        + "h1 {                                                     " + "	font-size: 14px;                                      " + "	font-weight: bold;                                    " + "	text-transform: uppercase;                            " + "	color: #000000;                                       " + "	margin: 0px;                                          " + "	padding: 10px 0px 0px 8px;                            " + "}                                                        "
                        + "h2 {                                                     " + "	font-size: 10px;                                      " + "	color: #999999;                                       " + "	font-weight: normal;                                  " + "	margin: 0px 0px 0px 8px;                              " + "	padding: 0px;                                         " + "}                                                        " + "ul {                                                     "
                        + "	margin: 0px 0px 10px 15px;                            " + "	padding-left: 15px;                                " + "	padding-top: 8px;                                     " + "	list-style-type: square;                              " + "	color: #999999;                                       " + "}";

        public LanzaChangelog(Context context) {
            super(new ContextThemeWrapper(context, R.style.AppTheme), DEFAULT_CSS);
        }
    }

}



