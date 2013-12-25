package com.easyweather.util;


public class ImageLoader {
	String IMAGE_ROOTPATH = "";// 保存图片根目录同时也是SD卡路径

	public static ImageLoader imageLoader = null;

	public ImageLoader getImageLoader() {
		if (imageLoader == null) {
			imageLoader = new ImageLoader();
		}
		return imageLoader;
	}

	public void saveImage() {

	}

}
