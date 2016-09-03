package com.app.sample.messenger.cctv_DB;

import android.location.Location;

/**
 * Created by duddu on 2016-09-03.
 */
public class CCTVdata {
//    private String id;
    private String address;
    private String contact;
    private Location location;
//    private String latitude;
//    private String longitude;

    public CCTVdata(String id, String address, String contact, String latitude, String longitude) {
        this.location = new Location(id);
        this.address = address;
        this.contact = contact;
        location.setLatitude(Double.parseDouble(latitude));
        location.setLongitude(Double.parseDouble(longitude));
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
