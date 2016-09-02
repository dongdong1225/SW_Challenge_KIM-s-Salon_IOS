package com.app.sample.messenger.data;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by dongheelee on 16. 9. 3..
 */
public class DangerPlace implements ClusterItem {

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    private LatLng location;


    public DangerPlace(LatLng location)
    {
        this.location = location;
    }


    @Override
    public LatLng getPosition() {
        return location;
    }
}
