package com.signaturemaker.app.Nucleo;

import android.content.res.Configuration;
import android.gesture.GestureOverlayView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.SVBar;
import com.nineoldandroids.animation.Animator;
import com.signaturemaker.app.R;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import de.cketti.library.changelog.ChangeLog;

import static com.signaturemaker.app.Constantes.PreferencesCons.colorTrazo;
import static com.signaturemaker.app.Constantes.PreferencesCons.strokeTrazo;


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

        private FloatingActionsMenu fab1, fab2;
        private FloatingActionButton blimpiar, btrazo, bcolor;
        private GestureOverlayView gestos;
        private TextView txtMnsVacio;
        private DiscreteSeekBar trazo;
        private LinearLayout layoutSeek;
        private LinearLayout layoutColor;
        private View rootView;
        private ColorPicker picker;
        private SVBar svBar;


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
            bcolor = (FloatingActionButton) rootView.findViewById(R.id.bcolor);
            gestos = (GestureOverlayView) rootView.findViewById(R.id.signaturePad);
            txtMnsVacio = (TextView) rootView.findViewById(R.id.txtMnsVacio);
            trazo = (DiscreteSeekBar) rootView.findViewById(R.id.trazo);
            layoutSeek = (LinearLayout) rootView.findViewById(R.id.layoutSeek);
            layoutColor = (LinearLayout) rootView.findViewById(R.id.layoutColor);
            picker = (ColorPicker) rootView.findViewById(R.id.picker);
            svBar = (SVBar) rootView.findViewById(R.id.svbar);

            picker.addSVBar(svBar);

            cargarPref();


            gestos.addOnGestureListener(new GestureOverlayView.OnGestureListener() {
                @Override
                public void onGestureStarted(GestureOverlayView overlay, MotionEvent event) {
                    txtMnsVacio.setVisibility(View.INVISIBLE);
                    layoutSeekInv();
                    layoutColInv();
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

                    colapsarBotones();
                    clearScreen();
                }
            });


            btrazo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (layoutSeek.getVisibility() == View.VISIBLE) {
                        layoutSeekInv();

                    } else {

                        if (layoutColor.getVisibility() == View.VISIBLE)
                            layoutColInv();

                        layoutSeekVi();

                    }
                }
            });


            bcolor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (layoutColor.getVisibility() == View.VISIBLE) {
                        layoutColInv();

                    } else {

                        if (layoutSeek.getVisibility() == View.VISIBLE)
                            layoutSeekInv();

                        layoutColVi();

                    }
                }
            });


            fab1.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
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
            trazo.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
                @Override
                public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                    gestos.setGestureStrokeWidth(value);
                    gestos.invalidate();
                }

                @Override
                public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

                }
            });

            picker.setOnColorChangedListener(new ColorPicker.OnColorChangedListener() {
                @Override
                public void onColorChanged(int i) {

                    colorTrazo = i;

                }
            });

            return rootView;
        }

        public void colapsarBotones() {
            fab2.collapse();
            fab1.collapse();
            layoutSeekInv();
            layoutColInv();
        }

        public void expandirBotones() {
            fab2.expand();
            fab1.expand();
        }

        private boolean animacionSeek = false;
        private boolean animacionClear = false;
        private boolean animacionCol = false;

        public void layoutSeekInv() {

            if (!animacionSeek) {
                YoYo.with(Techniques.TakingOff)
                        .duration(400).withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                        animacionSeek = true;
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        layoutSeek.setVisibility(View.INVISIBLE);
                        animacionSeek = false;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        animacionSeek = false;
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                        animacionSeek = false;
                    }
                })
                        .playOn(rootView.findViewById(R.id.layoutSeek));
            }

        }

        public void layoutSeekVi() {
            layoutSeek.setVisibility(View.VISIBLE);
            YoYo.with(Techniques.DropOut)
                    .duration(700)
                    .playOn(rootView.findViewById(R.id.layoutSeek));

        }


        public void layoutColInv() {

            if (!animacionCol) {

                picker.setColor(colorTrazo);
                picker.setOldCenterColor(colorTrazo);
                bcolor.setColorNormal(colorTrazo);
                gestos.setGestureColor(colorTrazo);
                gestos.setUncertainGestureColor(colorTrazo);
                gestos.removeOnGesturePerformedListener(null);
                gestos.addOnGesturePerformedListener(null);
                gestos.invalidate();

                YoYo.with(Techniques.TakingOff)
                        .duration(400).withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                        animacionCol = true;
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        layoutColor.setVisibility(View.INVISIBLE);
                        animacionCol = false;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        animacionCol = false;
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                        .playOn(rootView.findViewById(R.id.layoutColor));
            }

        }

        public void layoutColVi() {
            layoutColor.setVisibility(View.VISIBLE);

            YoYo.with(Techniques.DropOut)
                    .duration(700)
                    .playOn(rootView.findViewById(R.id.layoutColor));

        }

        public void cargarPref() {
            gestos.setGestureStrokeWidth(strokeTrazo);
            trazo.setProgress(strokeTrazo);
            picker.setColor(colorTrazo);
            picker.setOldCenterColor(colorTrazo);
            bcolor.setColorNormal(colorTrazo);
            gestos.setGestureColor(colorTrazo);
            gestos.setUncertainGestureColor(colorTrazo);

            gestos.removeOnGesturePerformedListener(null);
            gestos.addOnGesturePerformedListener(null);
            gestos.invalidate();

        }


        public void clearScreen() {

            if (!animacionClear) {
                YoYo.with(Techniques.TakingOff)
                        .duration(400).withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                        animacionClear = true;
                        txtMnsVacio.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        gestos.cancelClearAnimation();
                        gestos.clear(true);
                        gestos.invalidate();

                        YoYo.with(Techniques.DropOut).duration(1).playOn(rootView.findViewById(R.id.signaturePad));
                        animacionClear = false;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        animacionClear = false;
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                        animacionClear = false;
                    }
                })
                        .playOn(rootView.findViewById(R.id.signaturePad));
            }
        }

        @Override
        public void onConfigurationChanged(Configuration newConfig) {
            super.onConfigurationChanged(newConfig);

            clearScreen();
        }


    }
}
