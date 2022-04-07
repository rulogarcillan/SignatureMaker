/*
 __ _                   _                                 _
/ _(_) __ _ _ __   __ _| |_ _   _ _ __ ___    /\/\   __ _| | _____ _ __
\ \| |/ _` | '_ \ / _` | __| | | | '__/ _ \  /    \ / _` | |/ / _ \ '__|
_\ \ | (_| | | | | (_| | |_| |_| | | |  __/ / /\/\ \ (_| |   <  __/ |
\__/_|\__, |_| |_|\__,_|\__|\__,_|_|  \___| \/    \/\__,_|_|\_\___|_|
      |___/

Copyright (C) 2018  Raúl Rodríguez Concepción www.tuppersoft.com

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
package com.signaturemaker.app.application.features.sign

import android.Manifest.permission
import android.animation.Animator
import android.annotation.TargetApi
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.getbase.floatingactionbutton.FloatingActionsMenu
import com.github.gcacace.signaturepad.views.SignaturePad
import com.larswerkman.holocolorpicker.ColorPicker
import com.signaturemaker.app.R
import com.signaturemaker.app.application.core.extensions.Utils
import com.signaturemaker.app.application.core.extensions.createSnackBar
import com.signaturemaker.app.application.core.extensions.shareSign
import com.signaturemaker.app.application.core.extensions.showToast
import com.signaturemaker.app.application.core.platform.GlobalFragment
import com.signaturemaker.app.application.features.main.MainActivity
import com.signaturemaker.app.application.features.main.SharedViewModel
import com.signaturemaker.app.application.features.menu.SettingActivity
import com.signaturemaker.app.databinding.SingBoardFragmentBinding
import com.signaturemaker.app.domain.models.error.FileError.CreateError
import com.signaturemaker.app.domain.models.error.FileError.EmptyBitmap
import com.tuppersoft.skizo.android.core.extension.gone
import com.tuppersoft.skizo.android.core.extension.visible
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

/**
 * A placeholder fragment containing a simple view.
 */
@AndroidEntryPoint
class SignBoardFragment : GlobalFragment(), View.OnClickListener, View.OnLongClickListener {

    override val toolbarTitle: String
        get() = getString(R.string.app_name)
    override val showBackButton: Boolean
        get() = false

    private var runningAnimationSeek: YoYo.YoYoString? = null
    private var runningAnimationColor: YoYo.YoYoString? = null
    private var runningAnimation: YoYo.YoYoString? = null

    private var _binding: SingBoardFragmentBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val signBoardViewModel: SignBoardViewModel by viewModels()

