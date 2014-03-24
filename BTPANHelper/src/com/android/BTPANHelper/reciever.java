/*
# Copyright (C) 2014 Jonathan Jason Dennis [Meticulus] (theonejohnnyd@gmail.com)
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
*/

package com.android.BTPANHelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.lang.Process;
import java.io.DataOutputStream;
import java.lang.Runtime;
import java.util.List;
import java.util.ArrayList;

public class reciever extends BroadcastReceiver 
{
    static final String TAG = "BTPANHelper";
    public void onReceive(Context arg0, Intent arg1) 
    {
	Log.e(TAG, arg1.toString());
	Log.e(TAG, arg1.getExtras().toString());
	String[] keys = new String[arg1.getExtras().keySet().size()];
	arg1.getExtras().keySet().toArray(keys);
	for(int i =0;i < keys.length;i++){
		Log.e(TAG, keys[i]);
	}
	
	if(arg1.getAction().equals("android.bluetooth.adapter.action.CONNECTION_STATE_CHANGED") ) {
		int connected = arg1.getExtras().getInt("android.bluetooth.adapter.extra.CONNECTION_STATE");
		if(connected == 2){
			List<String> cmds = new ArrayList<String>();
			cmds.add("netcfg bt-pan dhcp");
			cmds.add("echo \"nameserver 8.8.8.8\" > /system/etc/resolv.conf");
			cmds.add("echo \"nameserver 8.8.4.4\" >> /system/etc/resolv.conf");
			cmds.add("dnsmasq -x /data/dnsmasq.pid");
			try{
				doCmds(cmds);
				Log.i(TAG, "Setting up network for bt-pan");
			}catch(Exception e){
				Log.e(TAG, "Error setting up network for bt-pan",e);
			}
			
		}	
	}
    }

    public static void doCmds(List<String> cmds) throws Exception {
    	Process process = Runtime.getRuntime().exec("su");
    	DataOutputStream os = new DataOutputStream(process.getOutputStream());

    	for (String tmpCmd : cmds) {
    	        os.writeBytes(tmpCmd+"\n");
    	}
	os.writeBytes("exit\n");	
    	os.flush();
    	os.close();
	
    	process.waitFor();
    }
    
}
