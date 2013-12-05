package com.easyweather.util;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import com.easyweather.logic.WeatherAPIConfig;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class HttpClient {
	
	 // 根据图片的url得到要显示的图片  
    public static Bitmap getBitmap(String url) {  
        Bitmap bm = null;// 生成了一张bmp图像  
        try {  
            URL iconurl = new URL(WeatherAPIConfig.downWeatherImg + url  
                    + ".gif");  
            URLConnection conn = iconurl.openConnection();  
            conn.connect();  
            // 获得图像的字符流  
            InputStream is = conn.getInputStream();  
            BufferedInputStream bis = new BufferedInputStream(is, 8192);  
            bm = BitmapFactory.decodeStream(bis);  
            bis.close();  
            is.close();// 关闭流  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return bm;  
    }  

}
