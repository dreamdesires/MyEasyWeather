/**
 * 定位基类
 */
package com.easyweather;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.easyweather.map.BaseLocationController;
import com.easyweather.map.LocationInfo;

public abstract class BaseLocationAct extends BaseAct{
	public  boolean mapflag = false;
	public MyBDLocationListener locationListenter=null;
	//protected LBSLocationClientController mLocationClientController;
	public LocationInfo locationInfo;// 获取的地理位置信息
	protected StringBuffer loactionSb = null;
	public BaseLocationController mBaseLocation;
	public LocationCallBack  mLocationCallBack;
	int locationCount=0;//设置获取定位信息的次数
	
	public interface LocationCallBack{
		public void getLocInfo(LocationInfo locInfo);
	}
	
	
	Handler locationhandler=new Handler(){
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.arg1==100){
				mBaseLocation.stopLocation();
				mLocationCallBack.getLocInfo(locationInfo);
			}
		};
	};
	
	public void getSendLocInfo(LocationCallBack  mLocationCallBack){
		this.mLocationCallBack=mLocationCallBack;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	/**
	 * 初始化定位信息、注册监听器以及配置定位参数
	 */
	public void initMapInfo() {
		locationListenter=new MyBDLocationListener();
		mBaseLocation = new BaseLocationController(getApplicationContext(), locationListenter);
		mBaseLocation.initLocation();
		mBaseLocation.setLocationParams();
		//startLocation();
		
	}
	/**
	 * start：启动定位SDK,发起定位
	 */
	public void startLocation(){
		locationCount=0;
		mBaseLocation.startLocation();
	}
	
	
	public class MyBDLocationListener implements BDLocationListener{
		@Override
		public void onReceiveLocation(BDLocation location) {
			if(location== null){
				return;
			}
			
			Message msg = locationhandler.obtainMessage();
			//若3次内进行网络请求获取到地理位置信息,关闭关闭定位监听，停止定位
			if(locationCount<=1){
				locationInfo=mBaseLocation.getReceiveLocation(location);
				if(locationInfo!=null){
					msg.arg1=100;
					//Log.e("locationInfo", ""+"定位"+locationInfo.getInfo());
				}
			}else{//若超过3次请求未获取到信息,关闭关闭定位监听，停止定位
				msg.arg1=100;
			}
			locationCount++;
			//Log.e("locationCount", ""+locationCount);
			locationhandler.sendMessage(msg);
		}

		@Override
		public void onReceivePoi(BDLocation arg0) {
			
		}
	}
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(mBaseLocation!=null){
			mBaseLocation.stopLocation();
			mBaseLocation.destoryLocation();
			mBaseLocation=null;
		}
	}

}
