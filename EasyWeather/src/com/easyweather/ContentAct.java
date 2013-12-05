package com.easyweather;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.easyweather.logic.WeatherAPIConfig;
import com.easyweather.ui.LoadDialog;
import com.easyweather.util.DataUtil;
import com.easyweather.util.HttpClient;
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
				JSONObject obj=new JSONObject(intent.getStringExtra("jsonobj"));
				//tv_title.setText(String.format("%s:%s", EasyApp.res.getString(R.string.curcity),obj.getString("name")));
				Bundle bundle=new Bundle();
				bundle.putString("city",obj.getString("code"));
				loadTemperData(bundle);
			} catch (JSONException e) {
				UToast.makeText(ContentAct.this, e.getMessage(), Toast.LENGTH_SHORT);
			}
		}
	}
	
	
	
	public void loadTemperData(Bundle params){
		String URL=DataUtil.getDataUtil().buildURL(WeatherAPIConfig.getWeatherInfo,params);
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(URL,new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String content) {
						super.onSuccess(content);
						Log.e("content", content);
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
			img.setImageBitmap(bitmap);
		}
	}
}
