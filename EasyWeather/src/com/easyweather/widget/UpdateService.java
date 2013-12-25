package com.easyweather.widget;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.widget.RemoteViews;

import com.easyweather.EasyApp;
import com.easyweather.logic.WeatherAPIConfig;
import com.easyweather.util.DataUtil;
import com.easyweather.util.ULog;
import com.ta.util.http.AsyncHttpClient;
import com.ta.util.http.AsyncHttpResponseHandler;

public class UpdateService extends Service{
	Context mContext;
	private Timer mTimer;
	JSONObject obj=null;
	Bundle bundle=null;
	
	Handler mHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			/**
			 * 获取本地设置的默认城市
			 */
			if (msg.arg1==1) {
				getLocalCity();
				loadTemperData(bundle);
				
			}else if (msg.arg1==2) {
				try {
					/**
					 * 获取本地保存的默认城市的天气信息
					 */
					obj=new JSONObject(data).getJSONObject("weatherinfo");
					obj.put("imagename", obj.getString("img1"));
					serviceUpate(obj);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		};
	};

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		ULog.e("onCreate", "onCreate");
		mContext=this;
		bundle=new Bundle();
		mTimer = new Timer();    
		/**
		 * 定时1个小时进行更新一次
		 */
		mTimer.schedule(new TimerTask() {          
		            @Override
		            public void run() {
		            	Message msg=mHandler.obtainMessage();
		            	msg.arg1=1;
		                mHandler.sendMessage(msg);              
		            }
		        },60*60*1000, 60*60*1000); 
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}
	
	/**
	 * 更新桌面widget
	 * @param obj
	 */
	public void serviceUpate(JSONObject obj){
		AppWidgetManager manager=AppWidgetManager.getInstance(this);
		RemoteViews views;
		views = EasyWeatherProvider.updateVIew(mContext,obj,true);
		int[] appids=manager.getAppWidgetIds(new ComponentName(this, EasyWeatherProvider.class));
		manager.updateAppWidget(appids, views);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		ULog.e("onDestroy", "onDestroy");
	}
	
	/**
	 * 从本地文件中读取第一个城市作为桌面的widget显示的城市
	 */
	public void getLocalCity(){
		String localCity = EasyApp.mPreCity.getString("localcity", "");
		if(!TextUtils.isEmpty(localCity)){
			try {
				JSONArray localCityArray=new JSONArray(localCity);
				for (int i = 0; i < localCityArray.length(); i++) {
					JSONObject localCityObj=localCityArray.getJSONObject(i);
					if (localCityObj.getBoolean("status")) {
						bundle.putString("city",localCityObj.getString("code"));
						break;
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	private String data;
	public void loadTemperData(Bundle params){
		String URL=DataUtil.getDataUtil().buildURL(WeatherAPIConfig.getWeatherInfo,params);
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(URL,new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(String content) {
						super.onSuccess(content);
						data = content;
						Message msg=mHandler.obtainMessage();
						msg.arg1=2;
						mHandler.sendMessage(msg);
					}
				});
	}
}
