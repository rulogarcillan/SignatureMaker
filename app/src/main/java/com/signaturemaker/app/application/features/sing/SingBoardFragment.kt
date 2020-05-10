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
package com.signaturemaker.app.application.features.sing

import android.animation.Animator
import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.appyvet.materialrangebar.RangeBar
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.getbase.floatingactionbutton.FloatingActionButton
import com.getbase.floatingactionbutton.FloatingActionsMenu
import com.github.gcacace.signaturepad.views.SignaturePad
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.larswerkman.holocolorpicker.ColorPicker
import com.larswerkman.holocolorpicker.SVBar
import com.signaturemaker.app.R
import com.signaturemaker.app.application.core.extensions.Utils
import com.signaturemaker.app.application.core.extensions.gone
import com.signaturemaker.app.application.core.extensions.showToast
import com.signaturemaker.app.application.core.extensions.visible
import com.signaturemaker.app.application.core.platform.FilesUtils
import com.signaturemaker.app.application.core.platform.PermissionsUtils
import com.signaturemaker.app.application.features.files.ClickInterface
import com.signaturemaker.app.application.features.files.ListFilesFragment
import com.signaturemaker.app.application.features.main.MainActivity


import java.io.File

/**
 * A placeholder fragment containing a simple view.
 */
class SingBoardFragment : Fragment(), View.OnClickListener, View.OnLongClickListener {

    private var clickInterface: ClickInterface? = null
    private var runningAnimationSeek: YoYo.YoYoString? = null
    private var runningAnimationColor: YoYo.YoYoString? = null
    private var runningAnimation: YoYo.YoYoString? = null
    private lateinit var singBoard: SignaturePad

    private lateinit var bColor: FloatingActionButton
    private lateinit var bList: FloatingActionButton
    private lateinit var bRubber: FloatingActionButton
    private lateinit var bSave: FloatingActionButton
    private lateinit var bSaveSend: FloatingActionButton
    private lateinit var bStroke: FloatingActionButton
    private lateinit var bWallpaper: FloatingActionButton
    private lateinit var fabLeft: FloatingActionsMenu
    private lateinit var fabUp: FloatingActionsMenu

    private lateinit var coloPickerLayout: LinearLayout
    private lateinit var picker: ColorPicker
    private lateinit var svbar: SVBar

    private lateinit var layoutWallapaper: ConstraintLayout
    private lateinit var txtSingHere: TextView

    private lateinit var rangeBar: RangeBar
    private lateinit var rangeSeekBarLayout: LinearLayout

    companion object {
        fun newInstance(): SingBoardFragment = SingBoardFragment()
    }

    private interface TextDialog {

        fun onGetTextDialog(name: String)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.sing_board_fragment, container, false)
        singBoard = view.findViewById(R.id.singBoard)
        singBoard.isSaveEnabled = false

        bColor = view.findViewById(R.id.bColor)
        bList = view.findViewById(R.id.bList)
        bRubber = view.findViewById(R.id.bRubber)
        bSave = view.findViewById(R.id.bSave)
        bSaveSend = view.findViewById(R.id.bSaveSend)
        bStroke = view.findViewById(R.id.bStroke)
        bWallpaper = view.findViewById(R.id.bWallpaper)
        fabLeft = view.findViewById(R.id.fabLeft)
        fabUp = view.findViewById(R.id.fabUp)

        coloPickerLayout = view.findViewById(R.id.coloPickerLayout)
        picker = view.findViewById(R.id.picker)
        svbar = view.findViewById(R.id.svbar)

        layoutWallapaper = view.findViewById(R.id.layoutWallapaper)
        txtSingHere = view.findViewById(R.id.txtSingHere)

        rangeBar = view.findViewById(R.id.rangeBar)
        rangeSeekBarLayout = view.findViewById(R.id.rangeSeekBarLayout)


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showBackButton()
        bList?.let {
            it.setOnClickListener(this)
            it.setOnLongClickListener(this)
        }

