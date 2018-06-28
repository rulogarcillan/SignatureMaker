package com.signaturemaker.app.activities;

import android.content.Intent;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.signaturemaker.app.R;
import com.signaturemaker.app.utils.Utils;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class IntroActivity extends AppIntro2 {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Utils.loadPreference(this, "loadIntro", true)) {
            //Utils.savePreference(this, "loadIntro", false);
        } else {
            launchActivityMain();
        }

        Fragment f1 = AppIntroFragment.newInstance("Firma", "Roboto", "Digitalice su firma de una forma fácil y sencilla", "normal",
                R.drawable.ic_pencil_icon, getResources().getColor(R.color.background_sliders), getResources().getColor(R.color.colorWhite), getResources().getColor(R.color.colorWhite));
        Fragment f2 = AppIntroFragment.newInstance("Transfiere", "Roboto", "Guarde y envíe su firma donde usted quiera", "normal",
                R.drawable.ic_share_icon, getResources().getColor(R.color.background_sliders), getResources().getColor(R.color.colorWhite), getResources().getColor(R.color.colorWhite));
        Fragment f3 = AppIntroFragment.newInstance("Adjunta", "Roboto", "Adjunte la imagen con la firma en su documento", "normal",
                R.drawable.ic_sign_icon, getResources().getColor(R.color.background_sliders), getResources().getColor(R.color.colorWhite), getResources().getColor(R.color.colorWhite));
        Fragment f4 = AppIntroFragment.newInstance("Listo", "Roboto", "¿Estás preparado?", "normal",
                R.drawable.ic_heart_icon, getResources().getColor(R.color.background_sliders), getResources().getColor(R.color.colorWhite), getResources().getColor(R.color.colorWhite));
        addSlide(f1);
        addSlide(f2);
        addSlide(f3);
        addSlide(f4);

        // OPTIONAL METHODS
        // Override bar/separator color.
        //setBarColor(Color.parseColor("#3F51B5"));


        // Hide Skip/Done button.
        showSkipButton(false);
        setProgressButtonEnabled(true);

        // Turn vibration on and set intensity.
        // NOTE: you will probably need to ask VIBRATE permission in Manifest.
        setVibrate(true);
        setVibrateIntensity(30);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);

        //launchActivityMain();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);

    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }


    private void launchActivityMain() {
        Intent myIntent = new Intent(this, MainActivity.class);
        this.startActivity(myIntent);
        finish();
    }
}