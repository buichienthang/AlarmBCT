package com.github.ppartisan.simplealarms.ui.fragment;

import static com.github.ppartisan.simplealarms.ui.AddEditAlarmActivity.ADD_ALARM;
import static com.github.ppartisan.simplealarms.ui.AddEditAlarmActivity.buildAddEditAlarmActivityIntent;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.github.ppartisan.simplealarms.R;
import com.github.ppartisan.simplealarms.adapter.AlarmsAdapter;
import com.github.ppartisan.simplealarms.model.Alarm;
import com.github.ppartisan.simplealarms.service.LoadAlarmsReceiver;
import com.github.ppartisan.simplealarms.service.LoadAlarmsService;
import com.github.ppartisan.simplealarms.util.AlarmUtils;
import com.github.ppartisan.simplealarms.view.DividerItemDecoration;
import com.github.ppartisan.simplealarms.view.EmptyRecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class FragmentAlarm extends Fragment implements LoadAlarmsReceiver.OnAlarmsLoadedListener {

    private AlarmsAdapter mAdapter;
    private LoadAlarmsReceiver mReceiver;
    private FloatingActionButton fab;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mReceiver = new LoadAlarmsReceiver(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_alarm,container,false);

        final EmptyRecyclerView rv = v.findViewById(R.id.recycler);
        mAdapter = new AlarmsAdapter();
        rv.setEmptyView(v.findViewById(R.id.empty_view));
        rv.setAdapter(mAdapter);
        rv.addItemDecoration(new DividerItemDecoration(getContext()));
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setItemAnimator(new DefaultItemAnimator());

        fab = v.findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            AlarmUtils.checkAlarmPermissions(getActivity());
            final Intent i = buildAddEditAlarmActivityIntent(getContext(), ADD_ALARM);
            startActivity(i);
        });

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        final IntentFilter filter = new IntentFilter(LoadAlarmsService.ACTION_COMPLETE);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mReceiver, filter);
        LoadAlarmsService.launchLoadAlarmsService(getContext());
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mReceiver);
    }

    @Override
    public void onAlarmsLoaded(ArrayList<Alarm> alarms) {
        mAdapter.setAlarms(alarms);
    }
}
