package com.samuelbernard147.soag.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.samuelbernard147.soag.R;
import com.samuelbernard147.soag.adapter.HistoryAdapter;
import com.samuelbernard147.soag.loader.HistoryLoader;
import com.samuelbernard147.soag.model.Humidity;
import com.samuelbernard147.soag.preference.TimerPreference;

import java.nio.BufferUnderflowException;
import java.util.ArrayList;

public class BottomSheetFragment extends BottomSheetDialogFragment implements LoaderManager.LoaderCallbacks<ArrayList<Humidity>> {
    HistoryAdapter adapter;
    RecyclerView recyclerView;
    Bundle bundle;
    String tag;

    public BottomSheetFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bottom_sheet, container, false);
        recyclerView = view.findViewById(R.id.rv_history);
        recyclerView.setLayoutManager(new LinearLayoutManager(getDialog().getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new HistoryAdapter();
        recyclerView.setAdapter(adapter);

        bundle = getArguments();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initHistory(bundle.getString("TAG"));
    }

    public void initHistory(String tag) {
        this.tag = tag;
        getLoaderManager().initLoader(0, bundle, BottomSheetFragment.this);
    }

    @NonNull
    @Override
    public Loader<ArrayList<Humidity>> onCreateLoader(int id, Bundle args) {
        return new HistoryLoader(getActivity(), tag);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<Humidity>> loader, ArrayList<Humidity> data) {
        adapter.setData(data);
        stopLoader(0);
    }

    void stopLoader(int id) {
        getLoaderManager().destroyLoader(id);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<Humidity>> loader) {
        adapter.setData(null);
    }
}