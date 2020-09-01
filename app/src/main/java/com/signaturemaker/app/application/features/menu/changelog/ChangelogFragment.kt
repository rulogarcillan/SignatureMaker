package com.signaturemaker.app.application.features.menu.changelog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.signaturemaker.app.R
import com.signaturemaker.app.application.core.platform.GlobalFragment
import com.signaturemaker.app.databinding.ChangelogFragmentBinding
import com.signaturemaker.app.domain.models.Changelog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChangelogFragment : GlobalFragment() {

    companion object {

        fun newInstance() = ChangelogFragment()
    }

    @Inject
    lateinit var changelog: List<Changelog>

    override val toolbarTitle: String
        get() = getString(R.string.changelog)
    override val showBackButton: Boolean
        get() = true

    private var _binding: ChangelogFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ChangelogFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        val changelogAdapter = ChangelogAdapter()
        binding.rvChangelog.layoutManager = GridLayoutManager(context, 1)
        binding.rvChangelog.adapter = changelogAdapter
        val list = changelog.toMutableList()
        list.sortByDescending { it.versionCode }
        changelogAdapter.submitList(list)
    }
}
