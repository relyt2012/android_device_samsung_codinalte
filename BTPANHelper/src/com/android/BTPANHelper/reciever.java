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
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.Runtime;
import java.util.List;
import java.util.ArrayList;

public class reciever extends BroadcastReceiver 
{
    static final String TAG = "BTPANHelper";
    static int BUFF_LEN = 1024 * 100;
    static final String CMD_BTPAN_IP = "RESULT=$(netcfg | grep bt-pan | awk -F '/' '{print $1}' | awk -F ' ' '{print $ 3}'); if [[ \"$RESULT\" == \"\" ]]; then echo \"not found\";else echo $RESULT;fi";
    static final String CMD_BTPAN_DHCP = "netcfg bt-pan dhcp";
    static final String CMD_DNS1 = "echo \"nameserver 8.8.8.8\" > /system/etc/resolv.conf";
    static final String CMD_DNS2 = "echo \"nameserver 8.8.4.4\" >> /system/etc/resolv.conf";
    static final String CMD_DNSMASQ = "dnsmasq -x /data/dnsmasq.pid";
    public void onReceive(Context arg0, Intent arg1) 
    {
	//Log.e(TAG, arg1.toString());
	//Log.e(TAG, arg1.getExtras().toString());
	//String[] keys = new String[arg1.getExtras().keySet().size()];
	//arg1.getExtras().keySet().toArray(keys);
	//for(int i =0;i < keys.length;i++){
	//	Log.e(TAG, keys[i]);
	//}
	if(arg1.getAction().equals("android.bluetooth.adapter.action.CONNECTION_STATE_CHANGED") ) {
		int connected = arg1.getExtras().getInt("android.bluetooth.adapter.extra.CONNECTION_STATE");
		if(connected == 2){
			try{
				String btpanip = "";
				btpanip = ExecuteShellCommand(CMD_BTPAN_IP).trim();
				if(btpanip.equals("not found"))
					Log.i(TAG,"bt-pan interface not found. Probably not enabled");
				else if (btpanip.equals("0.0.0.0")){
					Log.i(TAG,"bt-pan found but not configured.");
					Log.i(TAG,"Running dhcp...");
					ExecuteNoReturn(CMD_BTPAN_DHCP);
					Log.i(TAG,"Setting dns servers...");
					ExecuteNoReturn(CMD_DNS1);
					ExecuteNoReturn(CMD_DNS2);
					Log.i(TAG,"Running dnsmasq...");
					ExecuteNoReturn(CMD_DNSMASQ);
				}
				else{
					Log.i(TAG, "Found ipaddr='" + btpanip+ "'");
					Log.i(TAG, "We are the server, so do nothing..");
					
				}
				
			
			}catch(Exception e){
				Log.e(TAG, "Error setting up network for bt-pan",e);
			}
			
		}	
	}
    }

    public static void ExecuteNoReturn(String command) throws Exception {
    	Process process = Runtime.getRuntime().exec("su");
    	DataOutputStream os = new DataOutputStream(process.getOutputStream());

    	os.writeBytes(command+"\n");
	os.writeBytes("exit\n");	
    	os.flush();
    	os.close();

    	process.waitFor();
    }

    public static byte[] ExecuteCommand(String command) throws IOException
	{
		Process p = Runtime.getRuntime().exec(new String[]{"su", "-c", "system/bin/sh"});
		DataOutputStream stdin = new DataOutputStream(p.getOutputStream());
		//from here all commands are executed with su permissions
		stdin.writeBytes(command + "\n"); // \n executes the command
		InputStream stdout = p.getInputStream();
		byte[] buffer = new byte[BUFF_LEN];
		int read;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		//read method will wait forever if there is nothing in the stream
		//so we need to read it in another way than while((read=stdout.read(buffer))>0)
		while(true){
		    read = stdout.read(buffer);
		    baos.write(buffer, 0, read);
		    if(read<BUFF_LEN){
		        //we have read everything
		        break;
		    }
		}
		return baos.toByteArray();
	}
	public static String ExecuteShellCommand(String command) throws IOException
	{
		byte[] bytes = ExecuteCommand(command);
		return new String(bytes);
	}
    
}
