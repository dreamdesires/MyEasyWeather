package com.easyweather.map;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

public class BaseLocationController {
	Context mContext; 
	BDLocationListener mListener;
	private LocationClient mLocationClient;
	private LocationInfo locationInfo;
	String TAG="location";
	
	public BaseLocationController(Context context, BDLocationListener listener){
		this.mContext=context;
		this.mListener=listener;
	}
	
	/**
	 * 初始化
	 */
	public void initLocation(){
		locationInfo = new LocationInfo();
		mLocationClient = new LocationClient(mContext);
		//mLocationClient.registerLocationListener(mListener);
	}
	
	public void startLocation(){
		mLocationClient.registerLocationListener(mListener);
		if(mLocationClient!=null && !mLocationClient.isStarted()){
			mLocationClient.start();  
			mLocationClient.requestLocation();
		}
	}
	
	public void stopLocation(){
		if(mLocationClient!=null){
			mLocationClient.unRegisterLocationListener(mListener);
			mLocationClient.stop();
		}
	}
	
	public void destoryLocation(){
		mLocationClient = null; 
	}
	
	public LocationInfo getReceiveLocation(BDLocation location){
		StringBuffer sb = new StringBuffer(256);
		sb.append("time : ");
		sb.append(location.getTime());
		//Log.e(TAG, ""+location.getTime());
		sb.append("\nerror code : ");
		sb.append(location.getLocType());
		//Log.e(TAG, ""+location.getLocType());
		sb.append("\nlatitude : ");
		sb.append(location.getLatitude());
		//Log.e(TAG, ""+location.getLatitude());
		sb.append("\nlontitude : ");
		sb.append(location.getLongitude());
		//Log.e(TAG, ""+location.getLongitude());
		sb.append("\nradius : ");
		sb.append(location.getRadius());
		//Log.e(TAG, ""+location.getRadius());
		if (location.getLocType() == BDLocation.TypeGpsLocation){
			sb.append("\nspeed : ");
			sb.append(location.getSpeed());
			sb.append("\nsatellite : ");
			sb.append(location.getSatelliteNumber());
			
		} else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
			sb.append("\naddr : ");
			sb.append(location.getAddrStr());
			locationInfo.setInfo(location.getAddrStr());
			locationInfo.setLatitude(location.getLatitude());
			locationInfo.setLongitude(location.getLongitude());
			//Log.e(TAG, ""+location.getAddrStr());
		}
		
		if(locationInfo.getInfo()!=null){
			return locationInfo;
		}else{
			return null;
		}
	}
	
	
	/**
	 * 设置定位参数
	 */
	public void setLocationParams(){
		LocationClientOption option=new LocationClientOption();
		option.setOpenGps(true);
		option.setAddrType("all");//返回的定位结果包含地址信息
		option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02
		//option.setPriority(LocationClientOption.NetWorkFirst);  //设置定位优先级 
		option.setPriority(LocationClientOption.GpsFirst);
		//option.setScanSpan(5000);//设置发起定位请求的间隔时间为5000ms
		option.setScanSpan(3000);//设置发起定位请求的间隔时间为5000ms
		option.disableCache(true);//禁止启用缓存定位
		mLocationClient.setLocOption(option);
	}
	

}
