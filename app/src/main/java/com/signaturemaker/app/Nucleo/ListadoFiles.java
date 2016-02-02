package com.signaturemaker.app.Nucleo;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.listeners.ActionClickListener;
import com.nispok.snackbar.listeners.EventListener;
import com.signaturemaker.app.Constantes.PreferencesCons;
import com.signaturemaker.app.Ficheros.Ficheros;
import com.signaturemaker.app.R;
import com.signaturemaker.app.SwipeRecycler.SwipeableRecyclerViewTouchListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import static com.signaturemaker.app.Constantes.PreferencesCons.OP9;
import static com.signaturemaker.app.Constantes.PreferencesCons.ROOT;
import static com.signaturemaker.app.Constantes.PreferencesCons.pathFiles;
import static com.signaturemaker.app.Constantes.PreferencesCons.sort;
import static com.signaturemaker.app.Nucleo.LogUtils.TRAZA;

/**
 * A placeholder fragment containing a simple view.
 */
public class ListadoFiles extends Fragment {
    private LinearLayout layoutColor;
    private View rootView;
    private ArrayList<ItemFile> items = new ArrayList<>();

    private RecyclerView recyclerView;
    private TextView mensajeVacio, path;
    private AdapterFicheros adapter;
    private Boolean eliminar = true;

    private ImageView image;

    private SharedPreferences prefs;
    private int prefSort;
    private SharedPreferences.Editor editor;


    public ListadoFiles() {
    }


    public void recuperaPref() {

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = prefs.edit();
        sort = prefs.getInt(OP9, sort);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_list, container, false);

        recuperaPref();

