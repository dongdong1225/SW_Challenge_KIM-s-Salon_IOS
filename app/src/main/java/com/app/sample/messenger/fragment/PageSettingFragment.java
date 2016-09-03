package com.app.sample.messenger.fragment;

import android.graphics.Color;
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
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.app.sample.messenger.ActivityChatDetails;
import com.app.sample.messenger.ActivityMain;
import com.app.sample.messenger.R;
import com.app.sample.messenger.adapter.CallListAdapter;
import com.app.sample.messenger.adapter.ChatsListAdapter;
import com.app.sample.messenger.adapter.GroupsGridAdapter;
import com.app.sample.messenger.data.Constant;
import com.app.sample.messenger.model.Chat;
import com.app.sample.messenger.model.Group;
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

    private View view;

    private RecyclerView recyclerView;
    private List<Group> items = new ArrayList<>();
    private GroupsGridAdapter mAdapter;
    private Color mColor;

    private boolean Emergency ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.page_fragment_recent, container, false);

        mColor = new Color();
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(500); //You can manage the time of the blink with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        anim.setBackgroundColor(0);
        view.findViewById(R.id.sprayImage).startAnimation(anim);
//        myText.startAnimation(anim);
        Emergency = false;

        return view;
    }

    public void actionClick(View v)
    {

        Button emrgencyButton = (Button) view.findViewById(R.id.emergency_button);
        if(Emergency == false) {
            emrgencyButton.setText("신고 취소하기");
//            Animation anim = new AlphaAnimation(0.0f, 1.0f);
//            anim.setDuration(1000); //You can manage the time of the blink with this parameter
//            anim.setStartOffset(20);
//            anim.setRepeatMode(Animation.REVERSE);
//            anim.setRepeatCount(Animation.INFINITE);
//            anim.setBackgroundColor(0);
//            emrgencyButton.startAnimation(anim);

            emrgencyButton.setBackgroundColor(Color.LTGRAY);
            Emergency = true;
        }
        else
        {
            emrgencyButton.setText("신고하기");
            emrgencyButton.clearAnimation();
            //mColor = view;
            emrgencyButton.setBackgroundResource(R.color.colorPrimary);
            Emergency = false;
        }

    }






}
