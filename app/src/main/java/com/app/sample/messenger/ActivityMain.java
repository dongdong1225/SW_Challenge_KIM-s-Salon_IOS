package com.app.sample.messenger;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.app.sample.messenger.adapter.PageFragmentAdapter;
import com.app.sample.messenger.data.Tools;
import com.app.sample.messenger.fragment.PageMapFragment;
import com.app.sample.messenger.fragment.PageServicesFragment;
import com.app.sample.messenger.fragment.PageEmergencyContactsFragment;
import com.app.sample.messenger.fragment.PageSettingFragment;
import com.app.sample.messenger.fragment.PageUserInfoFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

public class ActivityMain extends AppCompatActivity implements OnMapReadyCallback {
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
                if (position == 4) {//tab setting
                    fab.hide();
                } else {
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(AUSTRALIA, 0));
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);
        googleMap.setTrafficEnabled(true);
        googleMap.setIndoorEnabled(true);
        googleMap.setBuildingsEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
    }





}




