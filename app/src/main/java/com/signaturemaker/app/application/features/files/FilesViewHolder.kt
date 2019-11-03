package com.signaturemaker.app.application.features.files

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import co.dift.ui.SwipeToAction
import com.signaturemaker.app.R
import com.signaturemaker.app.application.core.extensions.Utils
import com.signaturemaker.app.application.core.extensions.loadFromUrl
import com.signaturemaker.app.domain.models.ItemFile

class FilesViewHolder(rootView: View) : SwipeToAction.ViewHolder<ItemFile>(rootView) {

    private var textName: TextView = rootView.findViewById(R.id.textName)
    private var textDate: TextView = rootView.findViewById(R.id.textDate)
    private var textSize: TextView = rootView.findViewById(R.id.textSize)
    private var iconFile: ImageView = rootView.findViewById(R.id.imgSign)

    fun bind(items: ItemFile) {

        data = items

        textName.text = items.name.substring(3)
        textDate.text = items.date
        textSize.text = items.size

        iconFile.loadFromUrl("file:///" + Utils.path + "/" + items.name)
    }
}