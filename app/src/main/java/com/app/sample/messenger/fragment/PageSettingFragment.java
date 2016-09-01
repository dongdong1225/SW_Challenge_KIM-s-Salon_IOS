package com.app.sample.messenger.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.app.sample.messenger.ActivityChatDetails;
import com.app.sample.messenger.ActivityMain;
import com.app.sample.messenger.R;
import com.app.sample.messenger.adapter.ChatsListAdapter;
import com.app.sample.messenger.data.Constant;
import com.app.sample.messenger.model.Chat;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class PageSettingFragment extends Fragment {



//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        view = inflater.inflate(R.layout.page_fragment_recent, container, false);
//        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
//
//        // use a linear layout manager
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        recyclerView.setHasFixedSize(true);
//
//        items = Constant.getChatsData(getActivity());
//
//        // specify an adapter (see also next example)
//        mAdapter = new ChatsListAdapter(getActivity(), items);
//        recyclerView.setAdapter(mAdapter);
//        mAdapter.setOnItemClickListener(new ChatsListAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View v, Chat obj, int position) {
//                ActivityChatDetails.navigate((ActivityMain) getActivity(), v.findViewById(R.id.lyt_parent), obj.getFriend(), obj.getSnippet());
//            }
//        });

//        view = inflater.inflate(R.layout.mapview_fragment, container, false);
//        MapView mapview=(MapView)view.findViewById(R.id.map);
//        mapview.onCreate(savedInstanceState);
//        mapview.onResume();
//        mapview.getMapAsync(this);

//        return view;
//    }





}
