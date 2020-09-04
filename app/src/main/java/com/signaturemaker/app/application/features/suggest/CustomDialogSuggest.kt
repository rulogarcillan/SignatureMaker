package com.signaturemaker.app.application.features.suggest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.signaturemaker.app.R
import com.signaturemaker.app.application.core.extensions.isValidEmail
import com.signaturemaker.app.databinding.CustomDialogSuggestBinding
import com.signaturemaker.app.domain.models.SuggestMessage
import dagger.hilt.android.AndroidEntryPoint

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
        this.isCancelable = false

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
        suggestViewModel.sendSussecs.observe(viewLifecycleOwner, {
            if (it) {
                dismiss()
                Toast.makeText(view?.context, getString(R.string.thanks_comment), Toast.LENGTH_LONG).show()
            }

        })
    }
}

