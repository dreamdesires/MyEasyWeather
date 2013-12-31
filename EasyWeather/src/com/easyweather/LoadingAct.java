package com.easyweather;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.easyweather.util.ULog;
import com.easyweather.util.UToast;
import com.easyweather.widget.UpdateService;

public class LoadingAct extends BaseAct{
	
	private JSONArray array;
	private LinearLayout load_lin;
	private NotificationManager notifyManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loadingmain);
		init();
	}

	@Override
	public void init() {
		 notifyManager = (NotificationManager)this.getSystemService(NOTIFICATION_SERVICE);

		 notifyManager.cancel(1);   
		load_lin = (LinearLayout)findViewById(R.id.load_lin);
		/**加载透明动画**/
	    Animation mAnimation = AnimationUtils.loadAnimation(this, R.anim.welcome_alpha);  
		load_lin.setAnimation(mAnimation);
		load_lin.startAnimation(mAnimation);
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(5000);
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
		if(EasyApp.mPreCity.contains("alllocalcity") && !TextUtils.isEmpty(EasyApp.mPreCity.getString("alllocalcity", ""))){
			String localStr=EasyApp.mPreCity.getString("alllocalcity", "");
			
			try {
				array = new JSONArray(localStr);
				Intent intent=new Intent(LoadingAct.this,HomeAct.class);
				intent.putExtra("jsonarray", array.toString());
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT); 
				intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
				startActivity(intent);
				
				Intent serviceIntent=new Intent(LoadingAct.this,UpdateService.class);
				startService(serviceIntent);
				
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
