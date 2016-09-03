package com.app.sample.messenger.data;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.Log;
import android.widget.AdapterView;

import com.app.sample.messenger.R;
import com.app.sample.messenger.model.Chat;
import com.app.sample.messenger.model.ChatsDetails;
import com.app.sample.messenger.model.Friend;
import com.app.sample.messenger.model.Group;
import com.app.sample.messenger.model.GroupDetails;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class Constant {

    public static Resources getStrRes(Context context){
        return context.getResources();
    }

    public static String formatTime(long time){
        // income time
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(time);

        // current time
        Calendar curDate = Calendar.getInstance();
        curDate.setTimeInMillis(System.currentTimeMillis());

        SimpleDateFormat dateFormat = null;
        if(date.get(Calendar.YEAR)==curDate.get(Calendar.YEAR)){
            if(date.get(Calendar.DAY_OF_YEAR) == curDate.get(Calendar.DAY_OF_YEAR) ){
                dateFormat = new SimpleDateFormat("h:mm a", Locale.US);
            }
            else{
                dateFormat = new SimpleDateFormat("MMM d", Locale.US);
            }
        }
        else{
            dateFormat = new SimpleDateFormat("MMM yyyy", Locale.US);
        }
        return dateFormat.format(time);
    }


    public static float getAPIVerison() {

        Float f = null;
        try {
            StringBuilder strBuild = new StringBuilder();
            strBuild.append(android.os.Build.VERSION.RELEASE.substring(0, 2));
            f = new Float(strBuild.toString());
        } catch (NumberFormatException e) {
            Log.e("", "erro ao recuperar a versão da API" + e.getMessage());
        }

        return f.floatValue();
    }

    public static List<Friend> getFriendsData(Context ctx)  {
        List<Friend> items = new ArrayList<>();
        String s_arr[] = ctx.getResources().getStringArray(R.array.people_names);
        TypedArray drw_arr = ctx.getResources().obtainTypedArray(R.array.people_photos);
        for (int i = 0; i < s_arr.length; i++) {
            Friend fr = new Friend(i, s_arr[i], drw_arr.getResourceId(i, -1));
            items.add(fr);
        }
        return items;
    }

    public static List<Chat> getChatsData(Context ctx)  {
        List<Chat> items = new ArrayList<>();
        String s_arr[] = ctx.getResources().getStringArray(R.array.people_names);
        TypedArray drw_arr = ctx.getResources().obtainTypedArray(R.array.people_photos);
        String s_cht[] = ctx.getResources().getStringArray(R.array.chat_snippet);
        String s_date[] = ctx.getResources().getStringArray(R.array.chat_date);
        for (int i = 0; i < 10; i++) {
            items.add(new Chat(i, s_date[i], true, new Friend(s_arr[i+5], drw_arr.getResourceId(i+5, -1)), s_cht[i]));
        }
        return items;
    }

    public static List<Group> getGroupData(Context ctx)  {
        List<Group> items = new ArrayList<>();
        String s_name[] = ctx.getResources().getStringArray(R.array.groups_name);
        String s_date[] = ctx.getResources().getStringArray(R.array.groups_date);
        String s_address[] = ctx.getResources().getStringArray(R.array.group_details_address);
        TypedArray drw_arr = ctx.getResources().obtainTypedArray(R.array.groups_photos);

        items.add(new Group(0, s_date[0], s_name[0], "", s_address[0], drw_arr.getResourceId(0,-1)));
        items.add(new Group(1, s_date[1], s_name[1], "", s_address[1], drw_arr.getResourceId(1,-1)));
        items.add(new Group(2, s_date[2], s_name[2], "", s_address[2], drw_arr.getResourceId(2,-1)));
        items.add(new Group(3, s_date[3], s_name[3], "", s_address[3], drw_arr.getResourceId(3,-1)));
        items.add(new Group(4, s_date[4], s_name[4], "", s_address[4], drw_arr.getResourceId(4,-1)));
        items.add(new Group(5, s_date[5], s_name[5], "", s_address[5], drw_arr.getResourceId(5,-1)));

        return items;
    }

    private static ArrayList<Friend> friendSubList(Context ctx, int start, int end){
        ArrayList<Friend> friends = new ArrayList<>();
        friends.addAll(getFriendsData(ctx));
        ArrayList<Friend> friends_ = new ArrayList<>();
        for (int i = start; i <= end; i++) {
            friends_.add(friends.get(i));
        }
        return friends_;
    }

    public static List<ChatsDetails> getChatDetailsData(Context ctx, Friend friend)  {
        List<ChatsDetails> items = new ArrayList<>();
        String s_date[] = ctx.getResources().getStringArray(R.array.chat_details_date);
        String s_content[] = ctx.getResources().getStringArray(R.array.chat_details_content);

        items.add(new ChatsDetails(0, s_date[0], friend, s_content[0], false));
        items.add(new ChatsDetails(1, s_date[1], friend, s_content[1], true));
        items.add(new ChatsDetails(2, s_date[2], friend, s_content[2], false));

        return items;
    }



    public static List<GroupDetails> getGroupDetailsData(Context ctx, int position)  {
        List<Group> groups = getGroupData(ctx);
        List<GroupDetails> items = new ArrayList<>();

        String s_date[] = ctx.getResources().getStringArray(R.array.group_details_date);
        String s_content[] = ctx.getResources().getStringArray(R.array.group_alert);

        items.add(new GroupDetails(0, s_date[0], groups.get(position), s_content[position], false));

        return items;
    }

    public static List<Integer> getRandomPhotos(Context ctx)  {
        List<Integer> items = new ArrayList<>();
        TypedArray photo = ctx.getResources().obtainTypedArray(R.array.feed_photos);
        int photo_size = photo.length();
        for (int i = 0; i < photo_size; i++) {
            items.add(photo.getResourceId(i, -1));
        }
        Collections.shuffle(items);
        return items;
    }

    private static int getRandomIndex(Random r, int min, int max){
        return r.nextInt(max - min) + min;
    }

    private static Random rnd = new Random();
    public static boolean getBoolean() {
        return rnd.nextBoolean();
    }
}
