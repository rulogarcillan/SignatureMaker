package com.signaturemaker.app.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.signaturemaker.app.R;

import butterknife.ButterKnife;


public class ListFilesFragment extends Fragment {


    private OnFragmentInteractionListener mListener;
    private View rootView;

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
        ButterKnife.bind(this, rootView);
        return rootView;
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
