package com.signaturemaker.app.application.features.files

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


import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import co.dift.ui.SwipeToAction
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.signaturemaker.app.R
import com.signaturemaker.app.application.core.extensions.Utils
import com.signaturemaker.app.application.core.extensions.createSnackBar
import com.signaturemaker.app.application.core.extensions.loadFromUrl
import com.signaturemaker.app.application.core.platform.FilesUtils
import com.signaturemaker.app.application.core.platform.PermissionsUtils
import com.signaturemaker.app.application.features.main.MainActivity
import com.signaturemaker.app.application.utils.Constants
import com.signaturemaker.app.domain.models.ItemFile
import kotlinx.android.synthetic.main.list_files_fragment.recyclerView
import kotlinx.android.synthetic.main.list_files_fragment.txtMnsNoFiles
import kotlinx.android.synthetic.main.pathbar.path

class ListFilesFragment : Fragment() {

    private var adapter: AdapterFiles? = null
    private val items: MutableList<ItemFile> = mutableListOf()
    private var mySnackBar: Snackbar? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.list_files_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)
        showBackButton()
        loadItemsFiles()
        adapter = AdapterFiles(items) //Agregamos los items al adapter

        //definimos el recycler y agregamos el adaptaer
        recyclerView.setHasFixedSize(true)
        val layoutManager = StaggeredGridLayoutManager(
            1,
            StaggeredGridLayoutManager.VERTICAL
        )
        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        val itemDecoration = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(itemDecoration)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter

        SwipeToAction(recyclerView,
            object : SwipeToAction.SwipeListener<ItemFile> {

                override fun onClick(itemData: ItemFile) {

                    showPreviewImage(itemData)
                }

                override fun onLongClick(itemData: ItemFile) {
                }

                override fun swipeLeft(itemData: ItemFile): Boolean {
                    adapter?.let { mAdapter ->
                        val pos = items.indexOf(itemData)
                        if (pos == -1) {
                            return true
                        }
                        removeItemAdapter(itemData)
                        activity?.let { mActivity ->
                            mySnackBar = mActivity.createSnackBar(itemData.name,
                                resources.getString(R.string.tittle_undo), object : Snackbar.Callback() {
                                    override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                                        if (event != 1) {
                                            FilesUtils.removeFile(mActivity, itemData.name)
                                            if (items.size > 0) {
                                                txtMnsNoFiles.visibility = View.GONE
                                            } else {
                                                txtMnsNoFiles.visibility = View.VISIBLE
                                            }
                                            Utils.sort(mAdapter.items, Utils.sortOrder)
                                        } else {
                                            addItemAdapter(pos, itemData)
                                            loadItemsFiles()
                                            Utils.sort(mAdapter.items, Utils.sortOrder)
                                        }
                                        super.onDismissed(transientBottomBar, event)
                                    }
                                })
                            mySnackBar?.show()
                        }
                    }
                    return true
                }

                override fun swipeRight(itemData: ItemFile): Boolean {
                    activity?.let {
                        Utils.shareSign(it, itemData.name)
                    }
                    return true
                }
            })
    }

    private fun showBackButton() {
        activity?.let { mActivity ->
            if (mActivity is MainActivity) {
                mActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        reloadFiles()
        path.text = Utils.path.replace(Constants.ROOT, "/sdcard")
    }

    override fun onDestroyView() {
        mySnackBar?.let {
            if (it.isShown) {
                it.dismiss()
            }
        }
        super.onDestroyView()
    }

    fun loadItemsFiles() {

        PermissionsUtils.instance?.callRequestPermissionWrite(activity as Activity, object : PermissionListener {
            override fun onPermissionDenied(response: PermissionDeniedResponse) {}

            override fun onPermissionGranted(response: PermissionGrantedResponse) {
                items.clear()
                items.addAll(FilesUtils.loadItemsFiles())
                if (items.size > 0) {
                    txtMnsNoFiles.visibility = View.GONE
                } else {
                    txtMnsNoFiles.visibility = View.VISIBLE
                }
            }

            override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest, token: PermissionToken) {}
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.help_menu, menu)

        val itemS = menu.findItem(R.id.action_sort)

        itemS.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.action_sort) {
                Utils.sortOrder = Utils.sortOrder * -1
                adapter?.let {
                    Utils.sort(it.items, Utils.sortOrder)
                    adapter?.notifyDataSetChanged()
                }
            }
            true
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    fun reloadFiles() {
        loadItemsFiles()
        adapter?.notifyDataSetChanged()
    }

    private fun addItemAdapter(pos: Int, item: ItemFile) {
        adapter?.items?.add(pos, item)
        adapter?.notifyItemInserted(pos)
    }

    private fun removeItemAdapter(item: ItemFile): Int {
        val pos = items.indexOf(item)
        Log.d(Constants.TAG, item.name)
        adapter?.items?.remove(item)
        adapter?.notifyItemRemoved(pos)
        adapter?.notifyItemRangeChanged(pos, items.size)
        return pos
    }

    private fun showPreviewImage(itemData: ItemFile) {
        activity?.let {
            val alertDialog = AlertDialog.Builder(it)
            //alertDialog.setTitle(R.string.tittle_name_of_the_file);
            val view = it.layoutInflater.inflate(R.layout.imagen_dialog, null)
            val image = view.findViewById<ImageView>(R.id.image)
            image.loadFromUrl("file:///" + Utils.path + "/" + itemData.name)
            alertDialog.setCancelable(true)
            alertDialog.setView(view)
            alertDialog.show()
        }
    }
}
