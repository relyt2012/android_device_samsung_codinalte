package com.meticulus.codinalteparts.app;

/**
 * Created by meticulus on 4/7/14.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.meticulus.codinalteparts.app.FunctionsMain;


public class bootreceiver extends BroadcastReceiver  {

    SharedPreferences sharedPref;
    public void onReceive(Context arg0, Intent arg1)
    {
        sharedPref = PreferenceManager.getDefaultSharedPreferences(arg0.getApplicationContext());
        //If incallaudio is selected that start service at boot.
        if(sharedPref.getBoolean("incallaudio", true))
        {
            FunctionsMain.startInCallAudioService(arg0.getApplicationContext());
        }
        //Set cpu2 on or off
        //Setting it on if it's already on should make no difference.
        FunctionsMain.SetCPU2(sharedPref.getBoolean("cpu2",true));

        if(sharedPref.getBoolean("LKMNKP", true))
        {
            FunctionsMain.enableLMKNKP();
            FunctionsMain.setLMKNKPWhitelist();;
        }

        if(sharedPref.getBoolean("autologcat", false))
            FunctionsMain.startAutologcat();

        if(sharedPref.getBoolean("autokmsg", false))
            FunctionsMain.startAutokmsg();
        if(sharedPref.getBoolean("autoril",false))
            FunctionsMain.startAutorillog();


    }
}
