package com.app.sample.messenger.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.sample.messenger.R;
import com.app.sample.messenger.adapter.ChatsListAdapter;
import com.app.sample.messenger.data.DangerPlace;
import com.app.sample.messenger.data.OwnIconRendered;
import com.app.sample.messenger.model.Chat;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class PageMapFragment extends Fragment implements OnMapReadyCallback {

    private View view;

    private RecyclerView recyclerView;
    private List<Chat> items = new ArrayList<>();
    private ChatsListAdapter mAdapter;

    private GoogleMap _map;


    public ClusterManager<DangerPlace> mClusterManger;
    MapView mapView;
    public GoogleMap map;
    protected MapView mMapView;

    protected LocationManager mLocationManager;

    private LatLngBounds AUSTRALIA = new LatLngBounds(
            new LatLng(-44, 113), new LatLng(-10, 154));

    //private static final String TAG = LaunchTimeTestFragment.class.getSimpleName();

    private static final String EXTRA_CLUSTERING_TYPE = "clusteringType";
    public static final int CLUSTERING_DISABLED = 0;
    public static final int CLUSTERING_DISABLED_DYNAMIC = 1;
    public static final int CLUSTERING_ENABLED = 2;
    public static final int CLUSTERING_ENABLED_DYNAMIC = 3;

    private boolean initMyLoc = false;

    private static final int MARKERS_COUNT = 20000;

    CameraPosition mPreviousCameraPosition = null;

// Set the camera to the greatest possible zoom level that includes the
// bounds


    @Override
    public void onResume() {
        super.onResume();
        if (mMapView != null) {
            mMapView.onResume();
        }
    }

    @Override
    public void onPause() {
        if (mMapView != null) {
            mMapView.onPause();
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (mMapView != null) {
            try {
                mMapView.onDestroy();
            } catch (NullPointerException e) {
                Log.e("dd", "Error while attempting MapView.onDestroy(), ignoring exception", e);
            }
        }
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mMapView != null) {
            mMapView.onLowMemory();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mMapView != null) {
            mMapView.onSaveInstanceState(outState);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mapview_fragment, parent, false);
        mMapView = (MapView) view.findViewById(R.id.map_container);
        mMapView.onCreate(savedInstanceState);

        mMapView.getMapAsync(this);

        return view;
    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            //your code here
        }
    };




    @Override
    public void onMapReady(com.google.android.gms.maps.GoogleMap googleMap) {

        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(AUSTRALIA, 0));
        //googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        map = googleMap;

        googleMap.setTrafficEnabled(true);
        googleMap.setIndoorEnabled(true);
        googleMap.setBuildingsEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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



        mClusterManger = new ClusterManager<>(this.getContext(),googleMap);
        //map.setOnMapClickListener( mClusterManger);
        //googleMap.setOnCameraChangeListener(mClusterManger);
        googleMap.setOnMyLocationChangeListener(myLocationChangeListener());

        map.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                CameraPosition position = map.getCameraPosition();
                if(mPreviousCameraPosition == null || mPreviousCameraPosition.zoom != position.zoom) {
                    mPreviousCameraPosition = map.getCameraPosition();
                    mClusterManger.cluster();
                }
            }
        });
        //map = SupportMapFragment.newInstance().getExtendedMap();
        //setUpMap();
        addDangerPlace();

    }

    private void addDangerPlace()
    {

        Random rand = new Random();
        LatLng loc = new LatLng(0,0);
        for(int i = 0; i < 500; i ++) {
            double randnum = rand.nextDouble();
            int randInt = rand.nextInt(4);
            int randX = rand.nextInt(100);
            rand.setSeed(System.currentTimeMillis());
            int randY = rand.nextInt(100);

            switch(randInt)
            {
                case 0 :

                    loc = new LatLng(37.4716393 + randnum/randX,126.7558353 + randnum/randY);
                    break;
                case 1 :
                    loc = new LatLng(37.4716393 + randnum/randX,126.7558353 - randnum/randY);
                    break;
                case 2 :
                    loc = new LatLng(37.4716393 - randnum/randX,126.7558353 + randnum/randY);
                    break;
                case 3 :
                    loc = new LatLng(37.4716393 - randnum/randX,126.7558353 - randnum/randY);
                    break;
                default :

                    break;

            }

                    mClusterManger.addItem(new DangerPlace(loc));
                    mClusterManger.setRenderer(new OwnIconRendered(this.getContext(),map,mClusterManger));

        }


        for(int i = 0; i < 30; i ++) {
            rand.setSeed(System.currentTimeMillis());
            double randnum = rand.nextDouble();
            int randInt = rand.nextInt(4);
            int randX = rand.nextInt(10);
            int randY = rand.nextInt(10);
            double randnum2 = rand.nextDouble();

            switch(randInt)
            {
                case 0 :
                    loc = new LatLng(37.4716393 + randnum/randX,126.7558353 + randnum2/randY);
                    break;
                case 1 :
                    loc = new LatLng(37.4716393 + randnum/randX,126.7558353 - randnum2/randY);
                    break;
                case 2 :
                    loc = new LatLng(37.4716393 - randnum/randX,126.7558353 + randnum2/randY);
                    break;
                case 3 :
                    loc = new LatLng(37.4716393 - randnum/randX,126.7558353 - randnum2/randY);
                    break;
                default :

                    break;



            }

            Bitmap bigPictureBitmap  = BitmapFactory.decodeResource(this.getContext().getResources(),R.drawable.policeicon);
            bigPictureBitmap = Bitmap.createScaledBitmap(bigPictureBitmap,128,128,true);
            map.addMarker(new MarkerOptions().position(loc).icon(BitmapDescriptorFactory.fromBitmap(bigPictureBitmap)));
           // mClusterManger.addItem(new DangerPlace(loc));
           // mClusterManger.setRenderer(new OwnIconRendered(this.getContext(),map,mClusterManger));

        }
    }

    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener() {
        return new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
                double longitude = location.getLongitude();
                double latitude = location.getLatitude();

                Marker marker;
                //com.androidmapsextensions.MarkerOptions options = new com.androidmapsextensions.MarkerOptions();
                //marker = map.addMarker(options.position(loc));
                //marker = map.addMarker(new MarkerOptions().position(loc));
                //mClusterManger.addItem(new DangerPlace(loc));
                if(initMyLoc == false) {
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
                    initMyLoc = true;
                }
                //locationText.setText("You are at [" + longitude + " ; " + latitude + " ]");

                //get current address by invoke an AsyncTask object
               // new GetAddressTask(LocationActivity.this).execute(String.valueOf(latitude), String.valueOf(longitude));
            }
        };
    }



}


