package com.easyweather;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootBroadCastReceiver extends BroadcastReceiver{

	 private static final String TAG = "BootBroadcastReveiver";  
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {  
           
        }  
	}

}
