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
package com.signaturemaker.app.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.signaturemaker.app.R;
import com.signaturemaker.app.models.ItemFile;
import com.signaturemaker.app.utils.Constants;
import com.signaturemaker.app.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class AdapterFiles extends RecyclerView.Adapter<AdapterFiles.ViewHolder> {

    private List<ItemFile> items = new ArrayList<>();
    private Activity activity;
    private int viewType;
    OnItemClickListener mItemClickListener;

    public AdapterFiles(Activity activity, List<ItemFile> items, int viewType) {

        this.items = items;
        this.activity = activity;
        this.viewType = viewType;

    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public List<ItemFile> getItems() {
        return items;
    }

    public void setItems(List<ItemFile> items) {
        this.items = items;
    }


    @Override
    public int getItemCount() {
        return items.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        ImageView iconFile;
        TextView textName, textDate, textTam;
        public RelativeLayout viewBackground, viewForeground;

        public ViewHolder(View container) {
            super(container);
            iconFile = (ImageView) container.findViewById(R.id.imgSign);
            textName = (TextView) container.findViewById(R.id.textName);
            textDate = (TextView) container.findViewById(R.id.textDate);
            textTam = (TextView) container.findViewById(R.id.textSize);
            viewBackground = container.findViewById(R.id.view_background);
            viewForeground = container.findViewById(R.id.view_foreground);
            container.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition());
            }
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }


    @Override
    public AdapterFiles.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_explore, parent, false);
        ViewHolder vhItem = new ViewHolder(v);
        return vhItem;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {

        viewHolder.textName.setText(items.get(i).getName().substring(3));
        viewHolder.textDate.setText(items.get(i).getDate());
        viewHolder.textTam.setText(items.get(i).getSize());
        if (items.get(i).getName().endsWith("png") || items.get(i).getName().endsWith("PNG")){
            Picasso.get().load("file:///" + Utils.path + "/" + items.get(i).getName()).placeholder(R.drawable.ic_png)
                    .error(R.drawable.ic_png).into(viewHolder.iconFile);
        }
        if (items.get(i).getName().endsWith("svg") || items.get(i).getName().endsWith("SVG")){
            Picasso.get().load("file:///" + Utils.path + "/" + items.get(i).getName()).placeholder(R.drawable.ic_svg_icon)
                    .error(R.drawable.ic_svg_icon).into(viewHolder.iconFile);
        }

    }

}