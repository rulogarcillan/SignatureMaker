package com.signaturemaker.app.application.features.setting.changelog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.signaturemaker.app.R
import com.signaturemaker.app.R.layout
import com.signaturemaker.app.application.core.extensions.inflate
import com.signaturemaker.app.application.core.extensions.toDateStringLocalFormat
import com.signaturemaker.app.application.features.setting.changelog.ChangelogIdentifier.FEAT
import com.signaturemaker.app.application.features.setting.changelog.ChangelogIdentifier.FIX
import com.signaturemaker.app.domain.models.Change
import com.signaturemaker.app.domain.models.Changelog
import com.tuppersoft.skizo.core.extension.getColorFromAttr
import java.util.Locale

/**
 * Created by Raúl Rodríguez Concepción on 18/04/2020.
 * raulrcs@gmail.com
 */

class ChangelogAdapter : ListAdapter<Changelog, ItemChangelogHolder>(SettingChangelogCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemChangelogHolder {
        return ItemChangelogHolder(parent.inflate(layout.item_changelog))
    }

    override fun onBindViewHolder(holder: ItemChangelogHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class ItemChangelogHolder(val root: View) : RecyclerView.ViewHolder(root) {

    private val tvTittle = root.findViewById<TextView>(R.id.tvTitle)
    private val llChangelog = root.findViewById<LinearLayout>(R.id.llChangelog)

    fun bind(
        item: Changelog
    ) {

        item.change.forEach { changeItem ->
            val v = changelogView(root.context, changeItem)
            llChangelog.addView(v)
        }

        tvTittle.text = """${item.versionName}  (${item.getDateInLong().toDateStringLocalFormat()})"""
    }

    private fun changelogView(mContext: Context, changeItem: Change): View {
        val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v = inflater.inflate(R.layout.item_changelog_details, null)
        // val chipGroup: ChipGroup = v.findViewById(R.id.cgItem)
        val tvChangelog: TextView = v.findViewById(R.id.tvChangelog)
        val tvTypeChangelog: TextView = v.findViewById(R.id.tvTypeChangelog)
        val cvTypeChangelog: MaterialCardView = v.findViewById(R.id.cvTypeChangelog)

        val style = when (changeItem.type.toUpperCase(Locale.ROOT)) {
            FIX.toString().toUpperCase(Locale.ROOT) -> {
                R.style.chip_fix
            }
            FEAT.toString().toUpperCase(Locale.ROOT) -> {
                R.style.chip_feat
            }
            else -> {
                R.style.chip_any
            }
        }
        tvTypeChangelog.text = changeItem.type.toLowerCase(Locale.ROOT)
        tvChangelog.text = changeItem.text


        when (changeItem.type.toUpperCase(Locale.ROOT)) {
            FIX.toString().toUpperCase(Locale.ROOT) -> {
                cvTypeChangelog.setCardBackgroundColor(
                    ContextCompat.getColorStateList(
                        mContext,
                        R.color.chip_fix_color
                    )
                )
                tvTypeChangelog.setTextColor(mContext.getColorFromAttr(R.attr.colorChipChangelogText))
            }
            FEAT.toString().toUpperCase(Locale.ROOT) -> {
                cvTypeChangelog.setCardBackgroundColor(
                    ContextCompat.getColorStateList(
                        mContext,
                        R.color.chip_feat_color
                    )
                )
                tvTypeChangelog.setTextColor(mContext.getColorFromAttr(R.attr.colorChipChangelogText))
            }
        }
        return v
    }
}

class SettingChangelogCallback : DiffUtil.ItemCallback<Changelog>() {

    override fun areItemsTheSame(oldItem: Changelog, newItem: Changelog): Boolean = oldItem == newItem

    override fun areContentsTheSame(oldItem: Changelog, newItem: Changelog): Boolean = oldItem == newItem
}
