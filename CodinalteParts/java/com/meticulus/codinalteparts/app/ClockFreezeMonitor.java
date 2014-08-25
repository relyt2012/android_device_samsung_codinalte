package com.meticulus.codinalteparts.app;

import android.app.AlarmManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;


import java.lang.Override;
import java.util.TimerTask;

public class ClockFreezeMonitor extends Service {

    private static String TAG = "ClockFreezeMonitor";

    TimerTask TickTimeoutCheck;
    TimerTask TickBroadcaster;
    public static java.util.Timer generalTimer;
    AlarmReceiver ar;

    @Override
    public void onCreate() {
        super.onCreate();

        generalTimer = new java.util.Timer("CFM TIMER");

        TickTimeoutCheck = new TimerTask() {
            @Override
            public void run() {
                startBroadcasting();
            }
        };

        TickBroadcaster = new TimerTask() {
            @Override
            public void run() {
                Intent i = new Intent(Intent.ACTION_TIME_TICK);
                getApplicationContext().sendBroadcast(i);
            }
        };

        AlarmManager am = (AlarmManager) getApplicationContext().getSystemService(ALARM_SERVICE);
        IntentFilter intentf = new IntentFilter(Intent.ACTION_TIME_TICK);
        ar = new AlarmReceiver();
        registerReceiver(ar, intentf);
    }

    public void startBroadcasting(){
        unregisterReceiver(ar);
        generalTimer.schedule(TickBroadcaster,60000);
        Log.e(TAG, "Manual Time Updating engaged.");

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    class AlarmReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG, "ACTION_TIME_TICK received");
            TickTimeoutCheck.cancel();
            generalTimer.purge();

            TickTimeoutCheck = new TimerTask() {
                @Override
                public void run() {
                    startBroadcasting();
                }
            };

            generalTimer.schedule(TickTimeoutCheck,120000);
        }
    }
}
