package com.easyweather;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.easyweather.util.ULog;
import com.easyweather.util.UToast;

public class LoadingAct extends BaseAct{
	
	private JSONArray array;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loadingmain);
		init();
	}

	@Override
	public void init() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(3000);
					forwardPage();
				} catch (InterruptedException e) {
					UToast.makeText(LoadingAct.this,e.getMessage(), Toast.LENGTH_SHORT);
				}
			}
		}).start();
	}
	
	/**
	 * 判断当前要跳转的页面
	 */
	public void forwardPage(){
		if(EasyApp.mPreCity.contains("localcity") && !TextUtils.isEmpty(EasyApp.mPreCity.getString("localcity", ""))){
			String localStr=EasyApp.mPreCity.getString("localcity", "");
			try {
				array = new JSONArray(localStr);
				Intent intent=new Intent(LoadingAct.this,HomeAct.class);
				intent.putExtra("jsonarray", array.toString());
				intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
				startActivity(intent);
				
			} catch (JSONException e) {
				ULog.e("error", e.getMessage());
			}
		}else{
			Intent intent=new Intent(LoadingAct.this,SelectCityAct.class);
			//intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
			startActivity(intent);
		}
		LoadingAct.this.finish();
	}

}
