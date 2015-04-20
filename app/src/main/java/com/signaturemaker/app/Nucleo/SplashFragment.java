package com.signaturemaker.app.Nucleo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.signaturemaker.app.R;

public class SplashFragment extends Fragment {


    private int pos;
    ImageView image;
    Button boton;
    TextView text, textUrl;
    Animation animation;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onResume() {
        super.onResume();
        animation.cancel();
        if (pos == 4) {
            animation.setDuration(1000);
            animation.start();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        animation = AnimationUtils.loadAnimation(getActivity(), R.anim.abc_fade_in);
        View rootView = inflater.inflate(R.layout.splash_fragment, container, false);
        text = (TextView) rootView.findViewById(R.id.textplash);

        if (pos == 4) {
            rootView = inflater.inflate(R.layout.splash_fragment2, container, false);
            boton = (Button) rootView.findViewById(R.id.textplash);
            textUrl = (TextView) rootView.findViewById(R.id.url);
        }

        image = (ImageView) rootView.findViewById(R.id.imageSplash);


        switch (pos) {
            case 1:
                image.setImageResource(R.drawable.frame11);
                //  Picasso.with(getActivity()).load(R.drawable.splash1).into(image);

                text.setText(R.string.splash1);
                break;
            case 2:
                image.setImageResource(R.drawable.frame22);
                text.setText(R.string.splash2);
                break;
            case 3:
                image.setImageResource(R.drawable.frame33);
                text.setText(R.string.splash3);
                break;
            case 4:


                boton.startAnimation(animation);
                textUrl.setText(Html.fromHtml(getResources().getString(R.string.url1) + "<br>" + " " + "<font color='#01579b'><a href=\"https://github.com/rulogarcillan/SignatureMaker\">" + getResources().getString(R.string.url2) + "</a></font>"));

                image.setImageResource(R.drawable.frame44);
                boton.setText(R.string.start);


                textUrl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = getResources().getString(R.string.url2);
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                });


                boton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean("first_time", false);
                        editor.commit();
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        getActivity().startActivity(intent);
                        getActivity().finish();
                        getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                    }
                });
                break;
            default:
                break;
        }

        return rootView;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }


}