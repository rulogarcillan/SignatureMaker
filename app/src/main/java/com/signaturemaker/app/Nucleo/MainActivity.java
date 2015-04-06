package com.signaturemaker.app.Nucleo;

import android.os.Bundle;
import android.view.View;

import com.signaturemaker.app.R;

import de.cketti.library.changelog.ChangeLog;


public class MainActivity extends BaseActivity {

    GestureSignature fgestos = new GestureSignature();
    ListadoFiles fListado = new ListadoFiles();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ChangeLog cl = new ChangeLog(this);
        if (cl.isFirstRun()) {
            new BaseActivity.LanzaChangelog(this).getLogDialog().show();
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fgestos, "GESTOS")

                    .commit();
        }
        fgestos.SetOnItemClickListener(new GestureSignature.OnListadoClickListener() {
            @Override
            public void onItemClick(View view) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fListado, "LISTADO")
                        .addToBackStack("LISTADO")
                        .commit();
            }
        });

    }

}
