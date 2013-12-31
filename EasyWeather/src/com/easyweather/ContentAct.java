package com.easyweather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.easyweather.logic.WeatherAPIConfig;
import com.easyweather.ui.LoadDialog;
import com.easyweather.util.DataUtil;
import com.easyweather.util.FileUtil;
import com.easyweather.util.HttpClient;
import com.easyweather.util.ULog;
import com.easyweather.util.UToast;
import com.ta.util.http.AsyncHttpClient;
import com.ta.util.http.AsyncHttpResponseHandler;

public class ContentAct extends BaseAct{
	
	private TextView tv_date;//日期
	private TextView tv_temperature;//当成城市的温度
	private ImageView iv_weather;//当前天气图片
	private TextView tv_wellbeing;//当前天气的描述
	String  data=null;
	private TextView tv_week;
	private TextView tv_tab;
	private LoadDialog loadDialog;
	private int curposition;
	JSONObject cityobj=null;
	public  static JSONArray weatherArray=new JSONArray();
	
	public void saveLocalCityWeather(JSONObject weatherObj){
		Editor editor = EasyApp.mPreWeather.edit();
		try {
			weatherArray.put(curposition, weatherObj);
			editor.putString("weatherInfo", weatherArray.toString());
			editor.commit();
			//ULog.e("success", "success!");
		} catch (JSONException e) {
			ULog.e("error", "ContentAct:"+e.getMessage());
		}
		//ULog.e("weatherArray", weatherArray.toString());
	}

	Handler handler=new Handler(){

		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if(msg.arg1==1){
				if(!TextUtils.isEmpty(data)){
					try {
						JSONObject obj=new JSONObject(data).getJSONObject("weatherinfo");
						tv_tab.setText(obj.getString("city"));
						tv_date.setText(obj.getString("date_y"));//年月
						tv_week.setText(obj.getString("week"));//星期
						//温度
						tv_temperature.setText(String.format("%s:%s",EasyApp.res.getString(R.string.curcityweather),obj.getString("temp1")));
						loadImage(iv_weather,obj.getString("img1"));//设置当前天气图片
						tv_wellbeing.setText(obj.getString("weather1"));//天气描述
						obj.put("imagename", obj.getString("img1"));
						
						//ULog.e("imagename", FileUtil.getFile().getImagePath(obj.getString("img1")));
						saveLocalCityWeather(obj);
						
						//if(indexStr.equals("0")){
							//Intent intent = new Intent();
							//intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
							////要发送的内容
							//Bundle bundle=new Bundle();
							//bundle.putString("month", obj.getString("date_y"));
							//bundle.putString("week", obj.getString("week"));
							//bundle.putString("temp", obj.getString("weather1"));
							//intent.putExtra("weatherInfo", bundle);
							//发送 一个无序广播
							//ContentAct.this.sendBroadcast(intent);
						//}
					} catch (JSONException e) {
						
						UToast.makeText(ContentAct.this, e.getMessage(), Toast.LENGTH_SHORT);
					}
				}
				loadDialog.close();
			}
		};
	};
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabcontent);
		loadDialog = new LoadDialog(this);
		init();
		getIntentData();
	}

	@Override
	public void init() {
		tv_tab = (TextView)findViewById(R.id.tv_tab);
		tv_date = (TextView)findViewById(R.id.tv_date);
		tv_week = (TextView)findViewById(R.id.tv_week);
		tv_temperature = (TextView)findViewById(R.id.tv_temperature);
		iv_weather = (ImageView)findViewById(R.id.iv_weather);
		tv_wellbeing = (TextView)findViewById(R.id.tv_wellbeing);
	}

	public void getIntentData(){
		Intent intent = getIntent();
		if (intent.hasExtra("jsonobj")) {
			try {
				cityobj=new JSONObject(intent.getStringExtra("jsonobj"));
				curposition = cityobj.getInt("codeindex");
				Bundle bundle=new Bundle();
				bundle.putString("city",cityobj.getString("code"));
				
				loadTemperData(bundle);
			} catch (JSONException e) {
				UToast.makeText(ContentAct.this, e.getMessage(), Toast.LENGTH_SHORT);
			}
		}
	}
	
	
	/**
	 * 从网络上获取当前城市的天气信息
	 * @param params
	 */
	public void loadTemperData(Bundle params){
		String URL=DataUtil.getDataUtil().buildURL(WeatherAPIConfig.getWeatherInfo,params);
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(URL,new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String content) {
						super.onSuccess(content);
						//Log.e("content", content);
						data=content;
						Message msg=handler.obtainMessage();
						msg.arg1=1;
						handler.sendMessage(msg);
					}
					
					@Override
					public void onStart() {
						super.onStart();
						loadDialog.open();
					}
					@Override
					public void onFinish() {
						super.onFinish();
						loadDialog.close();
					}
				});
	}
	
	/**
	 * 获取当前天气的图片
	 * @param img
	 * @param imgurl
	 */
	public void loadImage(ImageView img,String imgurl){
		//http://m.weather.com.cn/img/b1.gif
		Bitmap bitmap =HttpClient.getBitmap(imgurl);
		if(bitmap!=null){
			
			FileUtil.getFile().saveSDCard(imgurl, bitmap);
			img.setImageBitmap(FileUtil.getFile().getSDImage(FileUtil.getFile().getImagePath(imgurl)));
			//img.setImageBitmap(bitmap);
			//UToast.makeText(ContentAct.this, "保存图片成功", Toast.LENGTH_SHORT);
		}
	}
}