    private fun interface TextDialog {

        fun onGetTextDialog(name: String)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = SingBoardFragmentBinding.inflate(inflater, container, false)
        binding.singBoard.isSaveEnabled = false
        initObserver()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showBackButton()
        binding.actionsButtons.bList?.let {
            it.setOnClickListener(this)
            it.setOnLongClickListener(this)
        }

        binding.actionsButtons.bSave.setOnClickListener(this)
        binding.actionsButtons.bSaveSend.setOnClickListener(this)
        binding.actionsButtons.bColor.setOnClickListener(this)
        binding.actionsButtons.bStroke.setOnClickListener(this)
        binding.actionsButtons.bRubber.setOnClickListener(this)
        binding.actionsButtons.bWallpaper.setOnClickListener(this)

        binding.actionsButtons.bSave.setOnLongClickListener(this)
        binding.actionsButtons.bSaveSend.setOnLongClickListener(this)
        binding.actionsButtons.bColor.setOnLongClickListener(this)
        binding.actionsButtons.bStroke.setOnLongClickListener(this)
        binding.actionsButtons.bRubber.setOnLongClickListener(this)

        //add bar to picker
        binding.cpColorPicker.picker.addSVBar(binding.cpColorPicker.svbar)

        //listener floating actions buttons
        binding.actionsButtons.fabUp.setOnFloatingActionsMenuUpdateListener(object :
            FloatingActionsMenu.OnFloatingActionsMenuUpdateListener {
            override fun onMenuCollapsed() {
                collapseFabs()
            }

            override fun onMenuExpanded() {
                expandFabs()
            }
        })

        //listener board
        binding.singBoard.setOnSignedListener(object : SignaturePad.OnSignedListener {

            override fun onClear() {
                binding.txtSingHere.visible()
            }

            override fun onSigned() {
                //Event triggered when the pad is signed
            }

            override fun onStartSigning() {
                binding.txtSingHere.gone()
                hideColorPicker()
                hideSeekbarStroke()
            }
        })

        //listener rangebar
        binding.stSlider.rangeBar.setOnRangeBarChangeListener { _, leftPinIndex, rightPinIndex, _, _ ->
            Utils.minStroke = leftPinIndex
            Utils.maxStroke = rightPinIndex
            binding.singBoard.setMinWidth(Utils.minStroke.toFloat())
            binding.singBoard.setMaxWidth(Utils.maxStroke.toFloat())
            Utils.saveAllPreferences(context)
        }

        //listerner picker
        binding.cpColorPicker.picker.onColorChangedListener =
            ColorPicker.OnColorChangedListener { i ->
                Utils.penColor = i
                binding.actionsButtons.bColor.colorNormal = Utils.penColor
                binding.singBoard.setPenColor(Utils.penColor)
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
            R.id.bStroke -> showSeekBarStroke()
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

    private fun changeWallpaper() {
        Utils.wallpaper++
        if (Utils.wallpaper == 5) {
            Utils.wallpaper = 1
        }
        Utils.saveAllPreferences(activity)
        selectWallpaper()
    }

    private fun showBackButton() {
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
                    _binding?.singBoard?.apply {
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
            runningAnimation = animation.playOn(binding.singBoard)
        }
    }

    /**
     * Method for collapse the fabs buttons
     */
    private fun collapseFabs() {
        binding.actionsButtons.fabUp.collapse()
        binding.actionsButtons.fabLeft.collapse()
        hideSeekbarStroke()
        hideColorPicker()
    }

    /**
     * Method for expand the fabs buttons
     */
    private fun expandFabs() {
        binding.actionsButtons.fabUp.expand()
        binding.actionsButtons.fabLeft.expand()
    }

    /**
     * Method for hide color picker
     */
    private fun hideColorPicker() {
        val animation = YoYo.with(Techniques.TakingOff)
            .duration(400).withListener(object : Animator.AnimatorListener {
                override fun onAnimationCancel(animation: Animator) {}

                override fun onAnimationEnd(animation: Animator) {
                    _binding?.cpColorPicker?.root?.visibility = View.INVISIBLE
                }

                override fun onAnimationRepeat(animation: Animator) {}

                override fun onAnimationStart(animation: Animator) {
                }
            })
        if (runningAnimationColor == null || runningAnimationColor?.isRunning == false) {
            runningAnimationColor = animation.playOn(binding.cpColorPicker.root)
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
                    _binding?.stSlider?.root?.visibility = View.INVISIBLE
                }

                override fun onAnimationRepeat(animation: Animator) {}

                override fun onAnimationStart(animation: Animator) {
                    // collapseFabs();
                }
            })
        if (runningAnimationSeek == null || runningAnimationSeek?.isRunning == false) {
            runningAnimationSeek = animation.playOn(binding.stSlider.root)
        }
    }

    private fun openListFilesFragment() {

        (activity as? MainActivity)?.let { mActivity ->
            mActivity.runWithPermission(
                { navigateToListFiles() },
                {},
                permission.WRITE_EXTERNAL_STORAGE
            )
        }
    }

    private fun navigateToListFiles() {
        findNavController().navigate(SignBoardFragmentDirections.actionSingBoardFragmentToListFilesFragment())
    }

    private fun initObserver() {
        handleSaveBitmap()
        handleFailure()
    }

