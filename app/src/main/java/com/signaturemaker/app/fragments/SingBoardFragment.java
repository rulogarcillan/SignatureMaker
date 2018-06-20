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
package com.signaturemaker.app.fragments;

import android.animation.Animator;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.signaturemaker.app.R;
import com.signaturemaker.app.comun.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import butterknife.Optional;

import static com.signaturemaker.app.comun.Constants.TAG;
import static com.signaturemaker.app.comun.Utils.showToast;

/**
 * A placeholder fragment containing a simple view.
 */
public class SingBoardFragment extends Fragment {


    @BindView(R.id.singBoard)
    SignaturePad mSingBoard;

    @BindView(R.id.FabLeft)
    FloatingActionsMenu fabLeft;
    @BindView(R.id.FabUp)
    FloatingActionsMenu fabUp;

    @BindView(R.id.bSave)
    FloatingActionButton bSave;
    @BindView(R.id.bList)
    FloatingActionButton bList;
    @BindView(R.id.bSaveSend)
    FloatingActionButton bSaveSend;
    @BindView(R.id.bColor)
    FloatingActionButton bColor;
    @BindView(R.id.bStroke)
    FloatingActionButton bStroke;
    @BindView(R.id.bRubber)
    FloatingActionButton bRubber;

    View rootView;
    YoYo.YoYoString runningAnimation;

    public SingBoardFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.sing_board_fragment, container, false);
        ButterKnife.bind(this, rootView);

        fabUp.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                expandFabs();
            }

            @Override
            public void onMenuCollapsed() {
                collapseFabs();
            }
        });
        // mSignaturePad = (SignaturePad) rootView.findViewById(R.id.signature_pad);
        // mSignaturePad.setVelocityFilterWeight(10);
        // mSignaturePad.setMaxWidth(1);

        mSingBoard.setMaxWidth(4);
        mSingBoard.setMinWidth(1);

        mSingBoard.setOnSignedListener(new SignaturePad.OnSignedListener() {

            @Override
            public void onStartSigning() {
                //Event triggered when the pad is touched
            }

            @Override
            public void onSigned() {
                //Event triggered when the pad is signed
            }

            @Override
            public void onClear() {
                //Event triggered when the pad is cleared
            }
        });
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    @Optional
    @OnLongClick({R.id.bSave, R.id.bList, R.id.bSaveSend, R.id.bColor, R.id.bStroke, R.id.bRubber})
    public boolean onLongClick(View view) {
        // since > api 26 insert tooltip in layout.xml
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            switch (view.getId()) {
                case R.id.bList:
                    showToast(getContext(), getString(R.string.title_bList));
                    break;
                case R.id.bSave:
                    showToast(getContext(), getString(R.string.title_bSave));
                    break;
                case R.id.bSaveSend:
                    showToast(getContext(), getString(R.string.title_bSaveSend));
                    break;
                case R.id.bColor:
                    showToast(getContext(), getString(R.string.title_bColor));
                    break;
                case R.id.bStroke:
                    showToast(getContext(), getString(R.string.title_bStroke));
                    break;
                case R.id.bRubber:
                    showToast(getContext(), getString(R.string.title_bRubber));
                    break;
                default:
                    break;
            }
        }
        return false;
    }


    @Optional
    @OnClick({R.id.bSave, R.id.bList, R.id.bSaveSend, R.id.bColor, R.id.bStroke, R.id.bRubber})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bList:
                break;
            case R.id.bSave:

                break;
            case R.id.bSaveSend:

                break;
            case R.id.bColor:

                break;
            case R.id.bStroke:

                break;
            case R.id.bRubber:
                cleanBoard();
                break;
            default:
                break;
        }
    }

    private void cleanBoard() {


        YoYo.AnimationComposer animation = YoYo.with(Techniques.TakingOff)
                .duration(400).withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        collapseFabs();
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                        mSingBoard.clear();
                        mSingBoard.setAlpha(1);
                        mSingBoard.setScaleX(1);
                        mSingBoard.setScaleY(1);
                        mSingBoard.setTranslationX(0);
                        mSingBoard.setTranslationY(0);
                        mSingBoard.setRotation(0);
                        mSingBoard.setRotationY(0);
                        mSingBoard.setRotationX(0);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
        if (runningAnimation == null || !runningAnimation.isRunning()) {
            runningAnimation = animation.playOn(mSingBoard);
        }
    }

    private void collapseFabs() {
        fabUp.collapse();
        fabLeft.collapse();
    }

    private void expandFabs() {
        fabUp.expand();
        fabLeft.expand();
    }
}
