package com.signaturemaker.app.application.features.menu

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.signaturemaker.app.R
import com.signaturemaker.app.application.core.extensions.inflate

/**
 * Created by Raúl Rodríguez Concepción on 18/04/2020.
 * raulrcs@gmail.com
 */

class SettingAdapter : ListAdapter<ItemSettingMenu, ItemSettingHolder>(SettingItemDiffCallback()) {

    private var onClickItem: ((item: ItemSettingMenu) -> Unit)? = null

    fun setOnClickItemListener(lambda: (item: ItemSettingMenu) -> Unit) {
        this.onClickItem = lambda
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemSettingHolder {
        return ItemSettingHolder(parent.inflate(R.layout.item_setting))
    }

    override fun onBindViewHolder(holder: ItemSettingHolder, position: Int) {
        holder.bind(getItem(position), onClickItem)
    }
}

class ItemSettingHolder(val root: View) : RecyclerView.ViewHolder(root) {

    private val ivIcon = root.findViewById<ImageView>(R.id.ivIcon)
    private val tvTitle = root.findViewById<TextView>(R.id.tvTitle)

    fun bind(
        item: ItemSettingMenu,
        onClickItem: ((item: ItemSettingMenu) -> Unit)? = null
    ) {
        root.setOnClickListener {
            onClickItem?.let {
                onClickItem(item)
            }
        }
        ivIcon.setImageResource(item.iconResourceId)
        tvTitle.setText(item.title)
    }
}

class SettingItemDiffCallback : DiffUtil.ItemCallback<ItemSettingMenu>() {

    override fun areItemsTheSame(oldItem: ItemSettingMenu, newItem: ItemSettingMenu): Boolean = oldItem == newItem

    override fun areContentsTheSame(oldItem: ItemSettingMenu, newItem: ItemSettingMenu): Boolean = oldItem == newItem
}
