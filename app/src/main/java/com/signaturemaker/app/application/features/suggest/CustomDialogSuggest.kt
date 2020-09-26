package com.signaturemaker.app.application.features.suggest

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager.LayoutParams
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.signaturemaker.app.R
import com.signaturemaker.app.application.core.extensions.isValidEmail
import com.signaturemaker.app.databinding.CustomDialogSuggestBinding
import com.signaturemaker.app.domain.models.SuggestMessage
import com.tuppersoft.skizo.android.core.extension.collapse
import com.tuppersoft.skizo.android.core.extension.expand
import com.tuppersoft.skizo.android.core.extension.getColorFromAttr
import com.tuppersoft.skizo.android.core.extension.gone
import com.tuppersoft.skizo.android.core.extension.hideKeyboard
import com.tuppersoft.skizo.android.core.extension.visible
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
open class CustomDialogSuggest : DialogFragment() {

    private val suggestViewModel: SuggestViewModel by viewModels()
    private var _binding: CustomDialogSuggestBinding? = null
    private val binding get() = _binding!!

    companion object {

        fun getInstance(): CustomDialogSuggest = CustomDialogSuggest()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.fullscreen_dialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = CustomDialogSuggestBinding.inflate(inflater, container, false)
        initObservers()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.isCancelable = true

        binding.telegramJoin.root.setOnClickListener {

            val url = if (Locale.getDefault().language.toLowerCase(Locale.ROOT) == "es") {
                "https://telegram.me/signature_maker_es"
            } else {
                "https://telegram.me/signature_maker_eng"
            }

            val telegram = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(telegram)
        }

        binding.btPositive.setOnClickListener {

            if (binding.tvEmail.text.toString().isEmpty() || binding.tvMessage.text.toString().isEmpty()
            ) {
                Toast.makeText(view.context, getString(R.string.complete_fields), Toast.LENGTH_LONG).show()
            } else {
                if (binding.tvEmail.text.toString().isValidEmail()) {
                    val msg = SuggestMessage(
                        binding.tvEmail.text.toString(),
                        binding.tvMessage.text.toString()
                    )
                    suggestViewModel.sendSuggest(msg)
                    activity?.let {
                        setStatusBarColorIfPossible(it.getColorFromAttr(android.R.attr.colorBackground))
                    }
                    binding.root.hideKeyboard()
                    binding.loading.visible()
                } else {
                    Toast.makeText(view.context, getString(R.string.valid_mail), Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.btNegative.setOnClickListener {
            dismiss()
        }
    }

    private fun initObservers() {
        observeSendSuggest()
    }

    private fun observeSendSuggest() {
        suggestViewModel.sendSuccess.observe(viewLifecycleOwner, {
            if (it) {
                dismiss()
                Toast.makeText(view?.context, getString(R.string.thanks_comment), Toast.LENGTH_LONG).show()
            } else {
                binding.loading.gone()
                activity?.let {
                    setStatusBarColorIfPossible(it.getColorFromAttr(R.attr.colorPrimaryVariant))
                }
                Toast.makeText(view?.context, getString(R.string.problems_comment), Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun setStatusBarColorIfPossible(color: Int) {
        if (VERSION.SDK_INT >= VERSION_CODES.M && !isUsingNightModeResources()) {
            dialog?.window?.let {
                var flags = it.decorView.systemUiVisibility
                flags = flags.xor(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
                it.decorView.systemUiVisibility = flags
                it.addFlags(LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                it.statusBarColor = color
            }
        }
    }

    private fun isUsingNightModeResources(): Boolean {
        return when (resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> true
            Configuration.UI_MODE_NIGHT_NO -> false
            Configuration.UI_MODE_NIGHT_UNDEFINED -> false
            else -> false
        }
    }

    override fun onResume() {
        super.onResume()
        // showBannerTelegram()
    }

    private fun showBannerTelegram() {
        binding.telegramJoin.root.apply {
            postDelayed({ expand() }, 500)
            postDelayed({
                collapse()
            }, 1000 * 5)
        }
    }
}

