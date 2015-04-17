package com.signaturemaker.app.Nucleo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.gesture.GestureOverlayView;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.SVBar;
import com.nineoldandroids.animation.Animator;
import com.signaturemaker.app.Constantes.PreferencesCons;
import com.signaturemaker.app.Ficheros.Ficheros;
import com.signaturemaker.app.R;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.io.File;

import static com.signaturemaker.app.Constantes.PreferencesCons.colorTrazo;
import static com.signaturemaker.app.Constantes.PreferencesCons.strokeTrazo;

/**
 * A placeholder fragment containing a simple view.
 */
public class GestureSignature extends Fragment {

    private FloatingActionsMenu fab1, fab2;
    private FloatingActionButton blimpiar, btrazo, bcolor, bList, bSave, bSaveSend;
    private GestureOverlayView gestos;
    private TextView txtMnsVacio;
    private DiscreteSeekBar trazo;
    private LinearLayout layoutSeek;
    private LinearLayout layoutColor;
    private View rootView;
    private ColorPicker picker;
    private SVBar svBar;
    private SharedPreferences prefs;
    private Boolean prefColor, prefStroke, prefName;
    private SharedPreferences.Editor editor;


    OnListadoClickListener mItemClickListener;

    //control de animacion
    private boolean animacionSeek = false;
    private boolean animacionClear = false;
    private boolean animacionCol = false;

    public GestureSignature() {
    }

    public interface OnListadoClickListener {
        public void onItemClick(View view, String tag);
    }