        bSave.setOnClickListener(this)
        bSaveSend.setOnClickListener(this)
        bColor.setOnClickListener(this)
        bStroke.setOnClickListener(this)
        bRubber.setOnClickListener(this)
        bWallpaper.setOnClickListener(this)

        bSave.setOnLongClickListener(this)
        bSaveSend.setOnLongClickListener(this)
        bColor.setOnLongClickListener(this)
        bStroke.setOnLongClickListener(this)
        bRubber.setOnLongClickListener(this)

        //add bar to picker
        picker.addSVBar(svbar)

        //listener floating actions buttons
        fabUp.setOnFloatingActionsMenuUpdateListener(object :
            FloatingActionsMenu.OnFloatingActionsMenuUpdateListener {
            override fun onMenuCollapsed() {
                collapseFabs()
            }

            override fun onMenuExpanded() {
                expandFabs()
            }
        })

        //listener board
        singBoard.setOnSignedListener(object : SignaturePad.OnSignedListener {

            override fun onClear() {
                txtSingHere?.visible()
            }

            override fun onSigned() {
                //Event triggered when the pad is signed
            }

            override fun onStartSigning() {
                txtSingHere?.gone()
                hideColorPicker()
                hideSeekbarStroke()
            }
        })

        //listener rangebar
        rangeBar.setOnRangeBarChangeListener { rangeBar, leftPinIndex, rightPinIndex, leftPinValue, rightPinValue ->
            Utils.minStroke = leftPinIndex
            Utils.maxStroke = rightPinIndex
            singBoard.setMinWidth(Utils.minStroke.toFloat())
            singBoard.setMaxWidth(Utils.maxStroke.toFloat())
            Utils.saveAllPreferences(context)
        }