        path = (TextView) rootView.findViewById(R.id.path);
        mensajeVacio = (TextView) rootView.findViewById(R.id.txtMnsVacio);


        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);

        adapter = new AdapterFicheros(getActivity(), items, 0); //Agregamos los items al adapter


        carga();
        //definimos el recycler y agregamos el adaptaer
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        clickItem();


        SwipeableRecyclerViewTouchListener swipeTouchListener =
                new SwipeableRecyclerViewTouchListener(recyclerView,
                        new SwipeableRecyclerViewTouchListener.SwipeListener() {
                            @Override
                            public boolean canSwipe(int position) {
                                return true;
                            }

                            @Override
                            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    eliminar = true;
                                    undo(items.get(position), position);
                                    items.remove(position);
                                    adapter.notifyItemRemoved(position);
                                }
                                adapter.notifyDataSetChanged();
                                if (!items.isEmpty())
                                    mensajeVacio.setVisibility(View.INVISIBLE);
                                else
                                    mensajeVacio.setVisibility(View.VISIBLE);

                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    export(items.get(position));

                                }

                            }
                        });

        recyclerView.addOnItemTouchListener(swipeTouchListener);


        if (!items.isEmpty())
            mensajeVacio.setVisibility(View.INVISIBLE);

        setHasOptionsMenu(true);

        return rootView;

    }

    @Override
    public void onResume() {
        super.onResume();

        carga();
        recuperaPref();
        if (!items.isEmpty())
            mensajeVacio.setVisibility(View.INVISIBLE);

        path.setText(pathFiles.replace(ROOT, "/sdcard"));


        getView().setFocusableInTouchMode(true);
        getView().requestFocus();

        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {

                        if (SnackbarManager.getCurrentSnackbar() != null && SnackbarManager.getCurrentSnackbar().isShowing()) {
                            SnackbarManager.getCurrentSnackbar().dismiss();
                            return true;
                        } else
                            return false;
                    }
                }
                return false;
            }
        });
        ordena(items, sort);
        recyclerView.getAdapter().notifyDataSetChanged();
    }


    public void carga() {

        items = Ficheros.cargaItems();
        ordena(items, sort);

        adapter.setItems(items);
        adapter.notifyDataSetChanged();
        if (!items.isEmpty())
            mensajeVacio.setVisibility(View.INVISIBLE);
        else
            mensajeVacio.setVisibility(View.VISIBLE);

     /*   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.READ_CONTACTS)
                    != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        Manifest.permission.READ_CONTACTS)) {



                } else {

                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE}, 5);
                }
            }
        }*/
    }


    private void ordena(ArrayList<ItemFile> lista, final int tipo) {
        Collections.sort(lista, new Comparator<ItemFile>() {
            @Override
            public int compare(ItemFile lhs, ItemFile rhs) {

                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                String date1 = lhs.getFecha();
                String date2 = rhs.getFecha();
                Date ddate = null;
                Date ddate2 = null;
                try {

                    ddate = formatter.parse(date1);
                    ddate2 = formatter.parse(date2);


                } catch (ParseException e) {
                    e.printStackTrace();
                }


                return ddate.compareTo(ddate2) * tipo;
            }
        });
    }


    private void export(ItemFile item) {

        File file = Ficheros.getFile(item.getNombre());

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        shareIntent.setType("image/*");
        getActivity().startActivity(Intent.createChooser(shareIntent, getActivity().getText(R.string.enviarsolo)));
    }


    private void clickItem() {


        adapter.SetOnItemClickListener(new AdapterFicheros.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int i) {


                MaterialDialog dia = new MaterialDialog.Builder(getActivity())

                        .customView(R.layout.imagen_dialog, false)
                        .show();

                image = (ImageView) dia.getCustomView().findViewById(R.id.image);

                Picasso.with(getActivity()).load("file:///" + pathFiles + "/" + items.get(i).getNombre()).placeholder(R.drawable.ic_png)
                        .error(R.drawable.ic_png).into(image);


            }
        });
    }


    private void undo(final ItemFile item, final int pos) {

        if (SnackbarManager.getCurrentSnackbar() != null && SnackbarManager.getCurrentSnackbar().isShowing())
            SnackbarManager.getCurrentSnackbar().dismiss();

        SnackbarManager.show(
                Snackbar.with(getActivity()).text(item.getNombre().substring(3) + " " + getResources().getString(R.string.eliminado)).actionLabel(R.string.deshacer).actionLabelTypeface(Typeface.DEFAULT_BOLD).actionColorResource(R.color.primary).actionListener(new ActionClickListener() {
                    @Override
                    public void onActionClicked(Snackbar snackbar) {

                        eliminar = false;
                        items.add(pos, item);
                        adapter.notifyItemInserted(pos);
                        adapter.notifyDataSetChanged();
                        if (!items.isEmpty())
                            mensajeVacio.setVisibility(View.INVISIBLE);
                        else
                            mensajeVacio.setVisibility(View.VISIBLE);


                    }
                }).eventListener(new EventListener() {
                    @Override
                    public void onShow(Snackbar snackbar) {

                    }

                    @Override
                    public void onShowByReplace(Snackbar snackbar) {

                    }

                    @Override
                    public void onShown(Snackbar snackbar) {

                    }

                    @Override
                    public void onDismiss(Snackbar snackbar) {

                        if (eliminar) {

                            Ficheros.removeFile(item.getNombre());
                            TRAZA(Uri.fromFile(new File(pathFiles + "/" + item.getNombre())) + "");
                            getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(pathFiles + "/" + item.getNombre()))));

                        }


                    }

                    @Override
                    public void onDismissByReplace(Snackbar snackbar) {
                        if (eliminar) {
                            Ficheros.removeFile(item.getNombre());
                            getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(pathFiles + "/" + item.getNombre()))));
                        }

                    }

                    @Override
                    public void onDismissed(Snackbar snackbar) {
                        if (eliminar) {
                            Ficheros.removeFile(item.getNombre());
                            getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(pathFiles + "/" + item.getNombre()))));
                        }

                    }
                }), getActivity());
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.help_menu, menu);
        MenuItem item = menu.findItem(R.id.action_help);
        MenuItem itemS = menu.findItem(R.id.action_sort);

        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_help:
                        dialogAyuda();
                        break;

                }

                return true;
            }
        });

        itemS.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.action_sort:
                        sort = sort * -1;
                        editor.putInt(PreferencesCons.OP9, sort);
                        editor.commit();
                        ordena(items, sort);
                        recyclerView.getAdapter().notifyDataSetChanged();

                        break;
                }

                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }


    private void dialogAyuda() {

        Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        LinearLayout contentView = (LinearLayout) ((getActivity()))
                .getLayoutInflater().inflate(R.layout.help, null);
        dialog.setContentView(contentView);


        dialog.show();

    }

    @Override
    public void onDestroy() {
        eliminar = false;
        super.onDestroy();
    }

  /*  @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 5: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    items = Ficheros.cargaItems();
                    ordena(items, sort);

                    adapter.setItems(items);
                    adapter.notifyDataSetChanged();
                    if (!items.isEmpty())
                        mensajeVacio.setVisibility(View.INVISIBLE);
                    else
                        mensajeVacio.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getActivity(),getString(R.string.permisos),Toast.LENGTH_LONG).show();
                    getActivity().finish();
                }
                return;
            }
        }
    }*/
}
