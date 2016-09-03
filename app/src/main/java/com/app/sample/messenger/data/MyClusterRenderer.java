package com.app.sample.messenger.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;

import com.app.sample.messenger.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

/**
 * Created by dongheelee on 16. 9. 4..
 */
public class MyClusterRenderer extends DefaultClusterRenderer<DangerPlace> {

    private final IconGenerator mClusterIconGenerator;

    Context context;
    public MyClusterRenderer(Context context, GoogleMap map,
                             ClusterManager<DangerPlace> clusterManager) {
        super(context, map, clusterManager);
        this.context = context;
        mClusterIconGenerator = new IconGenerator(context);
    }

    @Override
    protected void onBeforeClusterItemRendered(DangerPlace item,
                                               MarkerOptions markerOptions) {

        //Drawable mDrawable = mContext.getResources().getDrawable(R.drawable.ic_report);
        //mDrawable.setColorFilter(new PorterDuffColorFilter(0xffff00, PorterDuff.Mode.MULTIPLY));
        Bitmap bigPictureBitmap  = BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_loadingicon);
        bigPictureBitmap = Bitmap.createScaledBitmap(bigPictureBitmap,32,32,true);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bigPictureBitmap));
    }

    @Override
    protected void onClusterItemRendered(DangerPlace clusterItem, Marker marker) {
        super.onClusterItemRendered(clusterItem, marker);
    }

    @Override
    protected void onBeforeClusterRendered(Cluster<DangerPlace> cluster, MarkerOptions markerOptions){

        final Drawable clusterIcon = context.getResources().getDrawable(R.drawable.ic_lens_black_24dp);
        clusterIcon.setColorFilter(context.getResources().getColor(android.R.color.holo_orange_light), PorterDuff.Mode.SRC_ATOP);

        mClusterIconGenerator.setBackground(clusterIcon);

        //modify padding for one or two digit numbers
        if (cluster.getSize() < 10) {
            mClusterIconGenerator.setContentPadding(40, 20, 0, 0);
        }
        else {
            mClusterIconGenerator.setContentPadding(30, 20, 0, 0);
        }

        Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
    }
}

