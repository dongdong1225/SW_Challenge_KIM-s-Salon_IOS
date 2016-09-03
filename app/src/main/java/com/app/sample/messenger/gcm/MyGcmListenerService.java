package com.app.sample.messenger.gcm;

/**
 * Created by duddu on 2016-09-01.
 */
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.app.sample.messenger.ActivityMain;
import com.app.sample.messenger.R;
import com.app.sample.messenger.cctv_DB.CCTVdata;
import com.app.sample.messenger.cctv_DB.NotesDbAdapter;
import com.app.sample.messenger.data.Singleton;
import com.google.android.gms.gcm.GcmListenerService;

import java.util.ArrayList;

public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";
    private NotesDbAdapter dbAdapter;
    private ArrayList<CCTVdata> cctVdatas;
    private Location myLocation;
    private Singleton singleton;

    /**
     *
     * @param from SenderID 값을 받아온다.
     * @param data Set형태로 GCM으로 받은 데이터 payload이다.
     */
    @Override
    public void onMessageReceived(String from, Bundle data) {
        readDB();
        chooseCCTV(); // 내 위치 기준 구현 필요

        //노티 관련 변수
        String title = data.getString("title");
        String message = data.getString("message");
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Title: " + title);
        Log.d(TAG, "Message: " + message);

        // GCM으로 받은 메세지를 디바이스에 알려주는 sendNotification()을 호출한다.
        sendNotification(title, message);

        //문자 전송
        sendSMS("010-4427-0801", "도와주세요 " + chooseCCTV()); //비상연락망, 내 프로필 전송 구현 필요




    }


    /**
     * 실제 디바에스에 GCM으로부터 받은 메세지를 알려주는 함수이다. 디바이스 Notification Center에 나타난다.
     * @param title
     * @param message
     */
    private void sendNotification(String title, String message) {
        Intent intent = new Intent(this, ActivityMain.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_menu_video)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    public void sendSMS(String smsNumber, String smsText)
    {
        PendingIntent sentIntent = PendingIntent.getBroadcast(this, 0, new Intent("SMS_SENT_ACTION"), 0);
        PendingIntent deliveredIntent = PendingIntent.getBroadcast(this, 0, new Intent("SMS_DELIVERED_ACTION"), 0);

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch(getResultCode()){
                    case Activity.RESULT_OK:
                        // 전송 성공
                        Toast.makeText(getApplicationContext(), "전송 완료", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        // 전송 실패
                        Toast.makeText(getApplicationContext(), "전송 실패", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        // 서비스 지역 아님
                        Toast.makeText(getApplicationContext(), "서비스 지역이 아닙니다", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        // 무선 꺼짐
                        Toast.makeText(getApplicationContext(), "무선(Radio)가 꺼져있습니다", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        // PDU 실패
                        Toast.makeText(getApplicationContext(), "PDU Null", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter("SMS_SENT_ACTION"));

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()){
                    case Activity.RESULT_OK:
                        // 도착 완료
                        Toast.makeText(getApplicationContext(), "SMS 도착 완료", Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        // 도착 안됨
                        Toast.makeText(getApplicationContext(), "SMS 도착 실패", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter("SMS_DELIVERED_ACTION"));

        SmsManager mSmsManager = SmsManager.getDefault();
        mSmsManager.sendTextMessage(smsNumber, null, smsText, sentIntent, deliveredIntent);
    }

    public void readDB()
    {
        cctVdatas = new ArrayList<>();
        dbAdapter.open();
        Cursor result = dbAdapter.fetchAllNotes();
        result.moveToFirst();
        String resultStr = "";
        while (!result.isAfterLast()) {
            String id = result.getString(1);
            String addr = result.getString(2);
            String contact = result.getString(3);
            String latitude = result.getString(4);
            String longitude = result.getString(5);

            cctVdatas.add(new CCTVdata(id, addr, contact, latitude, longitude));

            result.moveToNext();
        }

        result.close();
        dbAdapter.close();
    }

    public String chooseCCTV()
    {
        String str_nearCCTV = "";
        myLocation = new Location("my");
        myLocation.setLatitude(37.3751215);
        myLocation.setLongitude(126.6676303);

        for(int i =0 ; i<cctVdatas.size() ; i++)
        {
            //500 미터 이내 cctv 가려냄
            if(myLocation.distanceTo(cctVdatas.get(i).getLocation())<=500)
            {
                str_nearCCTV += (cctVdatas.get(i).getLocation().getProvider() + "/ ");
                str_nearCCTV += (cctVdatas.get(i).getAddress() + "/ ");
                str_nearCCTV += (cctVdatas.get(i).getContact() + "/ ");

            }
        }


        return str_nearCCTV;

    }


}