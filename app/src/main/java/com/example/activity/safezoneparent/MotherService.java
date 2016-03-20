package com.example.activity.safezoneparent;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.TextView;

import com.parse.ParseObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.google.android.gms.internal.zzid.runOnUiThread;

/**
 * Created by Tien on 29-Dec-15.
 */
public class MotherService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    Timer timerMother;
    MotherTimerTask motherTimerTask;
    public static final String CHILD_LATITUDE = "child_latitude";
    public static final String CHILD_LONGITUDE = "child_longitude";
    public static final String CHILD_NAME = "child_name";
    @Override
    public void onCreate() {
        super.onCreate();
        timerMother = new Timer();
        motherTimerTask = new MotherTimerTask();
        timerMother.schedule(motherTimerTask, 10000, 300000); //5p
        Log.d("", "on Mother Service");
    }

    List<Route> routes;

    private int compareTime(Date time1, Date currentTime) {
        int result = 0;
        result = time1.compareTo(currentTime);
        return result;
    }

    public RouteHelper routeHelper = new RouteHelper(this);
    double lat;
    double llong;
    class MotherTimerTask extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("UI", "on run on UI Thread");
                    //1. Lay list routes tu DB len
                    routes = routeHelper.get();
                    // 2. Lay vi tri hien tai cua con tren server
                    QueryAccount queryAccount = new QueryAccount();
                    queryAccount.queryAccount("Children", new QueryAccount.QueryRouteCallBack() {
                        @Override
                        public void queryRouteSuccess(List<ParseObject> parseObjects) {

                                // 3.  So sanh thoi gian
                                for (int j = 0; j < routes.size(); j++) {
                                    for (int i = 0;i<parseObjects.size();i++) {
                                        String childName = parseObjects.get(i).getString(CHILD_NAME);
                                        if (childName.equals(routes.get(j).getChildrenName())) {
                                            lat = parseObjects.get(i).getDouble(CHILD_LATITUDE);
                                            llong = parseObjects.get(i).getDouble(CHILD_LONGITUDE);
                                            Log.d("", "lat: " + lat + "; long: " + llong);

                                            try {
                                                Log.d("","begin compare");
                                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                                                Date timeDBFrom = simpleDateFormat.parse(routes.get(j).getTimeFrom());
                                                Date timeDBTo = simpleDateFormat.parse(routes.get(j).getTimeTo());
                                                Date currentTime = new Date();
                                                String strCurrent = simpleDateFormat.format(currentTime);
                                                Date current = simpleDateFormat.parse(strCurrent);
                                                // so sanh thoi gian bat dau voi thoi gian hien tai
                                                int compareTime_from_current = compareTime(timeDBFrom, current);
                                                // so sanh thoi gian ket thuc voi thoi gian hien tai
                                                int compareTime_current_to = compareTime(current, timeDBTo);
                                                if ((compareTime_from_current == 0 || compareTime_from_current == -1) &&
                                                        (compareTime_current_to == -1 || compareTime_current_to == 0)) { //from <= now <= to
                                                    Log.d("", "compare time ok");


                                                    // so sanh location
                                                    double distance = calculateDistance(Double.valueOf(routes.get(j).getdLatitute()), Double.valueOf(routes.get(j).getdLongtitute()), lat, llong);
                                                    if (distance >= Double.parseDouble(routes.get(j).getRadius())) {
                                                        Log.d("Result", "Notification");
                                                        showDialog(getBaseContext(), childName);
                                                    }
                                                }

                                            } catch (java.text.ParseException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                }
                                }
                        }
                    });

                }
            });

        }
    }

    public void notification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        Intent notificationIntent = new Intent(getBaseContext(), MapsActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        builder.setAutoCancel(false);
        builder.setLights(Color.GREEN, 500, 500);
        builder.setStyle(new NotificationCompat.InboxStyle());
        builder.setSmallIcon(R.drawable.rsz_icon_notification)
                .setContentTitle("Notification").setContentText("out of route");
        Uri alarmmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(alarmmSound);

        NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, builder.build());

    }

    private void showDialog(final Context context, String name){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                .setTitle("WARNING!!!")
                .setIcon(R.drawable.rsz_notification2);

        TextView myMsg = new TextView(context);
        myMsg.setText("Your child - " + name.toUpperCase() + " is out of route");
        myMsg.setGravity(Gravity.CENTER);
        dialog.setView(myMsg);
        dialog.setCancelable(false).setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        }).setNegativeButton("Show On Map", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(getBaseContext(), MapsActivity.class);
                intent.putExtra("ChildName", ShowChildrenActivity.childrenNameClick);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        Uri alarmmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        MediaPlayer mp = MediaPlayer.create(context, alarmmSound);
        mp.start();
        AlertDialog alertDialog = dialog.create();
        alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        alertDialog.show();
    }

    private double calculateDistance(double pointSetX, double pointSetY, double currentPointX,double currentPointY) {
        double distance = 0;


        distance = Math.sqrt(Math.pow(pointSetX - currentPointX, 2) + Math.pow(pointSetY - currentPointY, 2));
        return distance;
    }

    @Override
    public void onDestroy() {
        Log.d("","on Destroy Mother");
        timerMother.cancel();
        super.onDestroy();
    }
}
