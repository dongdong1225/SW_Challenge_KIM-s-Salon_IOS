package com.app.sample.messenger;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
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
import android.widget.Toast;


import com.app.sample.messenger.adapter.PageFragmentAdapter;
import com.app.sample.messenger.cctv_DB.NotesDbAdapter;
import com.app.sample.messenger.fragment.PageMapFragment;
import com.app.sample.messenger.fragment.PageServicesFragment;
import com.app.sample.messenger.fragment.PageEmergencyContactsFragment;
import com.app.sample.messenger.fragment.PageSettingFragment;
import com.app.sample.messenger.fragment.PageUserInfoFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.app.sample.messenger.gcm.QuickstartPreferences;
import com.app.sample.messenger.gcm.RegistrationIntentService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.io.InputStream;

import java.util.ArrayList;

import jxl.Sheet;
import jxl.Workbook;

public class ActivityMain extends AppCompatActivity implements OnMapReadyCallback {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private View parent_view;
    private FloatingActionButton fab;

    private PageFragmentAdapter adapter;

    private PageSettingFragment f_recent;
    private PageMapFragment f_call;
    //private LaunchTimeTestFragment f_call;
    private PageEmergencyContactsFragment f_group;
    private PageServicesFragment f_friend;
    private PageUserInfoFragment f_setting;
    private static int[] imageResId = {
            R.drawable.ic_power_settings_new_white_24dp,
            R.drawable.ic_location_on_white_24dp,
            R.drawable.ic_call_white_24dp,
            R.drawable.ic_local_library_white_24dp,
            R.drawable.ic_account_circle_white_24dp
    };


    //이하 영마가 추가
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";

    //private Button mRegistrationButton;
    //private ProgressBar mRegistrationProgressBar;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private static final String DB_TAG = "NotesDbAdapter";
    //private EditText mInformationTextView;

    private NotesDbAdapter dbAdapter;

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
            }
        };
    }


    private LatLngBounds AUSTRALIA = new LatLngBounds(
            new LatLng(-44, 113), new LatLng(-10, 154));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(ActivityMain.this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(ActivityMain.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }

        };
        new TedPermission(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.SEND_SMS, android.Manifest.permission.RECEIVE_SMS, android.Manifest.permission.READ_PHONE_STATE)
                .check();


        //DB
        Log.d(DB_TAG, "DB TEST :: onCreate()");
        this.dbAdapter = new NotesDbAdapter(this);
        readXLSX();


        //GCM
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

        fab.hide();

        // for system bar in lollipop

        readXLSX();


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
            //f_call = new LaunchTimeTestFragment();
        }
        if (f_group == null) {
            f_group = new PageEmergencyContactsFragment();
        }
        if (f_friend == null) {
            f_friend = new PageServicesFragment();
        }
        if (f_setting == null) {
            f_setting = new PageUserInfoFragment();
        }
        adapter.addFragment(f_recent, getString(R.string.tab_recent));
        //adapter.addFragment(f_call, getString(R.string.tab_call));
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
                if (position == 2) {//tab setting
                    fab.show();
                } else {
                    fab.hide();
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(AUSTRALIA, 0));
//        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        googleMap.setMyLocationEnabled(true);
//        googleMap.setTrafficEnabled(true);
//        googleMap.setIndoorEnabled(true);
//        googleMap.setBuildingsEnabled(true);
//        googleMap.getUiSettings().setZoomControlsEnabled(true);
    }

    //read excel and insert to db
    public void readXLSX()
    {

        Workbook workbook = null;
        Sheet sheet = null;

        try {
            InputStream is = getBaseContext().getResources().getAssets().open("incheon.xls");
            workbook = Workbook.getWorkbook(is);

            if (workbook != null) {
                Log.e("xlsx", "work book");
                sheet = workbook.getSheet(0);

                if (sheet != null)
                {

                    int nMaxColumn = 2;
                    int nRowStartIndex = 1;
                    int nRowEndIndex = sheet.getColumn(nMaxColumn - 1).length - 1;
                    int nColumnStartIndex = 0;
                    //int nColumnEndIndex = sheet.getRow(2).length - 1;

                    dbAdapter.open();
                    for (int nRow = nRowStartIndex; nRow <= nRowEndIndex; nRow++) {
                        String cctv_id = sheet.getCell(nColumnStartIndex, nRow).getContents();
                        String cctv_address = sheet.getCell(nColumnStartIndex + 1, nRow).getContents();
                        String cctv_contact = sheet.getCell(nColumnStartIndex + 2, nRow).getContents();
                        String cctv_latitude = sheet.getCell(nColumnStartIndex + 3, nRow).getContents();
                        String cctv_logitude = sheet.getCell(nColumnStartIndex + 4, nRow).getContents();
                        dbAdapter.createNote(cctv_id, cctv_address, cctv_contact, cctv_latitude, cctv_logitude);
                    }

                }
                else
                {
                    Log.e("excel","Sheet is null!!");
                }
            }
            else
            {
                Log.e("excel","Workbook is null!!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (workbook != null) {
                Log.i("excel", "reading excel success");
                workbook.close();
            }
        }
    }
}




