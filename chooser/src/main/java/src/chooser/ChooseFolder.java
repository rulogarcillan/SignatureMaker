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
package src.chooser;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rulo on 17/03/15.
 */
public class ChooseFolder extends LinearLayout {

    private List<String> items = new ArrayList<>();

    private RecyclerView list;

    private AdapterExplorer mAdapter;

    private Context mContext;

    private String path = Files.root;

    private TextView txtPath;

    public ChooseFolder(Context mContext) {
        super(mContext);
        this.mContext = mContext;
        Initialize();
    }

    public ChooseFolder(Context mContext, AttributeSet attrs) {
        super(mContext, attrs);
        this.mContext = mContext;
        Initialize();
    }

    public ChooseFolder(Context mContext, AttributeSet attrs, int defStyleAttr) {
        super(mContext, attrs, defStyleAttr);
        this.mContext = mContext;
        Initialize();
    }

    public void addFolder(String name) {
        Files.addFolder(genPathClickFolder(name));
        this.items.add(1, name);
        this.mAdapter.setItems(this.items);
        this.mAdapter.notifyDataSetChanged();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
        this.txtPath.setText(this.path.replace(Files.root, "/sdcard"));
        this.items = Files.loadItems(this.path);
        this.mAdapter.setItems(this.items);
        this.mAdapter.notifyDataSetChanged();
    }

    private void Initialize() {

        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater cf = (LayoutInflater) getContext().getSystemService(infService);
        cf.inflate(R.layout.expl, this, true);

        this.txtPath = (TextView) findViewById(R.id.path);
        this.list = (RecyclerView) findViewById(R.id.listadoFolder);
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);

        this.txtPath.setText(this.path.replace(Files.root, "/sdcard"));
        this.items = Files.loadItems(this.path);
        this.mAdapter = new AdapterExplorer(items);

        list.setLayoutManager(layoutManager);
        list.setItemAnimator(new DefaultItemAnimator());
        list.setHasFixedSize(true);
        list.setAdapter(mAdapter);
        mAdapter.SetOnItemClickListener((view, position) -> {
            if (position == 0) {
                setPath(Files.getPathPreviousFolder(path));
            } else {
                setPath(genPathClickFolder(items.get(position)));
            }
        });

    }

    private String genPathClickFolder(String nameFolder) {
        return this.getPath() + "/" + nameFolder + "/";
    }

}
