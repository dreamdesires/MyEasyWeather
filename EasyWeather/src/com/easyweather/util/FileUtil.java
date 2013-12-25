package com.easyweather.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;

public class FileUtil {
	
	//private  String IMAGE_PATH="";

	public String IMAGE_ROOTPATH=getSDPath()+"/weatherimage/";
	
	public static FileUtil fileUtil=null;
	
	public static FileUtil getFile(){
		if (fileUtil==null){
			fileUtil=new FileUtil();
		} 
		return fileUtil;
	}
	
	/**
	 * 判断SD卡是否存在
	 * 
	 * @return
	 */
	public  boolean ExistSDCard() {
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			return true;
		} else
			return false;
	}

	/**
	 * 获取SD卡剩余空间
	 * 
	 * @return
	 */
	public  long getSDFreeSize() {
		// 取得SD卡文件路径
		File path = Environment.getExternalStorageDirectory();
		StatFs sf = new StatFs(path.getPath());
		// 获取单个数据块的大小(Byte)
		long blockSize = sf.getBlockSize();
		// 空闲的数据块的数量
		long freeBlocks = sf.getAvailableBlocks();
		// 返回SD卡空闲大小
		// return freeBlocks * blockSize; //单位Byte
		// return (freeBlocks * blockSize)/1024; //单位KB
		return (freeBlocks * blockSize) / 1024 / 1024; // 单位MB
	}

	/**
	 * SD卡总容量
	 * 
	 * @return
	 */
	public  long getSDAllSize() {
		// 取得SD卡文件路径
		File path = Environment.getExternalStorageDirectory();
		StatFs sf = new StatFs(path.getPath());
		// 获取单个数据块的大小(Byte)
		long blockSize = sf.getBlockSize();
		// 获取所有数据块数
		long allBlocks = sf.getBlockCount();
		// 返回SD卡大小
		// return allBlocks * blockSize; //单位Byte
		// return (allBlocks * blockSize)/1024; //单位KB
		return (allBlocks * blockSize) / 1024 / 1024; // 单位MB
	}
	
	/**
	 * 获取SD卡的sdDir路径
	 * 
	 * @return
	 */

	public  String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
		}
		return sdDir.toString();
	}

	
	public  String getImagePath(String imagename){
		String imagepath="";
		if(ExistSDCard() && getSDFreeSize()>=1){//存在SD卡同时，SD卡剩余空间>=1M
			imagepath= IMAGE_ROOTPATH+imagename+".png";
		}
		return imagepath;
	}
	BufferedOutputStream bos;
	
	public  boolean saveSDCard(String imagename,Bitmap bitmap){
		if (!TextUtils.isEmpty(IMAGE_ROOTPATH)) {
			 File filePath = new File(IMAGE_ROOTPATH);
	            if (!filePath.exists()) {
	                filePath.mkdirs();
	            }
	            File myCaptureFile = new File(getImagePath(imagename));
				try {
				    FileOutputStream fos = new FileOutputStream(myCaptureFile);
				    bitmap.compress(CompressFormat.JPEG, 50, fos);
				    fos.flush();  
				    fos.close();  
				} catch (FileNotFoundException e) {
					ULog.e("error", e.getMessage());
					return false;
				} catch (IOException e) {
					ULog.e("error", e.getMessage());
					return false;
				}  
		}
		return true;
	}
	
	
	public Bitmap getSDImage(String imagepath){
		Bitmap bitmap=null;
		File file = new File(imagepath);
        if (file.exists()) {
        	bitmap=BitmapFactory.decodeFile(imagepath);
        }
        return bitmap;
	}
}
