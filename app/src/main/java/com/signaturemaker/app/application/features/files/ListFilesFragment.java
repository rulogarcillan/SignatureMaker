package com.signaturemaker.app.application.features.files;/*
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


import static com.signaturemaker.app.application.core.extensions.Utils.createSnackbar;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import co.dift.ui.SwipeToAction;
import com.google.android.material.snackbar.Snackbar;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.signaturemaker.app.R;
import com.signaturemaker.app.application.core.extensions.Utils;
import com.signaturemaker.app.application.core.platform.FilesUtils;
import com.signaturemaker.app.application.core.platform.PermissionsUtils;
import com.signaturemaker.app.application.utils.Constants;
import com.signaturemaker.app.domain.models.ItemFile;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class ListFilesFragment extends Fragment {


    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }

    private AdapterFiles adapter;

    private Boolean flagDelete = true;

    private List<ItemFile> items = new ArrayList<>();

    private OnFragmentInteractionListener mListener;

    private Snackbar mySnackbar;

    // @BindView(R.id.path)
    private TextView path;

    //@BindView(R.id.recycler_view)
    private RecyclerView recyclerView;

    private View rootView;

    //@BindView(R.id.txtMnsNoFiles)
    private TextView txtMnsNoFiles;


    public ListFilesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.list_files_fragment, container, false);
        //ButterKnife.bind(this, rootView);

        path = rootView.findViewById(R.id.path);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        txtMnsNoFiles = rootView.findViewById(R.id.txtMnsNoFiles);
        setHasOptionsMenu(true);

        loadItemsFiles();
        adapter = new AdapterFiles(items); //Agregamos los items al adapter

        //definimos el recycler y agregamos el adaptaer
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(1,
                StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        final SwipeToAction swipeToAction = new SwipeToAction(recyclerView,
                new SwipeToAction.SwipeListener<ItemFile>() {

                    @Override
                    public void onClick(ItemFile itemData) {

                        showPreviewImage(itemData);
                    }

                    @Override
                    public void onLongClick(ItemFile itemData) {

                    }

                    @Override
                    public boolean swipeLeft(final ItemFile itemData) {

                        final int pos = adapter.getItems().indexOf(itemData);
                        if (pos == -1) {
                            return true;
                        }
                        removeItemAdapter(itemData);

                        if (getActivity() != null) {
                            mySnackbar = createSnackbar(getActivity(), itemData.getName(),
                                    getResources().getString(R.string.tittle_undo), new Snackbar.Callback() {
                                        @Override
                                        public void onDismissed(Snackbar transientBottomBar, int event) {
                                            if (event != 1) {
                                                if (getActivity() != null) {
                                                    FilesUtils.removeFile(getActivity(), itemData.getName());
                                                    if (items.size() > 0) {
                                                        txtMnsNoFiles.setVisibility(View.GONE);
                                                    } else {
                                                        txtMnsNoFiles.setVisibility(View.VISIBLE);
                                                    }
                                                    loadItemsFiles();
                                                    Utils.sort(items, Utils.sortOrder);
                                                }
                                            } else {
                                                addItemAdapter(pos, itemData);
                                                loadItemsFiles();
                                                Utils.sort(items, Utils.sortOrder);
                                                // adapter.setItems(items);
                                                //adapter.notifyDataSetChanged();

                                            }
                                            super.onDismissed(transientBottomBar, event);
                                        }

                                        @Override
                                        public void onShown(Snackbar sb) {
                                            super.onShown(sb);
                                        }
                                    });
                            mySnackbar.show();
                        }
                        return true;
                    }

                    @Override
                    public boolean swipeRight(ItemFile itemData) {
                        Utils.shareSign(getActivity(), itemData.getName());
                        return true;
                    }
                });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        reloadFiles();
        path.setText(Utils.path.replace(Constants.ROOT, "/sdcard"));
    }

    @Override
    public void onDestroyView() {
        if (mySnackbar != null && mySnackbar.isShown()) {
            mySnackbar.dismiss();
        }
        super.onDestroyView();
    }

    public void loadItemsFiles() {

        PermissionsUtils.getInstance().callRequestPermissionWrite(getActivity(), new PermissionListener() {
            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
            }

            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                items = FilesUtils.loadItemsFiles();
                if (items.size() > 0) {
                    txtMnsNoFiles.setVisibility(View.GONE);
                } else {
                    txtMnsNoFiles.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.help_menu, menu);

        // MenuItem item = menu.findItem(R.id.action_help);
        MenuItem itemS = menu.findItem(R.id.action_sort);

    /*    item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_help:
                }
                return true;
            }
        });*/

        itemS.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_sort) {
                Utils.sortOrder = Utils.sortOrder * -1;
                synchronized (items) {
                    Utils.sort(items, Utils.sortOrder);
                }
                recyclerView.getAdapter().notifyDataSetChanged();
            }
            return true;
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    public void reloadFiles() {
        loadItemsFiles();
        adapter.notifyDataSetChanged();
    }

    private void addItemAdapter(int pos, final ItemFile item) {
        adapter.getItems().add(pos, item);
        adapter.notifyItemInserted(pos);
    }

    private int removeItemAdapter(final ItemFile item) {
        int pos = adapter.getItems().indexOf(item);
        Log.d(Constants.TAG, item.getName());

        adapter.getItems().remove(item);
        adapter.notifyItemRemoved(pos);
        return pos;
    }

    private void showPreviewImage(ItemFile itemData) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        //alertDialog.setTitle(R.string.tittle_name_of_the_file);
        View view = getActivity().getLayoutInflater().inflate(R.layout.imagen_dialog, null);
        final ImageView image = view.findViewById(R.id.image);
        if (itemData.getName().endsWith("png") || itemData.getName().endsWith("PNG")) {
            Picasso.get().load("file:///" + Utils.path + "/" + itemData.getName()).placeholder(R.drawable.ic_png_icon)
                    .error(R.drawable.ic_png_icon).into(image);
        }
        if (itemData.getName().endsWith("svg") || itemData.getName().endsWith("SVG")) {
            Picasso.get().load("file:///" + Utils.path + "/" + itemData.getName()).placeholder(R.drawable.ic_svg_icon)
                    .error(R.drawable.ic_svg_icon).into(image);
        }

        alertDialog.setCancelable(true);
        alertDialog.setView(view);
        alertDialog.show();
    }

}
