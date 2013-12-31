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
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.easyweather.EasyApp;
import com.easyweather.HomeAct;
import com.easyweather.R;
import com.easyweather.util.ULog;
import com.easyweather.widget.EasyWeatherProvider;

public class CityAdapter extends CustomBaseAdapter {

	private JSONArray saveArray;//传递到content内容页面的数组(只有选中的数据)
	private JSONArray array = null;
	private JSONArray alllocalArray;
	JSONArray contentArray=null;//临时数组

	public CityAdapter(JSONArray array, Context context) {
		super(array, context);
		this.array = array;
		alllocalArray = array;
		readAddCity();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = convertView;
		HomeViewHolder holder = null;

		if (view == null) {
			view = mLayoutinfla.inflate(R.layout.gditem, null);
			holder = new HomeViewHolder(view);
			view.setTag(holder);
		} else {
			holder = (HomeViewHolder) view.getTag();
		}

		final JSONObject cityObj;
		try {
			cityObj = array.getJSONObject(position);
			final int curposition = position;

			holder.getAddress().setText(cityObj.getString("name"));
			boolean statusFlag = cityObj.getBoolean("status");
			holder.getAddressStatus().setOnCheckedChangeListener(null);
			holder.getAddressStatus().setChecked(statusFlag);

			holder.getAddressStatus().setOnCheckedChangeListener(
					new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
							try {
								JSONObject temp = new JSONObject();
								temp.put("status", isChecked);
								temp.put("name", cityObj.getString("name"));
								temp.put("code", cityObj.getString("code"));
								temp.put("codeindex", curposition);
								if (isChecked) {
									saveArray.put(temp);
								}else{
									for (int i = 0; i < saveArray.length(); i++) {
										JSONObject setTemp=saveArray.getJSONObject(i);
										if(setTemp.getString("name").equals(temp.getString("name")) && !temp.getBoolean("status")){
											saveArray.put(i, temp);
										}
									}
									getContentArray(saveArray);
								}
								
								saveAddLocalCity(saveArray);
								alllocalArray.put(curposition, temp);
								saveAllLocalCity(alllocalArray);

							} catch (JSONException e) {
								ULog.e("error", e.getMessage());
							}
							getCurLocalCity();
						}
					});

			holder.getAddress().setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mContext, HomeAct.class);
					intent.putExtra("jsonarray", saveArray.toString());
					intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
					mContext.startActivity(intent);
					((Activity) mContext).finish();
				}
			});

		} catch (JSONException e) {
			ULog.e("error", "CityAdapter:" + e.getMessage());
		}
		return view;
	}
	
	/**
	 * 过滤保存在本地选中的城市
	 * @param array
	 */
	public void getContentArray(JSONArray array){
		contentArray=new JSONArray();
		try {
			for (int i = 0; i < array.length(); i++) {
				JSONObject temp=array.getJSONObject(i);
				if(temp.getBoolean("status")){
					contentArray.put(temp);
				}
			}
			saveArray=contentArray;
		} catch (JSONException e) {
			ULog.e("error", e.getMessage());
		}
	}
	

	/**
	 * 更新桌面widget
	 * 
	 * @param obj
	 */
	public void serviceUpate(JSONObject obj) {
		AppWidgetManager manager = AppWidgetManager.getInstance(mContext);
		RemoteViews views;
		views = EasyWeatherProvider.updateVIew(mContext, obj, true);
		int[] appids = manager.getAppWidgetIds(new ComponentName(mContext,EasyWeatherProvider.class));
		manager.updateAppWidget(appids, views);
	}

	/**
	 * 获取更换首选城市的在桌面widget的实时天气
	 */
	JSONObject curWidgetCity = null;

	public void getCurLocalCity() {
		String defualtCity = "";
		if(EasyApp.mAddCityPre.contains("addlocalcity")){
			String cityStr=EasyApp.mAddCityPre.getString("addlocalcity", "");
			try {
				JSONArray cityArray=new JSONArray(cityStr);
				defualtCity=cityArray.getJSONObject(0).getString("name");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		try {

			if (EasyApp.mPreWeather.contains("weatherInfo")&& !TextUtils.isEmpty(EasyApp.mPreWeather.getString("weatherInfo", ""))) {
				String weather = EasyApp.mPreWeather.getString("weatherInfo","");
				String splite = weather.replaceAll("null,", "");
				JSONArray weatherArray;

				weatherArray = new JSONArray(splite);
				for (int i = 0; i < weatherArray.length(); i++) {
					JSONObject tempObj = weatherArray.getJSONObject(i);
					if (tempObj.getString("city").equals(defualtCity)) {
						serviceUpate(tempObj);
						break;
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 保存所有城市信息
	 * 
	 * @param jsonArray
	 */
	public void saveAllLocalCity(JSONArray jsonArray) {
		Editor editor = EasyApp.mPreCity.edit();
		editor.putString("alllocalcity", jsonArray.toString());
		editor.commit();
	}

	/**
	 * 保存城市本地的选中城市信息
	 * 
	 * @param jsonArray
	 */
	public void saveAddLocalCity(JSONArray jsonArray) {
		Editor editor = EasyApp.mAddCityPre.edit();
		editor.putString("addlocalcity", jsonArray.toString());
		editor.commit();
		ULog.e("addlocalcity", jsonArray.toString());
	}

	/**
	 * 读取用户以保存的城市信息
	 */
	public void readAddCity() {
		if (EasyApp.mAddCityPre.contains("addlocalcity")) {
			String addlocalStr = EasyApp.mAddCityPre.getString("addlocalcity","");
			try {
				saveArray = new JSONArray(addlocalStr);
				//trueArray=saveArray;
			} catch (JSONException e) {
				ULog.e("error", "CityAdapter:" + e.getMessage());
			}
		} else {
			saveArray = new JSONArray();
		}
	}
	
	public void updateLayout(JSONArray jsonArray) {
		this.array = jsonArray;
		notifyDataSetChanged();
	}

	public JSONArray getDataArray() {
		return saveArray;
	}
}

class HomeViewHolder {
	View view;
	TextView tvAddress;
	CheckBox selectStatus;

	public HomeViewHolder(View view) {
		this.view = view;
	}

	public TextView getAddress() {
		if (tvAddress == null) {
			tvAddress = (TextView) view.findViewById(R.id.tv_address);
		}
		return tvAddress;
	}

	public CheckBox getAddressStatus() {
		if (selectStatus == null) {
			selectStatus = (CheckBox) view.findViewById(R.id.iv_status);
		}
		return selectStatus;
	}
}