    public void SetOnItemClickListener(final OnListadoClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
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

        bList = (FloatingActionButton) rootView.findViewById(R.id.bList);
        bSave = (FloatingActionButton) rootView.findViewById(R.id.bSave);
        bSaveSend = (FloatingActionButton) rootView.findViewById(R.id.bSaveSend);

        picker.addSVBar(svBar);

        cargarPref();


        blimpiar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getActivity(), getResources().getString(R.string.limpiar), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        btrazo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getActivity(), getResources().getString(R.string.stroke), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        bcolor.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getActivity(), getResources().getString(R.string.selectcolor), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        bList.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getActivity(), getResources().getString(R.string.listado), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        bSave.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getActivity(), getResources().getString(R.string.guardar), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        bSaveSend.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getActivity(), getResources().getString(R.string.enviar), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        //salvar firma
        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                String name = Ficheros.generaNombre();
                if (prefName) {
                    MaterialDialog dia = new MaterialDialog.Builder(getActivity())
                            .customView(R.layout.name, false)
                            .neutralText(R.string.limpiar)
                            .positiveText(android.R.string.ok)
                            .autoDismiss(false)
                            .title(R.string.selectname)
                            .negativeText(android.R.string.cancel)
                            .show();

                    final TextView nametext = (TextView) dia.getCustomView().findViewById(R.id.nametext);
                    nametext.setText(name);

                    dia.getBuilder().callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            super.onPositive(dialog);
                            String nameFinal = Ficheros.limpiaName(nametext.getText().toString());
                            if (nameFinal.length() == 0) {
                                Toast.makeText(getActivity(), getResources().getString(R.string.trimname), Toast.LENGTH_SHORT).show();
                            } else {
                                dialog.dismiss();
                                SalvaImagenShare(gestos, false, Ficheros.addEXTNombre(nameFinal));
                                if (mItemClickListener != null) {
                                    mItemClickListener.onItemClick(v, "SAVE");
                                }
                            }
                        }

                        @Override
                        public void onNeutral(MaterialDialog dialog) {
                            super.onNeutral(dialog);
                            nametext.setText("");

                        }

                        @Override
                        public void onNegative(MaterialDialog dialog) {
                            super.onNegative(dialog);
                            dialog.dismiss();
                        }
                    });
                } else {
                    SalvaImagenShare(gestos, false, Ficheros.addEXTNombre(name));
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClick(v, "SAVE");
                    }
                }
            }
        });

        //salvar firma y enviar
        bSaveSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                String name = Ficheros.generaNombre();
                if (prefName) {
                    MaterialDialog dia = new MaterialDialog.Builder(getActivity())
                            .customView(R.layout.name, false)
                            .neutralText(R.string.limpiar)
                            .positiveText(android.R.string.ok)
                            .autoDismiss(false)
                            .title(R.string.selectname)
                            .negativeText(android.R.string.cancel)
                            .show();

                    final TextView nametext = (TextView) dia.getCustomView().findViewById(R.id.nametext);
                    nametext.setText(name);

                    dia.getBuilder().callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            super.onPositive(dialog);
                            String nameFinal = Ficheros.limpiaName(nametext.getText().toString());
                            if (nameFinal.length() == 0) {
                                Toast.makeText(getActivity(), getResources().getString(R.string.trimname), Toast.LENGTH_SHORT).show();
                            } else {
                                dialog.dismiss();
                                SalvaImagenShare(gestos, true, Ficheros.addEXTNombre(nameFinal));
                                if (mItemClickListener != null) {
                                    mItemClickListener.onItemClick(v, "SAVE");
                                }
                            }
                        }

                        @Override
                        public void onNeutral(MaterialDialog dialog) {
                            super.onNeutral(dialog);
                            nametext.setText("");

                        }

                        @Override
                        public void onNegative(MaterialDialog dialog) {
                            super.onNegative(dialog);
                            dialog.dismiss();
                        }
                    });
                } else {
                    SalvaImagenShare(gestos, false, Ficheros.addEXTNombre(name));
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClick(v, "SAVE");
                    }
                }
            }
        });

        bList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(v, "LISTADO");
                }
            }
        });


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

        //boton principal
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
                strokeTrazo = value;
                gestos.setGestureStrokeWidth(strokeTrazo);
                gestos.invalidate();
                if (prefStroke) {
                    editor.putInt(PreferencesCons.STROKE, strokeTrazo);
                    editor.commit();
                }
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
                if (prefColor) {

                    editor.putInt(PreferencesCons.COLOR, colorTrazo);
                    editor.commit();
                }

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


    //Hace invisible el selector de grosor
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

    //Hace visible el selector de grosor
    public void layoutSeekVi() {
        layoutSeek.setVisibility(View.VISIBLE);
        YoYo.with(Techniques.DropOut)
                .duration(700)
                .playOn(rootView.findViewById(R.id.layoutSeek));

    }

    //Hace invisible el selector de color
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

    //Hace visible el selector de color
    public void layoutColVi() {
        layoutColor.setVisibility(View.VISIBLE);

        YoYo.with(Techniques.DropOut)
                .duration(700)
                .playOn(rootView.findViewById(R.id.layoutColor));

    }

    //Carga los valores inicales en el lapiz como el color y el grosor.
    public void cargarPref() {

        recuperaPref();

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

    //Limpia la firma de la pantalla con un efecto
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


    public void SalvaImagenShare(GestureOverlayView gestos, Boolean flagShare, String name) {

        gestos.setDrawingCacheEnabled(true);
        Bitmap bm = Bitmap.createBitmap(gestos.getDrawingCache());

        if (Ficheros.guardar(bm, name)) {
            Toast.makeText(getActivity(), R.string.guardado, Toast.LENGTH_SHORT).show();
            if (flagShare)
                Ficheros.sendFirma(getActivity(), name);

            getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(PreferencesCons.pathFiles + "/" + name))));


            /*if (findViewById(R.id.fragment1) != null)
                Fragment.cargaViews();*/

        } else
            Toast.makeText(getActivity(), R.string.noguardado, Toast.LENGTH_SHORT).show();

        gestos.destroyDrawingCache();

    }

    public void recuperaPref() {

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = prefs.edit();
        prefColor = prefs.getBoolean(PreferencesCons.OP4, false);
        prefStroke = prefs.getBoolean(PreferencesCons.OP5, false);
        prefName = prefs.getBoolean(PreferencesCons.OP7, false);

        //
        if (prefStroke)
            strokeTrazo = prefs.getInt(PreferencesCons.STROKE, strokeTrazo);
        if (prefColor)
            colorTrazo = prefs.getInt(PreferencesCons.COLOR, colorTrazo);
    }


    @Override
    public void onResume() {
        super.onResume();
        recuperaPref();
    }

    //Cuando se gira la pantalla.
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // colapsarBotones();
        clearScreen();
    }


}