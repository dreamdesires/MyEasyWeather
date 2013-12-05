/**
 * Copyright 2013 Barfoo
 * 
 * All right reserved
 * 
 * Created on 2013-9-22 上午9:59:52
 * 
 * @author zxy
 */
package com.easyweather.util;

import android.util.Log;

public class ULog {

	/**
	 * 打开或关闭调试LOG
	 */
	private static final boolean DEBUG = true;

	public static final String TAG = "TAG";
	
	public static void v(String logmsg) {
		if (DEBUG) {
			Log.v(TAG, logmsg);
		}
	}

	public static void v(String logname, String logmsg) {
		if (DEBUG) {
			Log.v(logname, logmsg);
		}
	}

	
	public static void d(String logmsg) {
		if (DEBUG) {
			Log.d(TAG, logmsg);
		}
	}

	public static void d(String logname, String logmsg) {
		if (DEBUG) {
			Log.d(logname, logmsg);
		}
	}

	
	public static void i(String logmsg) {
		if (DEBUG) {
			Log.i(TAG, logmsg);
		}
	}

	public static void i(String logname, String logmsg) {
		if (DEBUG) {
			Log.i(logname, logmsg);
		}
	}

	
	public static void w(String logmsg) {
		if (DEBUG) {
			Log.w(TAG, logmsg);
		}
	}

	public static void w(String logname, String logmsg) {
		if (DEBUG) {
			Log.w(logname, logmsg);
		}
	}


	public static void e(String logmsg) {
		if (DEBUG) {
			Log.e(TAG, logmsg);
		}
	}

	public static void e(String logname, String logmsg) {
		if (DEBUG) {
			Log.e(logname, logmsg);
		}
	}

}