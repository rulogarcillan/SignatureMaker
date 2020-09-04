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
package com.signaturemaker.app.application.features.files

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.daimajia.swipe.SwipeLayout
import com.daimajia.swipe.implments.SwipeItemRecyclerMangerImpl
import com.daimajia.swipe.interfaces.SwipeAdapterInterface
import com.daimajia.swipe.interfaces.SwipeItemMangerInterface
import com.daimajia.swipe.util.Attributes.Mode
import com.signaturemaker.app.R
import com.signaturemaker.app.databinding.ItemExploreBinding
import com.signaturemaker.app.domain.models.ItemFile

class AdapterFiles :
    ListAdapter<ItemFile, FilesViewHolder>(DiffUtilsFilesBaseItem), SwipeAdapterInterface, SwipeItemMangerInterface {

    private var onClickItem: ((item: ItemFile, imageView: ImageView) -> Unit)? = null
    private var onClickShare: ((item: ItemFile) -> Unit)? = null
    private var onClickDelete: ((item: ItemFile) -> Unit)? = null

    private var mItemManger = SwipeItemRecyclerMangerImpl(this)

    fun setOnClickItemListener(lambda: ((item: ItemFile, imageView: ImageView) -> Unit)) {
        this.onClickItem = lambda
    }

    fun setOnClickShare(lambda: ((item: ItemFile) -> Unit)) {
        this.onClickShare = lambda
    }

    fun setOnClickDelete(lambda: ((item: ItemFile) -> Unit)) {
        this.onClickDelete = lambda
    }

    override fun onBindViewHolder(viewHolder: FilesViewHolder, pos: Int) {
        mItemManger.bindView(viewHolder.itemView, pos)
        viewHolder.bind(currentList[pos], pos, mItemManger, onClickItem, onClickShare, onClickDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilesViewHolder {
        return FilesViewHolder(ItemExploreBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun submitList(list: List<ItemFile>?) {
        super.submitList(list?.toList())
        notifyDataSetChanged()
    }

    object DiffUtilsFilesBaseItem : DiffUtil.ItemCallback<ItemFile>() {

        override fun areItemsTheSame(oldItem: ItemFile, newItem: ItemFile) = newItem.name == oldItem.name

        override fun areContentsTheSame(oldItem: ItemFile, newItem: ItemFile) = newItem == oldItem
    }

    override fun getSwipeLayoutResourceId(position: Int): Int {
        return R.id.swipelayout
    }

    override fun closeAllExcept(layout: SwipeLayout?) {
        mItemManger.closeAllExcept(layout)
    }

    override fun openItem(position: Int) {
    }

    override fun closeItem(position: Int) {
    }

    override fun closeAllItems() {
    }

    override fun getOpenItems(): MutableList<Int> {
        return mItemManger.openItems
    }

    override fun getOpenLayouts(): MutableList<SwipeLayout> {
        return mItemManger.openLayouts
    }

    override fun removeShownLayouts(layout: SwipeLayout?) {
    }

    override fun isOpen(position: Int): Boolean {
        return mItemManger.isOpen(position)
    }

    override fun getMode(): Mode {
        return mItemManger.mode
    }

    override fun setMode(mode: Mode?) {
    }
}
