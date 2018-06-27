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
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.appyvet.materialrangebar.RangeBar;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.SVBar;
import com.signaturemaker.app.R;
import com.signaturemaker.app.utils.FilesUtils;
import com.signaturemaker.app.utils.PermissionsUtils;
import com.signaturemaker.app.utils.Utils;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import static com.signaturemaker.app.utils.Utils.showToast;

/**
 * A placeholder fragment containing a simple view.
 */
public class SingBoardFragment extends Fragment implements View.OnClickListener, View.OnLongClickListener {

    //@BindView(R.id.singBoard)
    SignaturePad mSingBoard;

    //@BindView(R.id.FabLeft)
    FloatingActionsMenu fabLeft;
    //@BindView(R.id.FabUp)
    FloatingActionsMenu fabUp;

    //@BindView(R.id.bSave)
    FloatingActionButton bSave;
    //@BindView(R.id.bList)
    FloatingActionButton bList;
    //@BindView(R.id.bSaveSend)
    FloatingActionButton bSaveSend;
    //@BindView(R.id.bColor)
    FloatingActionButton bColor;
    //@BindView(R.id.bStroke)
    FloatingActionButton bStroke;
    //@BindView(R.id.bRubber)
    FloatingActionButton bRubber;

    View rootView;
    YoYo.YoYoString runningAnimationSeek;
    YoYo.YoYoString runningAnimationColor;
    YoYo.YoYoString runningAnimation;

    //@BindView(R.id.txtSingHere)
    TextView txtSingHere;
    //@BindView(R.id.rangeBar)
    RangeBar rangeBar;

    //@BindView(R.id.rangeSeekBarLayout)
    LinearLayout rangeSeekBarLayout;

    //@BindView(R.id.coloPickerLayout)
    LinearLayout coloPickerLayout;

    //@BindView(R.id.picker)
    ColorPicker picker;
    //@BindView(R.id.svbar)
    SVBar svbar;


