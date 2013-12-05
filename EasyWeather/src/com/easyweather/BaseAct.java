package com.easyweather;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public abstract class BaseAct extends Activity{
	
	private TextView headrTitle;

	private ImageView headerBack;
	private Button headerComplete;
	
	public abstract void init();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public TextView getHeaderTitle(){
		if(headrTitle==null){
			headrTitle = (TextView)findViewById(R.id.tv_indextitle);
		}
		return headrTitle;
	}
	
	public ImageView getHeaderBack(){
		if(headerBack==null){
			headerBack = (ImageView)findViewById(R.id.iv_indexback);
		}
		return headerBack;
	}
	public Button getHeaderComplet(){
		if(headerComplete==null){
			headerComplete = (Button)findViewById(R.id.btn_finish);
		}
		return headerComplete;
	}
	
}
