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
package com.signaturemaker.app.application.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import co.dift.ui.SwipeToAction;
import com.signaturemaker.app.R;
import com.signaturemaker.app.application.utils.Utils;
import com.signaturemaker.app.domain.models.ItemFile;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class AdapterFiles extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public class ViewHolder extends SwipeToAction.ViewHolder<ItemFile> {

        ImageView iconFile;

        TextView textName, textDate, textTam;

        public ViewHolder(View container) {
            super(container);
            iconFile = container.findViewById(R.id.imgSign);
            textName = container.findViewById(R.id.textName);
            textDate = container.findViewById(R.id.textDate);
            textTam = container.findViewById(R.id.textSize);
        }
    }

    private Activity activity;

    private List<ItemFile> items = new ArrayList<>();


    public AdapterFiles(Activity activity, List<ItemFile> items, int viewType) {

        this.items = items;
        this.activity = activity;


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public List<ItemFile> getItems() {
        return items;
    }

    public void setItems(List<ItemFile> items) {
        this.items = items;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {

        ItemFile item = items.get(i);
        ViewHolder vh = (ViewHolder) viewHolder;

        vh.textName.setText(items.get(i).getName().substring(3));
        vh.textDate.setText(items.get(i).getDate());
        vh.textTam.setText(items.get(i).getSize());
        if (items.get(i).getName().endsWith("png") || items.get(i).getName().endsWith("PNG")) {
            Picasso.get().load("file:///" + Utils.path + "/" + items.get(i).getName())
                    .placeholder(R.drawable.ic_png_icon)
                    .error(R.drawable.ic_png_icon).into(vh.iconFile);
        }
        if (items.get(i).getName().endsWith("svg") || items.get(i).getName().endsWith("SVG")) {
            Picasso.get().load("file:///" + Utils.path + "/" + items.get(i).getName())
                    .placeholder(R.drawable.ic_svg_icon)
                    .error(R.drawable.ic_svg_icon).into(vh.iconFile);
        }

        vh.data = item;

    }

    @Override
    public AdapterFiles.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_explore, parent, false);
        ViewHolder vhItem = new ViewHolder(v);
        return vhItem;
    }

}