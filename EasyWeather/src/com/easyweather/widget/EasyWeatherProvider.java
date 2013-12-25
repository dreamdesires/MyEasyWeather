package com.easyweather.widget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.RemoteViews;

import com.easyweather.EasyApp;
import com.easyweather.R;
import com.easyweather.SelectCityAct;
import com.easyweather.util.FileUtil;

public class EasyWeatherProvider extends AppWidgetProvider{
	Context mContext;
	android.widget.RemoteViews remote=null;
	JSONObject curWidgetCity=null;
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		this.mContext=context;
		getCurLocalCity();
		//ULog.e("onUpdate","onUpdate");
		for (int i = 0; i < appWidgetIds.length; i++) {
			int appWidgetId=appWidgetIds[i];
			if(EasyApp.mPreWeather.contains("weatherInfo") && !TextUtils.isEmpty(EasyApp.mPreWeather.getString("weatherInfo", ""))){
				String weather=EasyApp.mPreWeather.getString("weatherInfo", "");
				try {
						
						String splite=weather.replaceAll("null,", "");
						JSONArray array=new JSONArray(splite);
						for (int j = 0; j < array.length(); j++) {
								JSONObject obj=array.getJSONObject(j);
								if(curWidgetCity !=null && obj.getString("city").equals(curWidgetCity.getString("name"))){
									remote=updateVIew(mContext,obj,false);
									Intent dIntent = new Intent(mContext,SelectCityAct.class);
									PendingIntent pendingIntent=PendingIntent.getActivity(mContext, 200, dIntent, 0);
									remote.setOnClickPendingIntent(R.id.lin_view, pendingIntent);
									break;
								}
							}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			appWidgetManager.updateAppWidget(appWidgetId, remote);
		}
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		//ULog.e("onReceive","onReceive");
		super.onReceive(context, intent);  
	}
	
	/**
	 * 更新桌面widget组件
	 * @param context
	 * @param params
	 * @return
	 */
	public static RemoteViews updateVIew(Context context,JSONObject obj,boolean falg) {
		android.widget.RemoteViews remote = new android.widget.RemoteViews(context.getPackageName(), R.layout.ewidesktopitem);
		try {
			if(falg){
				remote.setImageViewBitmap(R.id.iv_curWeatherImg,FileUtil.getFile().getSDImage(FileUtil.getFile().getImagePath(obj.getString("img1"))));
			}else{
				remote.setImageViewBitmap(R.id.iv_curWeatherImg,FileUtil.getFile().getSDImage(FileUtil.getFile().getImagePath(obj.getString("imagename"))));
			}
			remote.setTextViewText(R.id.tv_curWeathertemp, obj.getString("temp1"));
			remote.setTextViewText(R.id.tv_curweek, obj.getString("week"));
			remote.setTextViewText(R.id.tv_curmonth, obj.getString("date_y"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return remote;
	}
	
	/**
	 * 获取当前首选城市的天气
	 */
	public void  getCurLocalCity(){
		if(EasyApp.mPreCity.contains("localcity")){
			String defaultCity = EasyApp.mPreCity.getString("localcity", "");
			try {
				JSONArray defaultArray=new JSONArray(defaultCity);
				for (int i = 0; i < defaultArray.length(); i++) {
					JSONObject defaultObj=defaultArray.getJSONObject(i);
					if(defaultObj.getBoolean("status")){
						curWidgetCity=defaultObj;
						break;
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
}
