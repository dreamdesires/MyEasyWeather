package com.easyweather.util;

import com.easyweather.EasyApp;

import android.os.Bundle;
import android.util.Log;

public class DataUtil {
	
	public static DataUtil dataUtil=null;
	
	public static DataUtil getDataUtil(){
		if (dataUtil==null) {
			dataUtil=new DataUtil();
		}
		return dataUtil;
	}
	
	/**
	 * 构建URL
	 * @param host
	 * @param params
	 * @return
	 */
	public String buildURL(String host,Bundle params){
		StringBuffer sBuffer=new StringBuffer();
		
		sBuffer.append(host);
		
		if(params!=null){
			for (String key : params.keySet()) {
				if (params.getString(key) != null && !params.getString(key).equals("")) {
					sBuffer.append(params.getString(key));
				}
			}
		}
		sBuffer.append(".html");
		Log.e("URL", sBuffer.toString());
		return sBuffer.toString();
	}
	
	public String getResStr(int resString){
		return EasyApp.res.getString(resString);
	}
	

	
}
