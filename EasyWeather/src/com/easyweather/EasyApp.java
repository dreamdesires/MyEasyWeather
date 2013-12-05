package com.easyweather;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import com.ta.TAApplication;

public class EasyApp extends TAApplication{

	public static Resources res=null;
	public static SharedPreferences mPreCity=null;//将修改过的城市数据保存在本地
	public static boolean isLocation=false;//设置当前应用进入是否定位 false为不定位,true为定位
	
	@Override
	public void onCreate() {
		super.onCreate();
		res=getResources();
		mPreCity=this.getSharedPreferences("localcity", Context.MODE_PRIVATE);
	}
	
}
