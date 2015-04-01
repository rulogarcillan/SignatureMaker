package com.signaturemaker.app.Nucleo;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.listeners.ActionClickListener;
import com.nispok.snackbar.listeners.EventListener;
import com.signaturemaker.app.Ficheros.Ficheros;
import com.signaturemaker.app.R;
import com.signaturemaker.app.SwipeRecycler.SwipeableRecyclerViewTouchListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import static com.signaturemaker.app.Constantes.PreferencesCons.ROOT;
import static com.signaturemaker.app.Constantes.PreferencesCons.pathFiles;

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
    private FrameLayout frame;
    private ImageView image;
    private Boolean fVolver = true;

    public ListadoFiles() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_list, container, false);

        path = (TextView) rootView.findViewById(R.id.path);
        mensajeVacio = (TextView) rootView.findViewById(R.id.txtMnsVacio);
        frame = (FrameLayout) rootView.findViewById(R.id.frame);
        image = (ImageView) rootView.findViewById(R.id.image);
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
        if (!items.isEmpty())
            mensajeVacio.setVisibility(View.INVISIBLE);

        path.setText(pathFiles.replace(ROOT, "/sdcard"));

    }


    public void carga() {

        items = Ficheros.cargaItems();

        adapter.setItems(items);
        adapter.notifyDataSetChanged();
        if (!items.isEmpty())
            mensajeVacio.setVisibility(View.INVISIBLE);
        else
            mensajeVacio.setVisibility(View.VISIBLE);

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

                if (frame.getVisibility() == View.INVISIBLE) {
                    fVolver = true;
                    frame.setVisibility(View.VISIBLE);
                    Picasso.with(getActivity()).load("file:///" + pathFiles + "/" + items.get(i).getNombre()).placeholder(R.drawable.ic_png)
                            .error(R.drawable.ic_png).into(image);
                }
            }
        });
    }


    private void undo(final ItemFile item, final int pos) {

        SnackbarManager.show(
                Snackbar.with(getActivity()).text(item.getNombre() + " " + getResources().getString(R.string.eliminado)).actionLabel(R.string.deshacer).actionLabelTypeface(Typeface.DEFAULT_BOLD).actionColorResource(R.color.primary).actionListener(new ActionClickListener() {
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

                        if (eliminar) Ficheros.removeFile(item.getNombre());


                    }

                    @Override
                    public void onDismissByReplace(Snackbar snackbar) {
                        if (eliminar) Ficheros.removeFile(item.getNombre());

                    }

                    @Override
                    public void onDismissed(Snackbar snackbar) {
                        if (eliminar) Ficheros.removeFile(item.getNombre());

                    }
                }), getActivity());
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.help_menu, menu);
        MenuItem item = menu.findItem(R.id.action_help);

        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {

                dialogAyuda();
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

        ImageView image = (ImageView) contentView.findViewById(R.id.helpDelete);
        final AnimationDrawable animation = (AnimationDrawable) image.getDrawable();

        ImageView image2 = (ImageView) contentView.findViewById(R.id.helpSend);
        final AnimationDrawable animation2 = (AnimationDrawable) image2.getDrawable();
        dialog.setCancelable(true);

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                animation.start();
                animation2.start();
            }
        });
        dialog.show();

    }

    public Boolean volver() {

        if (fVolver) {
            frame.setVisibility(View.INVISIBLE);
            fVolver = false;
            return true;
        }
        return false;
    }

}