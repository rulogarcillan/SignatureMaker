package com.signaturemaker.app.Nucleo;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.signaturemaker.app.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.signaturemaker.app.Constantes.PreferencesCons.pathFiles;

public class AdapterFicheros extends RecyclerView.Adapter<AdapterFicheros.ViewHolder> {

    private ArrayList<ItemFile> items = new ArrayList<>();
    private Activity activity;
    private int viewType;
    OnItemClickListener mItemClickListener;

    public AdapterFicheros(Activity activity, ArrayList<ItemFile> items, int viewType) {

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

    public ArrayList<ItemFile> getItems() {
        return items;
    }

    public void setItems(ArrayList<ItemFile> items) {
        this.items = items;
    }


    @Override
    public int getItemCount() {
        return items.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        ImageView iconFile;
        TextView textName, textDate, textTam;


        public ViewHolder(View container) {
            super(container);

            if (viewType == ItemFile.TYPE_LARGE) {

                iconFile = (ImageView) container.findViewById(R.id.imgFirma);
                textName = (TextView) container.findViewById(R.id.textName);
                textDate = (TextView) container.findViewById(R.id.textDate);
                textTam = (TextView) container.findViewById(R.id.textTam);

                container.setOnClickListener(this);

            } else if (viewType == ItemFile.TYPE_SHORT) {


            }


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
    public AdapterFicheros.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == ItemFile.TYPE_LARGE) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_explore_l, parent, false);
            ViewHolder vhItem = new ViewHolder(v);

            return vhItem;

        } else if (viewType == ItemFile.TYPE_SHORT) {

        }
        return null;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {

        if (viewType == ItemFile.TYPE_LARGE) {

            viewHolder.textName.setText(items.get(i).getNombre().substring(3));
            viewHolder.textDate.setText(items.get(i).getFecha());
            viewHolder.textTam.setText(items.get(i).getTamaño());
            Picasso.with(activity).load("file:///" + pathFiles + "/" + items.get(i).getNombre()).placeholder(R.drawable.ic_png)
                    .error(R.drawable.ic_png).into(viewHolder.iconFile);


        } else if (viewType == ItemFile.TYPE_SHORT) {


        }


    }


}