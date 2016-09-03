package com.app.sample.messenger.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;

import com.app.sample.messenger.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

/**
 * Created by dongheelee on 16. 9. 3..
 */
public class OwnIconRendered extends DefaultClusterRenderer<DangerPlace> {

    Context mContext;

    public OwnIconRendered(Context context, GoogleMap map, ClusterManager<DangerPlace> clusterManager) {
        super(context, map, clusterManager);
        mContext = context;
    }

    @Override
    protected void onBeforeClusterItemRendered(DangerPlace item,
                                               MarkerOptions markerOptions) {

        //Drawable mDrawable = mContext.getResources().getDrawable(R.drawable.ic_report);
        //mDrawable.setColorFilter(new PorterDuffColorFilter(0xffff00, PorterDuff.Mode.MULTIPLY));
        Bitmap bigPictureBitmap  = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.ic_loadingicon);
        bigPictureBitmap = Bitmap.createScaledBitmap(bigPictureBitmap,32,32,true);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bigPictureBitmap));
    }
}