    public SingBoardFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.sing_board_fragment, container, false);
        //ButterKnife.bind(this, rootView);

        mSingBoard = rootView.findViewById(R.id.singBoard);
        fabLeft = rootView.findViewById(R.id.FabLeft);
        fabUp = rootView.findViewById(R.id.FabUp);
        bSave = rootView.findViewById(R.id.bSave);
        bList = rootView.findViewById(R.id.bList);
        bSaveSend = rootView.findViewById(R.id.bSaveSend);
        bColor = rootView.findViewById(R.id.bColor);
        bStroke = rootView.findViewById(R.id.bStroke);
        bRubber = rootView.findViewById(R.id.bRubber);
        txtSingHere = rootView.findViewById(R.id.txtSingHere);
        rangeBar = rootView.findViewById(R.id.rangeBar);
        rangeSeekBarLayout = rootView.findViewById(R.id.rangeSeekBarLayout);
        coloPickerLayout = rootView.findViewById(R.id.coloPickerLayout);
        picker = rootView.findViewById(R.id.picker);
        svbar = rootView.findViewById(R.id.svbar);

        bList.setOnClickListener(this);
        bSave.setOnClickListener(this);
        bSaveSend.setOnClickListener(this);
        bColor.setOnClickListener(this);
        bStroke.setOnClickListener(this);
        bRubber.setOnClickListener(this);

        bList.setOnLongClickListener(this);
        bSave.setOnLongClickListener(this);
        bSaveSend.setOnLongClickListener(this);
        bColor.setOnLongClickListener(this);
        bStroke.setOnLongClickListener(this);
        bRubber.setOnLongClickListener(this);

        //add bar to picker
        picker.addSVBar(svbar);

        //listener floating actions buttons
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

        //listener board
        mSingBoard.setOnSignedListener(new SignaturePad.OnSignedListener() {

            @Override
            public void onStartSigning() {
                goneTxtSingHere();
                hideColorPicker();
                hideSeekbarStroke();
            }

            @Override
            public void onSigned() {
                //Event triggered when the pad is signed
            }

            @Override
            public void onClear() {
                visibleTxtSingHere();
            }
        });

        //listener rangebar
        rangeBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex,
                                              int rightPinIndex, String leftPinValue, String rightPinValue) {
                Utils.minStroke = leftPinIndex;
                Utils.maxStroke = rightPinIndex;
                mSingBoard.setMinWidth(Utils.minStroke);
                mSingBoard.setMaxWidth(Utils.maxStroke);
                Utils.saveAllPreferences(getActivity());
            }

        });

        //listerner picker
        picker.setOnColorChangedListener(new ColorPicker.OnColorChangedListener() {
            @Override
            public void onColorChanged(int i) {
                Utils.penColor=i;
                bColor.setColorNormal(Utils.penColor);
                mSingBoard.setPenColor(Utils.penColor);
                Utils.saveAllPreferences(getActivity());
            }
        });




        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        setSeekBarRange();
        setColor();
    }

    /**
     * Method for set color in picker color selector
     */
    private void setColor() {
        picker.setColor(Utils.penColor);
        picker.setOldCenterColor(Utils.penColor);
        bColor.setColorNormal(Utils.penColor);
        mSingBoard.setPenColor(Utils.penColor);
    }

    /**
     * Method for set the seekbar
     */
    private void setSeekBarRange() {
        rangeBar.setRangePinsByValue(Utils.minStroke, Utils.maxStroke);
        mSingBoard.setMinWidth(Utils.minStroke);
        mSingBoard.setMaxWidth(Utils.maxStroke);
    }


    /**
     * Show popup menu with save options
     */
    private void savePopUpOptions() {
        PopupMenu popupMenu = new PopupMenu(getContext(), bSave);
        popupMenu.getMenuInflater().inflate(R.menu.save_menu, popupMenu.getMenu());

        try {
            Field field = popupMenu.getClass().getDeclaredField("mPopup");
            field.setAccessible(true);
            Object menuPopupHelper = field.get(popupMenu);
            Class<?> cls = Class.forName("com.android.internal.view.menu.MenuPopupHelper");
            Method method = cls.getDeclaredMethod("setForceShowIcon", new Class[]{boolean.class});
            method.setAccessible(true);
            method.invoke(menuPopupHelper, new Object[]{true});
        } catch (Exception e) {
            e.printStackTrace();
        }

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final MenuItem menuItem) {

                PermissionsUtils.getInstance().callRequestPermissions(getActivity(), PermissionsUtils.permissionsReadWrite, new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        saveFiles(menuItem.getItemId());
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                    }
                });
                return false;
            }
        });

        popupMenu.show();
    }


    /**
     * Show popup menu with share options
     */
    private void sharePopUpOptions() {
        PopupMenu popupMenu = new PopupMenu(getContext(), bSave);
        popupMenu.getMenuInflater().inflate(R.menu.share_menu, popupMenu.getMenu());

        try {
            Field field = popupMenu.getClass().getDeclaredField("mPopup");
            field.setAccessible(true);
            Object menuPopupHelper = field.get(popupMenu);
            Class<?> cls = Class.forName("com.android.internal.view.menu.MenuPopupHelper");
            Method method = cls.getDeclaredMethod("setForceShowIcon", new Class[]{boolean.class});
            method.setAccessible(true);
            method.invoke(menuPopupHelper, new Object[]{true});
        } catch (Exception e) {
            e.printStackTrace();
        }

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final MenuItem menuItem) {
                PermissionsUtils.getInstance().callRequestPermissions(getActivity(), PermissionsUtils.permissionsReadWrite, new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        shareFiles(menuItem.getItemId());
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                    }
                });
                return false;
            }
        });

        popupMenu.show();
    }


    /**
     * Method for clear board sign
     */
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

    /**
     * Method for collapse the fabs buttons
     */
    private void collapseFabs() {
        fabUp.collapse();
        fabLeft.collapse();
        hideSeekbarStroke();
        hideColorPicker();
    }

    /**
     * Method for expand the fabs buttons
     */
    private void expandFabs() {
        fabUp.expand();
        fabLeft.expand();
    }

    /**
     * Method for change the visibility of textview to gone
     */
    private void goneTxtSingHere() {
        txtSingHere.setVisibility(View.GONE);
    }

    /**
     * Method for change the visibility of textview to visible
     */
    private void visibleTxtSingHere() {
        txtSingHere.setVisibility(View.VISIBLE);
    }


    /**
     * Method for hide seekbar stroke
     */
    private void hideSeekbarStroke() {
        YoYo.AnimationComposer animation = YoYo.with(Techniques.TakingOff)
                .duration(400).withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        // collapseFabs();
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        rangeSeekBarLayout.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                });
        if (runningAnimationSeek == null || !runningAnimationSeek.isRunning()) {
            runningAnimationSeek = animation.playOn(rangeSeekBarLayout);
        }
    }

    /**
     * Method for show seekbar stroke
     */
    private void showSeekbarStroke() {
        if (rangeSeekBarLayout.getVisibility() == View.VISIBLE) {
            hideSeekbarStroke();
        } else {

            YoYo.AnimationComposer animation = YoYo.with(Techniques.DropOut)
                    .duration(700).withListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            rangeSeekBarLayout.setVisibility(View.VISIBLE);
                            hideColorPicker();
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {

                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {
                        }
                    });
            if ((runningAnimationSeek == null || !runningAnimationSeek.isRunning()) && (runningAnimationColor == null || !runningAnimationColor.isRunning())) {

                runningAnimationSeek = animation.playOn(rangeSeekBarLayout);
            }
        }
    }


    /**
     * Method for hide color picker
     */
    private void hideColorPicker() {
        YoYo.AnimationComposer animation = YoYo.with(Techniques.TakingOff)
                .duration(400).withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        // collapseFabs();
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        coloPickerLayout.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                });
        if (runningAnimationColor == null || !runningAnimationColor.isRunning()) {
            runningAnimationColor = animation.playOn(coloPickerLayout);
        }
    }

    /**
     * Method for show color picker
     */
    private void showColorPicker() {
        if (coloPickerLayout.getVisibility() == View.VISIBLE) {
            hideColorPicker();
        } else {
            YoYo.AnimationComposer animation = YoYo.with(Techniques.DropOut)
                    .duration(700).withListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            coloPickerLayout.setVisibility(View.VISIBLE);
                            hideSeekbarStroke();
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {

                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {
                        }
                    });
            if ((runningAnimationSeek == null || !runningAnimationSeek.isRunning()) && (runningAnimationColor == null || !runningAnimationColor.isRunning())) {
                runningAnimationColor = animation.playOn(coloPickerLayout);

            }
        }
    }


    private void openListFilesFragment() {
        PermissionsUtils.getInstance().callRequestPermissions(getActivity(), PermissionsUtils.permissionsReadWrite, new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.container, new ListFilesFragment());
                ft.addToBackStack(SingBoardFragment.class.getSimpleName());
                ft.commit();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
            }
        });
    }


    private void saveFiles(int idMenu) {
        Boolean status = false;
        switch (idMenu) {
            case R.id.savePngTrans:
                status = FilesUtils.saveBitmapFile(mSingBoard.getTransparentSignatureBitmap(true), FilesUtils.addExtensionNamePng(FilesUtils.generateName()));
                break;
            case R.id.savePngWhite:
                status = FilesUtils.saveBitmapFile(mSingBoard.getSignatureBitmap(), FilesUtils.addExtensionNamePng(FilesUtils.generateName()));
                break;
            case R.id.saveSvg:
                status = FilesUtils.saveSvgFile(mSingBoard.getSignatureSvg(), FilesUtils.addExtensionNameSvg(FilesUtils.generateName()));
                break;
            default:
                break;
        }

        if (status) {
            showToast(getActivity(), getResources().getString(R.string.title_save_ok));
        } else {
            showToast(getActivity(), getResources().getString(R.string.title_save_ko));
        }
    }

    private void shareFiles(int idMenu) {

        saveFiles(idMenu);

        switch (idMenu) {
            case R.id.savePngTrans:

                break;
            case R.id.savePngWhite:

                break;
            case R.id.saveSvg:

                break;
            default:
                break;
        }
    }


    /**
     * Method for all click buttons
     *
     * @param view
     * @return
     */
    /*@Optional
    @OnClick({R.id.bSave, R.id.bList, R.id.bSaveSend, R.id.bColor, R.id.bStroke, R.id.bRubber})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bList:
                openListFilesFragment();
                break;
            case R.id.bSave:
                savePopUpOptions();
                break;
            case R.id.bSaveSend:
                sharePopUpOptions();
                break;
            case R.id.bColor:
                showColorPicker();
                break;
            case R.id.bStroke:
                showSeekbarStroke();
                break;
            case R.id.bRubber:
                cleanBoard();
                break;
            default:
                break;
        }
    }
*/
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.bList:
                openListFilesFragment();
                break;
            case R.id.bSave:
                savePopUpOptions();
                break;
            case R.id.bSaveSend:
                sharePopUpOptions();
                break;
            case R.id.bColor:
                showColorPicker();
                break;
            case R.id.bStroke:
                showSeekbarStroke();
                break;
            case R.id.bRubber:
                cleanBoard();
                break;
            default:
                break;
        }
    }


    /**
     * Method for all long click buttons
     *
     * @param view
     * @return
     */
/*    @Optional
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
*/
    @Override
    public boolean onLongClick(View view) {
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
}
