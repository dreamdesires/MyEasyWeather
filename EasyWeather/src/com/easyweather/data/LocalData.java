package com.easyweather.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.RemoteViews;

import com.easyweather.EasyApp;
import com.easyweather.logic.WeatherAPIConfig;
import com.easyweather.util.DataUtil;
import com.easyweather.widget.EasyWeatherProvider;
import com.ta.util.http.AsyncHttpClient;
import com.ta.util.http.AsyncHttpResponseHandler;

public class LocalData {
	
	Bundle bundle=null;
	private String data;
	private JSONObject obj;
	protected Context mContext;
	
	public LocalData (Context context){
		this.mContext=context;
		getLocalCity();
		loadTemperData(bundle);
	}
	
	Handler mHandler=new Handler(){

		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			/**
			 * 获取本地设置的默认城市
			 */
//			if (msg.arg1==1) {
//				getLocalCity();
//				loadTemperData(bundle);
//				//addNotificaction();
//				
//			}else 
				if (msg.arg1==2) {
				try {
					obj = new JSONObject(data).getJSONObject("weatherinfo");
					obj.put("imagename", obj.getString("img1"));
					serviceUpate(obj);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		};
	};
	
	/**
	 * 从本地文件中读取第一个城市作为桌面的widget显示的城市
	 */
	public  void getLocalCity(){
		String localCity = EasyApp.mAddCityPre.getString("addlocalcity", "");
		if(!TextUtils.isEmpty(localCity)){
			try {
				JSONArray localCityArray=new JSONArray(localCity);
				
				JSONObject localCityObj=localCityArray.getJSONObject(0);
				bundle.putString("city",localCityObj.getString("code"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 网络上获取天气信息
	 * @param params
	 */
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
	
	/**
	 * 更新桌面widget
	 * @param obj
	 */
	public void serviceUpate(JSONObject obj){
		AppWidgetManager manager=AppWidgetManager.getInstance(mContext);
		RemoteViews views;
		views = EasyWeatherProvider.updateVIew(mContext,obj,true);
		int[] appids=manager.getAppWidgetIds(new ComponentName(mContext, EasyWeatherProvider.class));
		manager.updateAppWidget(appids, views);
	}

}
