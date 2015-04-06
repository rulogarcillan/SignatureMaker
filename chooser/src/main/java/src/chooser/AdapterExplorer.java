package src.chooser;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterExplorer extends RecyclerView.Adapter<AdapterExplorer.ViewHolder> {


    private ArrayList<String> items = new ArrayList<>();

    OnItemClickListener mItemClickListener;


    public AdapterExplorer(ArrayList<String> items) {

        this.items = items;

    }


    public ArrayList<String> getItems() {
        return items;
    }

    public void setItems(ArrayList<String> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name;


        public ViewHolder(View container, int ViewType) {
            super(container);

            name = (TextView) container.findViewById(R.id.textName);
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
    public AdapterExplorer.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_explore, parent, false);
        ViewHolder vhItem = new ViewHolder(v, viewType);

        return vhItem;

    }


    @Override
    public void onBindViewHolder(AdapterExplorer.ViewHolder viewHolder, final int i) {

        viewHolder.name.setText(items.get(i));

    }
}

