package com.app.sample.messenger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.app.sample.messenger.adapter.PageFragmentAdapter;
import com.app.sample.messenger.data.Tools;
import com.app.sample.messenger.fragment.PageMapFragment;
import com.app.sample.messenger.fragment.PageServicesFragment;
import com.app.sample.messenger.fragment.PageEmergencyContactsFragment;
import com.app.sample.messenger.fragment.PageSettingFragment;
import com.app.sample.messenger.fragment.PageUserInfoFragment;
import com.app.sample.messenger.gcm.QuickstartPreferences;
import com.app.sample.messenger.gcm.RegistrationIntentService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class ActivityMain extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private View parent_view;
    private FloatingActionButton fab;

    private PageFragmentAdapter adapter;

    private PageSettingFragment f_recent;
    private PageMapFragment f_call;
    private PageEmergencyContactsFragment f_group;
    private PageServicesFragment f_friend;
    private PageUserInfoFragment f_setting;
    private static int[] imageResId = {
            R.drawable.ic_tab_recent,
            R.drawable.ic_tab_call,
            R.drawable.ic_tab_group,
            R.drawable.ic_tab_friends,
            R.drawable.ic_tab_setting
    };

    //이하 영마가 추가
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";

    //private Button mRegistrationButton;
    //private ProgressBar mRegistrationProgressBar;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    //private EditText mInformationTextView;

    /**
     * Instance ID를 이용하여 디바이스 토큰을 가져오는 RegistrationIntentService를 실행한다.
     */
    public void getInstanceIdToken() {
        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }

    /**
     * LocalBroadcast 리시버를 정의한다. 토큰을 획득하기 위한 READY, GENERATING, COMPLETE 액션에 따라 UI에 변화를 준다.
     */
    public void registBroadcastReceiver(){
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();


                if(action.equals(QuickstartPreferences.REGISTRATION_READY)){
                    // 액션이 READY일 경우
                    //mRegistrationProgressBar.setVisibility(ProgressBar.GONE);
                    //mInformationTextView.setVisibility(View.GONE);
                } else if(action.equals(QuickstartPreferences.REGISTRATION_GENERATING)){
                    // 액션이 GENERATING일 경우
                    //mRegistrationProgressBar.setVisibility(ProgressBar.VISIBLE);
                    //mInformationTextView.setVisibility(View.VISIBLE);
                    //mInformationTextView.setText(getString(R.string.registering_message_generating));
                } else if(action.equals(QuickstartPreferences.REGISTRATION_COMPLETE)){
                    // 액션이 COMPLETE일 경우
                    //mRegistrationProgressBar.setVisibility(ProgressBar.GONE);
                    //mRegistrationButton.setText(getString(R.string.registering_message_complete));
                    //mRegistrationButton.setEnabled(false);
                    //String token = intent.getStringExtra("token");
                    //mInformationTextView.setText(token);
                }

            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registBroadcastReceiver();
        getInstanceIdToken();

        parent_view = findViewById(R.id.viewpager);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        setupTabClick();
        onFabClick();

        // for system bar in lollipop
        Tools.systemBarLolipop(this);
    }

    /**
     * 앱이 실행되어 화면에 나타날때 LocalBoardcastManager에 액션을 정의하여 등록한다.
     */
    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_READY));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_GENERATING));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));

    }

    /**
     * 앱이 화면에서 사라지면 등록된 LocalBoardcast를 모두 삭제한다.
     */
    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }


    /**
     * Google Play Service를 사용할 수 있는 환경이지를 체크한다.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }





    private void onFabClick() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = viewPager.getCurrentItem();
                switch (index) {
                    case 0:
                        Snackbar.make(parent_view, "New Chat Clicked", Snackbar.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Snackbar.make(parent_view, "New Call Clicked", Snackbar.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Snackbar.make(parent_view, "Add Group Clicked", Snackbar.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Snackbar.make(parent_view, "Add Friend Clicked", Snackbar.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }


    private void setupViewPager(ViewPager viewPager) {
        adapter = new PageFragmentAdapter(getSupportFragmentManager());
        if (f_recent == null) {
            f_recent = new PageSettingFragment();
        }
        if (f_call == null) {
            f_call = new PageMapFragment();
        }
        if (f_group== null) {
            f_group = new PageEmergencyContactsFragment();
        }
        if (f_friend == null) {
            f_friend = new PageServicesFragment();
        }
        if (f_setting == null) {
            f_setting = new PageUserInfoFragment();
        }
        adapter.addFragment(f_recent, getString(R.string.tab_recent));
        adapter.addFragment(f_call, getString(R.string.tab_call));
        adapter.addFragment(f_group, getString(R.string.tab_group));
        adapter.addFragment(f_friend, getString(R.string.tab_friend));
        adapter.addFragment(f_setting, getString(R.string.tab_setting));
        viewPager.setAdapter(adapter);
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(imageResId[0]);
        tabLayout.getTabAt(1).setIcon(imageResId[1]);
        tabLayout.getTabAt(2).setIcon(imageResId[2]);
        tabLayout.getTabAt(3).setIcon(imageResId[3]);
        tabLayout.getTabAt(4).setIcon(imageResId[4]);
    }

    private void setupTabClick() {
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if(position == 4){//tab setting
                    fab.hide();
                }else{
                    fab.show();
                }
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    public void actionClick(View v) {
        f_setting.actionClick(v);
    }
}
