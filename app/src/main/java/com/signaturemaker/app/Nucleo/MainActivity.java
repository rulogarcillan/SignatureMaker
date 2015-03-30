package com.signaturemaker.app.Nucleo;

import android.gesture.GestureOverlayView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.nineoldandroids.animation.Animator;
import com.signaturemaker.app.R;

import de.cketti.library.changelog.ChangeLog;


public class MainActivity extends BaseActivity {

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
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }



    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        FloatingActionsMenu fab1, fab2;
        FloatingActionButton blimpiar, btrazo;
        GestureOverlayView gestos ;
        TextView txtMnsVacio;
        private SeekBar trazo;
        private LinearLayout layoutSeek;
        View rootView;
        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.fragment_sign, container, false);
            fab1 = (FloatingActionsMenu) rootView.findViewById(R.id.Fab1);
            fab2 = (FloatingActionsMenu) rootView.findViewById(R.id.Fab2);
            blimpiar = (FloatingActionButton) rootView.findViewById(R.id.blimpiar);
            btrazo = (FloatingActionButton) rootView.findViewById(R.id.btrazo);
            gestos = (GestureOverlayView) rootView.findViewById(R.id.signaturePad);
            txtMnsVacio = (TextView) rootView.findViewById(R.id.txtMnsVacio);
            trazo = (SeekBar) rootView.findViewById(R.id.trazo);
            layoutSeek = (LinearLayout) rootView.findViewById(R.id.layoutSeek);






            gestos.setGestureStrokeWidth(3);

            gestos.addOnGestureListener(new GestureOverlayView.OnGestureListener() {
                @Override
                public void onGestureStarted(GestureOverlayView overlay, MotionEvent event) {
                    txtMnsVacio.setVisibility(View.INVISIBLE);
                    layoutSeekInv();
                }

                @Override
                public void onGesture(GestureOverlayView overlay, MotionEvent event) {

                }

                @Override
                public void onGestureEnded(GestureOverlayView overlay, MotionEvent event) {

                }

                @Override
                public void onGestureCancelled(GestureOverlayView overlay, MotionEvent event) {

                }

            });

            blimpiar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    gestos.cancelClearAnimation();
                    gestos.clear(true);
                    gestos.invalidate();
                    colapsarBotones();
                    txtMnsVacio.setVisibility(View.VISIBLE);
                }
            });


            btrazo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (layoutSeek.getVisibility() == View.VISIBLE){
                        layoutSeekInv();

                    }else{
                        layoutSeekVi();

                }
            }});

            fab1.setOnFloatingActionsMenuUpdateListener( new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
                @Override
                public void onMenuExpanded() {

                    expandirBotones();

                }

                @Override
                public void onMenuCollapsed() {
                    colapsarBotones();
                }
            });

            // cambio de trazo
            trazo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    // TODO Auto-generated method stub

                    gestos.setGestureStrokeWidth(progress);
                    gestos.invalidate();

                }
            });



            return rootView;
        }

        public void colapsarBotones(){
            fab2.collapse();
            fab1.collapse();
            layoutSeekInv();
        }

        public void expandirBotones(){
            fab2.expand();
            fab1.expand();
        }

        private boolean animacion=false;
        public void layoutSeekInv(){

            if (!animacion) {
                YoYo.with(Techniques.TakingOff)
                        .duration(400).withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                        animacion = true;
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        layoutSeek.setVisibility(View.INVISIBLE);
                        animacion=false;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        animacion=false;
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                        .playOn(rootView.findViewById(R.id.layoutSeek));
            }

        }

        public void layoutSeekVi(){
            layoutSeek.setVisibility(View.VISIBLE);
            YoYo.with(Techniques.DropOut)
                    .duration(700)
                    .playOn(rootView.findViewById(R.id.layoutSeek));

        }
    }
}
