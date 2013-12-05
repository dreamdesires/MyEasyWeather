/**
 * Copyright 2013 Barfoo
 * 
 * All right reserved
 * 
 * Created on 2013-10-10 下午5:57:51
 * 
 * @author zxy
 */
package com.easyweather.util;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.easyweather.R;

public class UToast {

	public static void makeText(Activity context, int usandpwisnull, int duration) {
		LayoutInflater inflater = context.getLayoutInflater();
		View layout = inflater.inflate(R.layout.toast, (ViewGroup) context.findViewById(R.id.toastframat));
		TextView message = (TextView) layout.findViewById(R.id.message);
		message.setText(usandpwisnull);
		Toast toast = new Toast(context.getApplicationContext());
		toast.setDuration(duration);
		toast.setGravity(Gravity.BOTTOM, 0, 300);
		toast.setView(layout);
		toast.show();
	}

	public static void makeText(Activity context, String usandpwisnull, int duration) {
		LayoutInflater inflater = context.getLayoutInflater();
		View layout = inflater.inflate(R.layout.toast, (ViewGroup) context.findViewById(R.id.toastframat));
		TextView message = (TextView) layout.findViewById(R.id.message);
		message.setText(usandpwisnull);
		Toast toast = new Toast(context.getApplicationContext());
		toast.setDuration(duration);
		toast.setGravity(Gravity.BOTTOM, 0, 300);
		toast.setView(layout);
		toast.show();
	}
}
