package com.app.sample.messenger.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Group implements Serializable {
    private int id;
    private String date;
    private String name;
    private String snippet;
    private String address;
    private int photo;
    private ArrayList<Friend> friends = new ArrayList<>();

    public Group(int id, String date, String name, String snippet, String address, int photo) {
        this.id = id;
        this.date = date;
        this.name = name;
        this.snippet = snippet;
        this.address = address;
        this.photo = photo;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getSnippet() {
        return snippet;
    }

    public int getPhoto() {
        return photo;
    }

    public List<Friend> getFriends() {
        return friends;
    }
}


