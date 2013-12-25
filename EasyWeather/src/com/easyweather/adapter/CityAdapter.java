package com.easyweather.adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RemoteViews;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.easyweather.EasyApp;
import com.easyweather.HomeAct;
import com.easyweather.R;
import com.easyweather.util.ULog;
import com.easyweather.widget.EasyWeatherProvider;

public class CityAdapter extends CustomBaseAdapter{

	private JSONArray saveArray;

	public CityAdapter(JSONArray array, Context context) {
		super(array, context);
		getAddressArray();
	}
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View view =convertView;
		HomeViewHolder holder=null;
		
		if(view==null){
			view =mLayoutinfla.inflate(R.layout.gditem, null);
			holder=new HomeViewHolder(view);
			view.setTag(holder);
		}else{
			holder=(HomeViewHolder)view.getTag();
		}
		
		final JSONObject cityObj;
		try {
			cityObj = array.getJSONObject(position);
			final int curposition=position;
			
			holder.getAddress().setText(cityObj.getString("name"));
			boolean statusFlag=cityObj.getBoolean("status");
			holder.getAddressStatus().setOnCheckedChangeListener(null); 
			holder.getAddressStatus().setChecked(statusFlag);
			
			holder.getAddressStatus().setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					try {
						JSONObject temp=new JSONObject();
						temp.put("status", isChecked);
						temp.put("name", cityObj.getString("name"));
						temp.put("code", cityObj.getString("code"));
						temp.put("codeindex", curposition);
						//ULog.e("CityAdapter", "CityAdapter:"+curposition);
						saveArray.put(curposition, temp);
						saveLocalCity(saveArray);
						
						
					} catch (JSONException e) {
						ULog.e("error", e.getMessage());
					}
				}
			});
			
			holder.getAddress().setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent=new Intent(mContext,HomeAct.class);
					intent.putExtra("jsonarray", saveArray.toString());
					intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
					mContext.startActivity(intent);
					((Activity)mContext).finish();
				}
			});

		} catch (JSONException e) {
			ULog.e("error", "CityAdapter:"+e.getMessage());
		}
		return view;
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

	/**
	 * 获取当前首选城市的天气
	 */
	JSONObject curWidgetCity=null;
	public void  getCurLocalCity(JSONArray array){
		String defualtCity="";
		try {
			for (int i = 0; i < array.length(); i++) {
				JSONObject tempCityObj=array.getJSONObject(i);
				if(tempCityObj.getBoolean("status")){
					defualtCity=tempCityObj.getString("name");
					break;
				}
			}
			
			if(EasyApp.mPreWeather.contains("weatherInfo") && !TextUtils.isEmpty(EasyApp.mPreWeather.getString("weatherInfo", ""))){
				String weather=EasyApp.mPreWeather.getString("weatherInfo", "");
				String splite=weather.replaceAll("null,", "");
				JSONArray weatherArray;
				
					weatherArray = new JSONArray(splite);
					for (int i = 0; i < weatherArray.length(); i++) {
						JSONObject tempObj=weatherArray.getJSONObject(i);
						if(tempObj.getString("city").equals(defualtCity)){
							serviceUpate(tempObj);
							break;
						}
					}
				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 保存选择的城市
	 * @param jsonArray
	 */
	public void saveLocalCity(JSONArray jsonArray){
		Editor editor = EasyApp.mPreCity.edit();
		editor.putString("localcity", jsonArray.toString());
		editor.commit();
		getCurLocalCity(jsonArray);
	}

	public void updateLayout(JSONArray jsonArray){
		this.array=jsonArray;
		notifyDataSetChanged();
	}
	
	public void getAddressArray(){
		saveArray = this.array;
	}

	public JSONArray getDataArray(){
		return saveArray;
	}
}

class HomeViewHolder{
	View view ;
	TextView tvAddress;
	CheckBox selectStatus;
	public HomeViewHolder (View view){
		this.view=view;
	}
	
	public TextView getAddress(){
		if(tvAddress==null){
			tvAddress=(TextView)view.findViewById(R.id.tv_address);
		}
		return tvAddress;
	}
	
	public CheckBox getAddressStatus(){
		if(selectStatus==null){
			selectStatus=(CheckBox)view.findViewById(R.id.iv_status);
		}
		return selectStatus;
	}
}
