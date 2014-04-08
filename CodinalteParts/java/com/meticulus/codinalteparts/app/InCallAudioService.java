package com.meticulus.codinalteparts.app;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.DataOutputStream;

/**
 * Created by meticulus on 4/7/14.
 */
public class InCallAudioService  extends Service {

    Boolean Hooked = false, Speak = false;
    AudioManager audioManager;
    TelephonyManager telephonyManager;
    int vmax = 100;
    private IntentFilter mCallStateChangedFilter;
    private BroadcastReceiver mCallStateIntentReceiver;
    final Handler schandler = new Handler();
    final Handler vmhandler = new Handler();


    Runnable myscan = new Runnable() {
        public void run() {
            scanForSpeak();
        }
    };

    Runnable myvolmax = new Runnable() {
        public void run() {
            mySetMaxVolume();
        }
    };

    public void scanForSpeak() {
        if (((!Speak) && (audioManager.isSpeakerphoneOn())) || ((Speak) && (!audioManager.isSpeakerphoneOn()))) {
            Speak = audioManager.isSpeakerphoneOn();
            CorrectMaxVolume();
        }
        schandler.postDelayed(myscan, 500);
    }

    public void mySetMaxVolume() {
        int streamType = AudioManager.STREAM_VOICE_CALL;
        if (audioManager.isBluetoothA2dpOn() || audioManager.isBluetoothScoOn()) {
            streamType = 6;
        }
        audioManager.setStreamVolume(streamType, vmax, 0);
        Log.i("cmcallservice", "curr. vol: "+Integer.toString(audioManager.getStreamVolume(streamType)));
        schandler.postDelayed(myscan, 500);
    }

    public void resetStreamVolume(int streamType) {
         /* the idea here is to change the volume then change it back */
        int currentVolume = audioManager.getStreamVolume(streamType);
        int maxvolume = audioManager.getStreamMaxVolume(streamType);

        if(currentVolume == 0)
            audioManager.setStreamVolume(streamType,1,0);
        else if(currentVolume == maxvolume)
            audioManager.setStreamVolume(streamType,currentVolume -1,0);
        else
            audioManager.setStreamVolume(streamType,currentVolume +1,0);
        audioManager.setStreamVolume(streamType, currentVolume, 0);
    }
    public void setStreamVolume(int streamType,int volume) {

        audioManager.setStreamVolume(streamType, volume, 0);
    }
    public void resetStreams(){
        resetStreamVolume(AudioManager.STREAM_RING);
        resetStreamVolume(AudioManager.STREAM_NOTIFICATION);
        resetStreamVolume(AudioManager.STREAM_SYSTEM);
    }

    public void CorrectMaxVolume() {
        int streamType = AudioManager.STREAM_VOICE_CALL;
        if (audioManager.isBluetoothA2dpOn() || audioManager.isBluetoothScoOn()) {
            streamType = 6;
        }
        resetStreamVolume(streamType);
        Log.i("cmcallservice", "curr. vol: " + Integer.toString(audioManager.getStreamVolume(streamType)));
        vmhandler.postDelayed(myvolmax, 200);

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        resetStreams();
        mCallStateChangedFilter = new IntentFilter();
        mCallStateChangedFilter.addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
        mCallStateIntentReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                vmax = audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL);
                telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                telephonyManager.listen(new CustomPhoneStateListener(context), PhoneStateListener.LISTEN_CALL_STATE);
            }
        };
    }

    @Override
    public void onStart(Intent intent, int startId) {
        registerReceiver(mCallStateIntentReceiver, mCallStateChangedFilter);
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mCallStateIntentReceiver);
    }
    public static void ExecuteNoReturn(String command) throws Exception {
        Process process = Runtime.getRuntime().exec("su");
        DataOutputStream os = new DataOutputStream(process.getOutputStream());
        os.writeBytes(command + "\n");
        //os.writeBytes("exit\n");
        //os.flush();
        //os.close();

        //process.waitFor();
    }

    public class CustomPhoneStateListener extends PhoneStateListener {

        //private static final String TAG = "PhoneStateChanged";
        Context context; //Context to make Toast if required
        public CustomPhoneStateListener(Context context) {
            super();
            this.context = context;
        }

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    //when Idle i.e no call
                    Hooked = false;
                    vmhandler.removeCallbacks(myvolmax);
                    schandler.removeCallbacks(myscan);
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    //when Off hook i.e in call
                    //Make intent and start your service here
                    Speak = audioManager.isSpeakerphoneOn();
                    if (!Hooked) {CorrectMaxVolume();}
                    Hooked = true;
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    //when Ringing
                    break;
                default:
                    break;
            }
        }
    }


}