    private fun handleSaveBitmap() {

        signBoardViewModel.saveBitmap.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { uriResponse ->
                activity?.createSnackBar(resources.getString(R.string.title_save_ok))?.show()
                sharedViewModel.reloadFileList()
                if (uriResponse.share) {
                    activity?.shareSign(uriResponse.uri)
                }
            }
        }
    }

    private fun handleFailure() {
        signBoardViewModel.failure.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { failure ->
                when (failure) {
                    is EmptyBitmap -> {
                        activity?.createSnackBar(failure.message)?.show()
                    }
                    is CreateError -> {
                        activity?.createSnackBar(failure.message)?.show()
                    }
                }
            }
        }
    }

    private fun selectedNameAndSave(idMenu: Int, share: Boolean) {
        selectName { name -> saveFileAndSend(idMenu, share, name) }
    }

    private fun saveFileAndSend(idMenu: Int, share: Boolean, name: String? = null) {
        when (idMenu) {
            R.id.savePngTrans -> {
                signBoardViewModel.saveFileBitmap(
                    share,
                    binding.singBoard.getTransparentSignatureBitmap(true),
                    Utils.path,
                    name
                )
            }
            R.id.savePngWhite -> {

                signBoardViewModel.saveFileBitmap(
                    share,
                    binding.singBoard.signatureBitmap,
                    Utils.path,
                    name
                )
            }
            /*R.id.saveSvg -> {
                binding.singBoard.signatureSvg?.let {
                    name = FilesUtils.addExtensionNameSvg(FilesUtils.generateName())
                    status = FilesUtils.saveSvgFile(it, name)
                }
            }*/
        }
    }

    /**
     * Show popup menu with save options
     */
    private fun savePopUpOptions() {
        val popupMenu = PopupMenu(context, binding.actionsButtons.bSave)
        popupMenu.menuInflater.inflate(R.menu.save_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->

            (activity as? MainActivity)?.let { mActivity ->
                mActivity.runWithPermission({
                    if (Utils.nameSave) {
                        selectedNameAndSave(menuItem.itemId, false)
                    } else {
                        saveFileAndSend(menuItem.itemId, false)
                    }
                }, {}, permission.WRITE_EXTERNAL_STORAGE)

            }
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
                    callBack.onGetTextDialog(cleanName("no_name"))
                } else {
                    callBack.onGetTextDialog(cleanName(input.text.toString()))
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

    fun addExtensionNameSvg(name: String) = "SM_$name.svg"

    private fun cleanName(name: String): String {

        val original = " áàäéèëíìïóòöúùuñÁÀÄÉÈËÍÌÏÓÒÖÚÙÜÑçÇ.\\/:?*\"<>|"
        val ascii = "_aaaeeeiiiooouuunAAAEEEIIIOOOUUUNcC          "
        var output = name

        for (i in original.indices) {
            output =
                output.replace(original[i], ascii[i]).toLowerCase(Locale.ROOT).trim { it <= ' ' }
        }
        return output
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private fun selectWallpaper() {
        when (Utils.wallpaper) {
            1 -> {
                context?.let {

                    binding.txtSingHere.setTextColor(ContextCompat.getColor(it, R.color.darkGrey))
                    binding.root.background = getDrawable(it, R.drawable.fondotrans1)
                }
            }
            2 -> {
                context?.let {
                    binding.txtSingHere.setTextColor(
                        ContextCompat.getColor(
                            it,
                            android.R.color.white
                        )
                    )
                    binding.root.background = getDrawable(it, R.drawable.fondotrans2)
                }
            }
            3 -> {

                context?.let {
                    binding.txtSingHere.setTextColor(ContextCompat.getColor(it, R.color.darkGrey))
                    binding.root.setBackgroundColor(
                        ContextCompat.getColor(
                            it,
                            android.R.color.white
                        )
                    )
                }
            }
            4 -> {
                context?.let {
                    binding.txtSingHere.setTextColor(
                        ContextCompat.getColor(
                            it,
                            android.R.color.white
                        )
                    )
                    binding.root.setBackgroundColor(
                        ContextCompat.getColor(
                            it,
                            android.R.color.black
                        )
                    )
                }
            }
        }
    }

    /**
     * Method for set color in picker color selector
     */
    private fun setColor() {
        binding.cpColorPicker.picker.color = Utils.penColor
        binding.cpColorPicker.picker.oldCenterColor = Utils.penColor
        binding.actionsButtons.bColor.colorNormal = Utils.penColor
        binding.singBoard.setPenColor(Utils.penColor)
    }

    /**
     * Method for set the seekbar
     */
    private fun setSeekBarRange() {
        binding.stSlider.rangeBar.setRangePinsByValue(
            Utils.minStroke.toFloat(),
            Utils.maxStroke.toFloat()
        )
        binding.singBoard.setMinWidth(Utils.minStroke.toFloat())
        binding.singBoard.setMaxWidth(Utils.maxStroke.toFloat())
    }

    /**
     * Show popup menu with share options
     */
    private fun sharePopUpOptions() {
        val popupMenu = PopupMenu(context, binding.actionsButtons.bSave)
        popupMenu.menuInflater.inflate(R.menu.share_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->

            (activity as? MainActivity)?.let { mActivity ->
                mActivity.runWithPermission({
                    if (Utils.nameSave) {
                        selectedNameAndSave(menuItem.itemId, true)
                    } else {
                        saveFileAndSend(menuItem.itemId, true)
                    }
                }, {}, permission.WRITE_EXTERNAL_STORAGE)

            }

            false
        }
        popupMenu.show()
    }

    /**
     * Method for show color picker
     */
    private fun showColorPicker() {
        if (binding.cpColorPicker.root.visibility == View.VISIBLE) {
            hideColorPicker()
        } else {
            val animation = YoYo.with(Techniques.DropOut)
                .duration(700).withListener(object : Animator.AnimatorListener {
                    override fun onAnimationCancel(animation: Animator) {}

                    override fun onAnimationEnd(animation: Animator) {
                    }

                    override fun onAnimationRepeat(animation: Animator) {}

                    override fun onAnimationStart(animation: Animator) {
                        _binding?.cpColorPicker?.root?.visibility = View.VISIBLE
                        hideSeekbarStroke()
                    }
                })
            if ((runningAnimationSeek == null || runningAnimationSeek?.isRunning == false) && (runningAnimationColor == null || runningAnimationColor?.isRunning == false)) {
                runningAnimationColor = animation.playOn(binding.cpColorPicker.root)
            }
        }
    }

    /**
     * Method for show seekbar stroke
     */
    private fun showSeekBarStroke() {
        if (binding.stSlider.root.visibility == View.VISIBLE) {
            hideSeekbarStroke()
        } else {

            val animation = YoYo.with(Techniques.DropOut)
                .duration(700).withListener(object : Animator.AnimatorListener {
                    override fun onAnimationCancel(animation: Animator) {}

                    override fun onAnimationEnd(animation: Animator) {
                    }

                    override fun onAnimationRepeat(animation: Animator) {}

                    override fun onAnimationStart(animation: Animator) {
                        _binding?.stSlider?.root?.visibility = View.VISIBLE
                        hideColorPicker()
                    }
                })
            if ((runningAnimationSeek == null || runningAnimationSeek?.isRunning == false) && (runningAnimationColor == null || runningAnimationColor?.isRunning == false)) {

                runningAnimationSeek = animation.playOn(binding.stSlider.root)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)

        menu.findItem(R.id.idSettings).setOnMenuItemClickListener {
            activity?.let {
                val myIntent = Intent(it, SettingActivity::class.java)
                startActivity(myIntent)
                it.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
            }
            true
        }

        super.onCreateOptionsMenu(menu, inflater)
    }
}
