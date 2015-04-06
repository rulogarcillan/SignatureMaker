package com.signaturemaker.app.Nucleo;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.signaturemaker.app.R;


public class PreferencesAct extends PreferenceActivity {
    private Toolbar mToolBar;
    private FrameLayout container;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prepareLayout();

    }

    private void prepareLayout() {
        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        View content = root.getChildAt(0);
        LinearLayout toolbarContainer = (LinearLayout) View.inflate(this, R.layout.preferences, null);

        root.removeAllViews();
        toolbarContainer.addView(content);
        root.addView(toolbarContainer);


        mToolBar = (Toolbar) toolbarContainer.findViewById(R.id.toolbar);
        container = (FrameLayout) toolbarContainer.findViewById(R.id.container);


        getFragmentManager().beginTransaction()
                .replace(container.getId(), new PreferenceFrag())
                .commit();

        mToolBar.setTitle(R.string.settings);
        mToolBar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}