        //listerner picker
        picker.onColorChangedListener = ColorPicker.OnColorChangedListener { i ->
            Utils.penColor = i
            bColor.colorNormal = Utils.penColor
            singBoard.setPenColor(Utils.penColor)
            Utils.saveAllPreferences(context)
        }
    }

    override fun onResume() {
        super.onResume()
        setSeekBarRange()
        setColor()
        selectWallpaper()
    }

    override fun onClick(view: View) {

        when (view.id) {
            R.id.bList -> openListFilesFragment()
            R.id.bSave -> savePopUpOptions()
            R.id.bSaveSend -> sharePopUpOptions()
            R.id.bColor -> showColorPicker()
            R.id.bStroke -> showSeekbarStroke()
            R.id.bRubber -> cleanBoard()
            R.id.bWallpaper -> changeWallpaper()
        }
    }

    override fun onLongClick(view: View): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            when (view.id) {
                R.id.bList -> context?.showToast(getString(R.string.title_bList))
                R.id.bSave -> context?.showToast(getString(R.string.title_bSave))
                R.id.bSaveSend -> context?.showToast(getString(R.string.title_bSaveSend))
                R.id.bColor -> context?.showToast(getString(R.string.title_bColor))
                R.id.bStroke -> context?.showToast(getString(R.string.title_bStroke))
                R.id.bRubber -> context?.showToast(getString(R.string.title_bRubber))
                else -> {
                }
            }
        }
        return false
    }

    fun setInterface(clickInterface: ClickInterface) {
        this.clickInterface = clickInterface
    }

    private fun changeWallpaper() {
        Utils.wallpaper++
        if (Utils.wallpaper == 5) {
            Utils.wallpaper = 1
        }
        Utils.saveAllPreferences(activity)
        selectWallpaper()
    }

    fun showBackButton() {
        activity?.let { mActivity ->
            if (mActivity is MainActivity) {
                mActivity.supportActionBar?.setDisplayHomeAsUpEnabled(false)
            }
        }
    }

    /**
     * Method for clear board sign
     */
    private fun cleanBoard() {

        val animation = YoYo.with(Techniques.TakingOff)
            .duration(400).withListener(object : Animator.AnimatorListener {
                override fun onAnimationCancel(animation: Animator) {}

                override fun onAnimationEnd(animation: Animator) {
                    singBoard.apply {
                        clear()
                        alpha = 1f
                        scaleX = 1f
                        scaleY = 1f
                        translationX = 0f
                        translationY = 0f
                        rotation = 0f
                        rotationY = 0f
                        rotationX = 0f
                    }
                }

                override fun onAnimationRepeat(animation: Animator) {
                }

                override fun onAnimationStart(animation: Animator) {
                    collapseFabs()
                }
            })

        if (runningAnimation == null || runningAnimation?.isRunning == false) {
            runningAnimation = animation.playOn(singBoard)
        }
    }

    /**
     * Method for collapse the fabs buttons
     */
    private fun collapseFabs() {
        fabUp.collapse()
        fabLeft.collapse()
        hideSeekbarStroke()
        hideColorPicker()
    }

    /**
     * Method for expand the fabs buttons
     */
    private fun expandFabs() {
        fabUp.expand()
        fabLeft.expand()
    }

    /**
     * Method for hide color picker
     */
    private fun hideColorPicker() {
        val animation = YoYo.with(Techniques.TakingOff)
            .duration(400).withListener(object : Animator.AnimatorListener {
                override fun onAnimationCancel(animation: Animator) {}

                override fun onAnimationEnd(animation: Animator) {
                    coloPickerLayout.visibility = View.INVISIBLE
                }

                override fun onAnimationRepeat(animation: Animator) {}

                override fun onAnimationStart(animation: Animator) {
                    // collapseFabs();
                }
            })
        if (runningAnimationColor == null || runningAnimationColor?.isRunning == false) {
            runningAnimationColor = animation.playOn(coloPickerLayout)
        }
    }

    /**
     * Method for hide seekbar stroke
     */
    private fun hideSeekbarStroke() {
        val animation = YoYo.with(Techniques.TakingOff)
            .duration(400).withListener(object : Animator.AnimatorListener {
                override fun onAnimationCancel(animation: Animator) {}

                override fun onAnimationEnd(animation: Animator) {
                    rangeSeekBarLayout.visibility = View.INVISIBLE
                }

                override fun onAnimationRepeat(animation: Animator) {}

                override fun onAnimationStart(animation: Animator) {
                    // collapseFabs();
                }
            })
        if (runningAnimationSeek == null || runningAnimationSeek?.isRunning == false) {
            runningAnimationSeek = animation.playOn(rangeSeekBarLayout)
        }
    }

    private fun openListFilesFragment() {

        PermissionsUtils.instance?.callRequestPermissionWrite(activity as Activity, object : PermissionListener {
            override fun onPermissionDenied(response: PermissionDeniedResponse) {}

            override fun onPermissionGranted(response: PermissionGrantedResponse) {
                val ft = fragmentManager?.beginTransaction()
                ft?.replace(R.id.container, ListFilesFragment())
                ft?.addToBackStack(SingBoardFragment::class.java.simpleName)
                ft?.commit()
            }

            override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest, token: PermissionToken) {}
        })
    }

    private fun saveFileAndSend(idMenu: Int, share: Boolean) {
        if (Utils.nameSave) {
            selectName(object : TextDialog {
                var statusParticular: Boolean = false
                override fun onGetTextDialog(name: String) {
                    var mName = ""
                    when (idMenu) {
                        R.id.savePngTrans -> {
                            mName = FilesUtils.addExtensionNamePng(name)
                            statusParticular = FilesUtils
                                .saveBitmapFile(singBoard.getTransparentSignatureBitmap(true), mName)
                        }
                        R.id.savePngWhite -> {
                            mName = FilesUtils.addExtensionNamePng(name)
                            statusParticular = FilesUtils.saveBitmapFile(singBoard.signatureBitmap, mName)
                        }
                        R.id.saveSvg -> {
                            mName = FilesUtils.addExtensionNameSvg(name)
                            statusParticular = FilesUtils.saveSvgFile(singBoard.signatureSvg, mName)
                        }
                    }
                    if (statusParticular) {
                        if (share) {
                            activity?.let {
                                Utils.shareSign(it, mName)
                            }
                        }
                        context?.showToast(resources.getString(R.string.title_save_ok))
                        activity?.sendBroadcast(
                            Intent(
                                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                                Uri.fromFile(File(Utils.path + mName))
                            )
                        )
                        clickInterface?.buttonClicked()
                    } else {
                        context?.showToast(resources.getString(R.string.title_save_ko))
                    }
                }
            })
        } else {
            var status: Boolean = false
            var name = ""
            when (idMenu) {
                R.id.savePngTrans -> {
                    name = FilesUtils.addExtensionNamePng(FilesUtils.generateName())
                    val sing = singBoard.getTransparentSignatureBitmap(true)
                    sing?.let {
                        status = FilesUtils.saveBitmapFile(it, name)
                    }
                }
                R.id.savePngWhite -> {
                    name = FilesUtils.addExtensionNamePng(FilesUtils.generateName())
                    val sing: Bitmap? = singBoard.signatureBitmap
                    sing?.let {
                        status = FilesUtils.saveBitmapFile(it, name)
                    }
                }
                R.id.saveSvg -> {
                    name = FilesUtils.addExtensionNameSvg(FilesUtils.generateName())
                    status = FilesUtils.saveSvgFile(singBoard.signatureSvg, name)
                }
            }
            if (status) {
                if (share) {
                    activity?.let {
                        Utils.shareSign(it, name)
                    }
                }
                context?.showToast(resources.getString(R.string.title_save_ok))
                activity?.sendBroadcast(
                    Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(File(Utils.path + name)))
                )
                clickInterface?.buttonClicked()
            } else {
                context?.showToast(resources.getString(R.string.title_save_ko))
            }
        }
    }

    /**
     * Show popup menu with save options
     */
    private fun savePopUpOptions() {
        val popupMenu = PopupMenu(context, bSave)
        popupMenu.menuInflater.inflate(R.menu.save_menu, popupMenu.menu)


        popupMenu.setOnMenuItemClickListener { menuItem ->
            PermissionsUtils.instance?.callRequestPermissionWrite(activity as Activity, object : PermissionListener {
                override fun onPermissionDenied(response: PermissionDeniedResponse) {}

                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    saveFileAndSend(menuItem.itemId, false)
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest,
                    token: PermissionToken
                ) {
                }
            })
            false
        }

        popupMenu.show()
    }

    private fun selectName(callBack: TextDialog) {

        context?.let {
            val alertDialog = AlertDialog.Builder(it)
            alertDialog.setTitle(R.string.tittle_name_of_the_file)
            val view = layoutInflater.inflate(R.layout.name_selector, null)

            val input = view.findViewById<EditText>(R.id.txtName)
            alertDialog.setCancelable(false)

            alertDialog.setPositiveButton(
                android.R.string.ok
            ) { _, _ ->
                if (input.text.toString().equals("", ignoreCase = true)) {
                    callBack.onGetTextDialog(FilesUtils.cleanName("no_name"))
                } else {
                    callBack.onGetTextDialog(FilesUtils.cleanName(input.text.toString()))
                }
            }

            alertDialog.setNegativeButton(
                android.R.string.cancel
            ) { dialog, _ -> dialog.cancel() }

            alertDialog.setNeutralButton(R.string.tittle_clean) { _, _ -> }
            alertDialog.setView(view)
            alertDialog.show()
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private fun selectWallpaper() {
        when (Utils.wallpaper) {
            1 -> {
                context?.let {
                    txtSingHere.setTextColor(ContextCompat.getColor(it, android.R.color.black))
                    layoutWallapaper.background = getDrawable(it, R.drawable.fondotrans1)
                }
            }
            2 -> {
                context?.let {
                    txtSingHere.setTextColor(ContextCompat.getColor(it, android.R.color.white))
                    layoutWallapaper.background = getDrawable(it, R.drawable.fondotrans2)
                }
            }
            3 -> {

                context?.let {
                    txtSingHere.setTextColor(ContextCompat.getColor(it, android.R.color.black))
                    layoutWallapaper.setBackgroundColor(ContextCompat.getColor(it, android.R.color.white))
                }
            }
            4 -> {
                context?.let {
                    txtSingHere.setTextColor(ContextCompat.getColor(it, android.R.color.white))
                    layoutWallapaper.setBackgroundColor(ContextCompat.getColor(it, android.R.color.black))
                }
            }
        }
    }

    /**
     * Method for set color in picker color selector
     */
    private fun setColor() {
        picker.color = Utils.penColor
        picker.oldCenterColor = Utils.penColor
        bColor.colorNormal = Utils.penColor
        singBoard.setPenColor(Utils.penColor)
    }

    /**
     * Method for set the seekbar
     */
    private fun setSeekBarRange() {
        rangeBar.setRangePinsByValue(Utils.minStroke.toFloat(), Utils.maxStroke.toFloat())
        singBoard.setMinWidth(Utils.minStroke.toFloat())
        singBoard.setMaxWidth(Utils.maxStroke.toFloat())
    }

    /**
     * Show popup menu with share options
     */
    private fun sharePopUpOptions() {
        val popupMenu = PopupMenu(context, bSave)
        popupMenu.menuInflater.inflate(R.menu.share_menu, popupMenu.menu)


        popupMenu.setOnMenuItemClickListener { menuItem ->
            PermissionsUtils.instance?.callRequestPermissionWrite(activity as Activity, object : PermissionListener {
                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                }

                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    saveFileAndSend(menuItem.itemId, true)
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest,
                    token: PermissionToken
                ) {
                }
            })
            false
        }

        popupMenu.show()
    }

    /**
     * Method for show color picker
     */
    private fun showColorPicker() {
        if (coloPickerLayout.visibility == View.VISIBLE) {
            hideColorPicker()
        } else {
            val animation = YoYo.with(Techniques.DropOut)
                .duration(700).withListener(object : Animator.AnimatorListener {
                    override fun onAnimationCancel(animation: Animator) {}

                    override fun onAnimationEnd(animation: Animator) {
                    }

                    override fun onAnimationRepeat(animation: Animator) {}

                    override fun onAnimationStart(animation: Animator) {
                        coloPickerLayout.visibility = View.VISIBLE
                        hideSeekbarStroke()
                    }
                })
            if ((runningAnimationSeek == null || runningAnimationSeek?.isRunning == false) && (runningAnimationColor == null || runningAnimationColor?.isRunning == false)) {
                runningAnimationColor = animation.playOn(coloPickerLayout)
            }
        }
    }

    /**
     * Method for show seekbar stroke
     */
    private fun showSeekbarStroke() {
        if (rangeSeekBarLayout.visibility == View.VISIBLE) {
            hideSeekbarStroke()
        } else {

            val animation = YoYo.with(Techniques.DropOut)
                .duration(700).withListener(object : Animator.AnimatorListener {
                    override fun onAnimationCancel(animation: Animator) {}

                    override fun onAnimationEnd(animation: Animator) {
                    }

                    override fun onAnimationRepeat(animation: Animator) {}

                    override fun onAnimationStart(animation: Animator) {
                        rangeSeekBarLayout.visibility = View.VISIBLE
                        hideColorPicker()
                    }
                })
            if ((runningAnimationSeek == null || runningAnimationSeek?.isRunning == false) && (runningAnimationColor == null || runningAnimationColor?.isRunning == false)) {

                runningAnimationSeek = animation.playOn(rangeSeekBarLayout)
            }
        }
    }
}
