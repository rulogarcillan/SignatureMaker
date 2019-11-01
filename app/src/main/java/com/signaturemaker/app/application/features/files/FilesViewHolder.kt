package com.signaturemaker.app.application.features.files

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import co.dift.ui.SwipeToAction
import com.signaturemaker.app.R
import com.signaturemaker.app.application.core.extensions.Utils
import com.signaturemaker.app.domain.models.ItemFile
import com.squareup.picasso.Picasso

class FilesViewHolder(rootView: View) : SwipeToAction.ViewHolder<ItemFile>(rootView) {

    private var textName: TextView = rootView.findViewById(R.id.textName)
    private var textDate: TextView = rootView.findViewById(R.id.textDate)
    private var textSize: TextView = rootView.findViewById(R.id.textSize)
    private var iconFile: ImageView = rootView.findViewById(R.id.iconFile)

    fun bind(items: ItemFile) {

        textName.text = items.name.substring(3)
        textDate.text = items.date
        textSize.text = items.size

        if (items.name.endsWith("png") || items.name.endsWith("PNG")) {
            Picasso.get().load("file:///" + Utils.path + "/" + items.name)
                .placeholder(R.drawable.ic_png_icon)
                .error(R.drawable.ic_png_icon).into(iconFile)
        }
        if (items.name.endsWith("svg") || items.name.endsWith("SVG")) {
            Picasso.get().load("file:///" + Utils.path + "/" + items.name)
                .placeholder(R.drawable.ic_svg_icon)
                .error(R.drawable.ic_svg_icon).into(iconFile)
        }
    }
}