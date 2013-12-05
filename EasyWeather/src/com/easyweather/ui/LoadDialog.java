package com.easyweather.ui;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.easyweather.R;

/**
 * @author dream ?? 这个功能不建议这样处理，可以套装成一个公用的方法，没有必要每次都实例化这个
 */
public class LoadDialog {

	Activity context;
	Context mContext;

	android.app.AlertDialog load_dialog;

	static android.app.AlertDialog ldialog;

	public LoadDialog(Activity context) {
		this.context = context;
	}

	// 打开正在登录dialog
	public void openCancelable() {
		try {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			LinearLayout linear = (LinearLayout) context.getLayoutInflater().inflate(R.layout.logindialog, null);
			linear.setVisibility(View.VISIBLE);
			builder.setView(linear);
			builder.setCancelable(false);
			builder.create();
			load_dialog = builder.show();
		} catch (Exception e) {
			Log.i("msg", e.getMessage());
		}
	}

	// 打开正在登录dialog
	public void open() {
		try {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			LinearLayout linear = (LinearLayout) context.getLayoutInflater().inflate(R.layout.logindialog, null);
			linear.setVisibility(View.VISIBLE);
			builder.setView(linear);
			builder.create();
			builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
				@Override
				public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
					close();
					return false;
				}
			});
			load_dialog = builder.show();
		} catch (Exception e) {
			Log.i("msg", e.getMessage());
		}
	}

	// 关闭正在登录dialog
	public void close() {
		try {
			if (load_dialog != null && load_dialog.isShowing()) {
				load_dialog.dismiss();
			}
		} catch (Exception e) {
			Log.i("msg", "error:" + e.getMessage());
		}
	}

}
