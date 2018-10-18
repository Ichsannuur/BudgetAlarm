package com.fatiha.ichsannuur.budgetalarm.Templates;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fatiha.ichsannuur.budgetalarm.Adapter.AlarmCancelListAdapter;
import com.fatiha.ichsannuur.budgetalarm.Adapter.AlarmSuccessListAdapter;
import com.fatiha.ichsannuur.budgetalarm.Connection.DB_Helper;
import com.fatiha.ichsannuur.budgetalarm.Object.Alarm;
import com.fatiha.ichsannuur.budgetalarm.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlarmCancelList extends Fragment {

    DB_Helper database;
    List<Alarm> alarmList = new ArrayList<>();
    RecyclerView recyclerView;
    ProgressDialog dialog;
    AlarmCancelListAdapter adapter;

    public AlarmCancelList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        dialog = new ProgressDialog(getContext());
        dialog.setTitle("Loading");
        dialog.show();
        View view = inflater.inflate(R.layout.fragment_alarm_success_list, container, false);
        database = new DB_Helper(getContext());
        alarmList = database.getdataOrderByCancel();
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        loadData();
        return view;
    }

    private void loadData() {
        adapter = new AlarmCancelListAdapter(getContext(),alarmList);
        recyclerView.setAdapter(adapter);
        dialog.dismiss();
    }

}
