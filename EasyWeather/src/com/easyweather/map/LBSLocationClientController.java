package com.easyweather.map;

import android.content.Context;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

public class LBSLocationClientController {

	private LocationClient mLocationClient;

	/**
	 *
	 * @param context(当前上下文对象)
	 * @param listener  1.接收异步返回的定位结果，参数是BDLocation类型参数。 
	 * 					2.接收异步返回的POI查询结果，参数是BDLocation类型参数。
	 */
	
	public LBSLocationClientController(Context context, BDLocationListener listener){
		initializeLocationClient(context,listener);
		this.initializeLocationClientOption();
	}

	private void initializeLocationClient(Context context,BDLocationListener listener) {

    	mLocationClient = new LocationClient(context);
    		//当没有注册监听函数时，无法发起网络请求。
        mLocationClient.registerLocationListener(listener);
	}
	//关闭定位同时取消监听(不发出网络请求)
	public void cancelBDLocationListener(Context context,BDLocationListener listener){
		stopLocationClient();
		mLocationClient.unRegisterLocationListener(listener);
	}
	
	
	private boolean isLocationClientPrepared() {
		return mLocationClient != null && mLocationClient.isStarted();
	}

	public void setLocationOption(LocationClientOption option){
		mLocationClient.setLocOption(option);
	}


	public void startLocationClient() {
		if(mLocationClient != null){
			mLocationClient.start();
		}
	}
	// 关闭定位SDK(关闭定位SDK。调用stop之后，设置的参数LocationClientOption仍然保留)
	public void stopLocationClient() {
		if(mLocationClient != null){
    		mLocationClient.stop();
    	}
	}

	public void requestPIOInformation() {
		if(isLocationClientPrepared()){
			//Log.i("requestPoi", "requestPoi");
			mLocationClient.requestPoi();
		}
	}


	public void requestLocationInformation() {
		if(isLocationClientPrepared()){
			Log.i("requestLocation", "requestLocation");
			//发起定位请求。请求过程是异步的，定位结果在上面的监听函数中获取。
			mLocationClient.requestLocation();
		}
	}

	//异步返回消息时，获取经纬度以及地理位置信息
	public StringBuffer buildLocationStringBuffer(BDLocation location) {
		StringBuffer sb = new StringBuffer(256);
		sb.append("\nerror code : ");
		sb.append(location.getLocType());
		sb.append("\nlatitude : ");
		sb.append(location.getLatitude());
		sb.append("\nlontitude : ");
		sb.append(location.getLongitude());
		if (location.getLocType() == BDLocation.TypeGpsLocation){// 通过GPS
			sb.append("\nspeed : ");
			sb.append(location.getSpeed());
			sb.append("\nsatellite : ");
			sb.append(location.getSatelliteNumber());
		} else if (location.getLocType() == BDLocation.TypeNetWorkLocation){//通过网络
			sb.append("\naddr : ");
			sb.append(location.getAddrStr());
		}
		return sb;
	}
	
	
	//异步返回的位置信息进行包装获取当前地理位置信息
	public LocationInfo getLocationInfo(BDLocation location){
		LocationInfo locationInfo=new LocationInfo();
		locationInfo.setLatitude(location.getLatitude());
		locationInfo.setLongitude(location.getLongitude());
		
		//判断当前的获取地理位置信息的方式(GPS、NetWork)
		if(location.getLocType() == BDLocation.TypeGpsLocation){
			locationInfo.setInfo(location.getAddrStr());
		}else if(location.getLocType() == BDLocation.TypeNetWorkLocation){
			locationInfo.setInfo(location.getAddrStr());
		}
		return locationInfo;
	}


	//获取兴趣点信息
	public StringBuffer buildPIOStringBuffer(BDLocation poiLocation) {
		StringBuffer sb = new StringBuffer(256);
		sb.append("Poi time : ");
		sb.append(poiLocation.getTime());
		sb.append("\nerror code : ");
		sb.append(poiLocation.getLocType());
		sb.append("\nlatitude : ");
		sb.append(poiLocation.getLatitude());
		sb.append("\nlontitude : ");
		sb.append(poiLocation.getLongitude());
		sb.append("\nradius : ");
		sb.append(poiLocation.getRadius());
		if (poiLocation.getLocType() == BDLocation.TypeNetWorkLocation){
			sb.append("\naddr : ");
			sb.append(poiLocation.getAddrStr());
		}
		if(poiLocation.hasPoi()){
			sb.append("\nPoi:");
			sb.append(poiLocation.getPoi());
		}else{
			sb.append("noPoi information");
		}
		return sb;
	}

    public void initializeLocationClientOption() {
   	 	LocationClientOption option = new LocationClientOption();
   	 	option.setOpenGps(true);//开启GPS
        option.setAddrType("detail");
        option.setCoorType("gcj02");
        option.setScanSpan(5000);
        option.disableCache(true);//禁止启用缓存定位
        option.setPoiNumber(5);	//最多返回POI个数
        option.setPoiDistance(5000); //poi查询距离
        option.setPoiExtraInfo(true); //是否需要POI的电话和地址等详细信息
        setLocationOption(option);
	}

}
