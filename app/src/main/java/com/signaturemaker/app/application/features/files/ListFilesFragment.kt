package com.signaturemaker.app.application.features.files

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


import android.Manifest.permission
import android.app.Activity
import android.content.Intent
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.snackbar.Snackbar
import com.signaturemaker.app.R
import com.signaturemaker.app.application.core.extensions.Utils
import com.signaturemaker.app.application.core.extensions.createSnackBar
import com.signaturemaker.app.application.core.extensions.shareSign
import com.signaturemaker.app.application.core.platform.GlobalFragment
import com.signaturemaker.app.application.features.image.ImageActivity
import com.signaturemaker.app.application.features.main.MainActivity
import com.signaturemaker.app.application.features.main.SharedViewModel
import com.signaturemaker.app.databinding.ListFilesFragmentBinding
import com.signaturemaker.app.domain.models.ItemFile
import com.tuppersoft.skizo.android.core.extension.gone
import com.tuppersoft.skizo.android.core.extension.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListFilesFragment : GlobalFragment() {

    override val toolbarTitle: String
        get() = getString(R.string.app_name)
    override val showBackButton: Boolean
        get() = true

    private val mAdapter: AdapterFiles = AdapterFiles()
    private var mySnackBar: Snackbar? = null
    private var _binding: ListFilesFragmentBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val listFilesViewModel: ListFilesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ListFilesFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)
        initObserver()

        binding.recyclerView.apply {
            val itemDecoration = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
            addItemDecoration(itemDecoration)
            setHasFixedSize(true)
            adapter = mAdapter
            mAdapter.submitList(
                listOf(
                    ItemFile(name = "", date = "", size = "", shimmer = true),
                    ItemFile(name = "", date = "", size = "", shimmer = true),
                    ItemFile(name = "", date = "", size = "", shimmer = true),
                    ItemFile(name = "", date = "", size = "", shimmer = true),
                    ItemFile(name = "", date = "", size = "", shimmer = true)
                )
            )
        }


        mAdapter.setOnClickItemListener { item, imageView ->
            showDetailsImage(item, imageView)
        }

        mAdapter.setOnClickShare { item ->
            activity?.shareSign(item.uri)
        }

        mAdapter.setOnClickDelete { item ->
            mAdapter.let { mAdapter ->
                val pos = mAdapter.currentList.indexOf(item)
                if (pos != -1) {
                    mAdapter.removeItem(pos)

                    activity?.let { mActivity ->
                        mySnackBar = mActivity.createSnackBar(item.name,
                            resources.getString(R.string.tittle_undo), object : Snackbar.Callback() {
                                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                                    if (event != 1) {
                                        listFilesViewModel.removeFile(item)
                                        if (mAdapter.currentList.isNotEmpty()) {
                                            _binding?.txtMnsNoFiles?.gone()
                                        } else {
                                            _binding?.txtMnsNoFiles?.visible()
                                        }
                                    } else {
                                        reloadFiles()
                                    }
                                    super.onDismissed(transientBottomBar, event)
                                }
                            })
                        mySnackBar?.show()

                    }
                }
            }
        }
    }

    private fun initObserver() {
        initReloadFileList()
        initHandleFileList()
    }

    private fun initHandleFileList() {
        listFilesViewModel.listFiles.observe(viewLifecycleOwner) { list ->
            addListToAdapter(list)
            if (list.isNotEmpty()) {
                binding.txtMnsNoFiles.gone()
            } else {
                binding.txtMnsNoFiles.visible()
            }
        }
    }

    private fun initReloadFileList() {
        sharedViewModel.reloadFileList.observe(viewLifecycleOwner) {
            reloadFiles()
        }
    }

    override fun onResume() {
        super.onResume()
        reloadFiles()
    }

    override fun onDestroyView() {
        mySnackBar?.let {
            if (it.isShown) {
                it.dismiss()
            }
        }
        _binding = null
        super.onDestroyView()
    }

    private fun loadItemsFiles() {
        activity?.let { mActivity ->
            val permission = if (VERSION.SDK_INT >= VERSION_CODES.TIRAMISU) {
                permission.READ_MEDIA_IMAGES
            } else {
                permission.WRITE_EXTERNAL_STORAGE
            }
            (mActivity as? MainActivity)?.let {
                it.runWithPermission({
                    listFilesViewModel.getAllFiles(Utils.path)
                }, {}, permission)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.help_menu, menu)

        val itemS = menu.findItem(R.id.action_sort)

        itemS.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.action_sort) {
                Utils.sortOrder = Utils.sortOrder * -1
                addListToAdapter(Utils.sort(mAdapter.currentList, Utils.sortOrder))
            }
            true
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    fun reloadFiles() {
        loadItemsFiles()
    }

    private fun addListToAdapter(list: List<ItemFile>) {
        mAdapter.submitList(Utils.sort(list, Utils.sortOrder))
        mAdapter.notifyDataSetChanged()
    }

    private fun showDetailsImage(item: ItemFile, imageView: ImageView) {

        val intent = Intent(activity, ImageActivity::class.java)
        val b = Bundle()
        b.putParcelable(ImageActivity.TAG, item)

        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            activity as Activity,
            imageView, ViewCompat.getTransitionName(imageView) ?: ""
        )
        intent.putExtras(b)
        startActivity(intent, options.toBundle())
    }
}
