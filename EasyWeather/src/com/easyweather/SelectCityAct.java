/**
 * 选择城市
 * 功能:进入程序后，进行自动定位:
 * 定位成功将当前定位的地区作为默认选择的城市
 * 定位失败，提示定位失败，请选择城市
 */
package com.easyweather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.easyweather.adapter.CityAdapter;
import com.easyweather.map.LocationInfo;
import com.easyweather.util.ULog;
import com.easyweather.util.UToast;
import com.easyweather.widget.UpdateService;

public class SelectCityAct extends BaseLocationAct implements OnItemClickListener,OnClickListener{
	
	private JSONArray array;
	//JSONArray saveArray=null;
	private GridView gd;
	private CityAdapter adapter;
	String code=null;
	private TextView curLocation;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.selectcitymain);
		readLocalCity();
		init();
		
		if(EasyApp.isLocation){//定位暂时没有添加定位功能
			initMapInfo();
			startLocation();
			getSendLocInfo(new LocationCallBack() {
				
				@Override
				public void getLocInfo(LocationInfo locInfo) {
					if(locInfo!=null){
						//定位成功
						dataMatch(locationInfo.getInfo());
						curLocation.setVisibility(View.VISIBLE);
					}
				}
			});
		}else{//不定位直接显示
			curLocation.setVisibility(View.GONE);
			UToast.makeText(SelectCityAct.this,EasyApp.res.getString(R.string.addcity), Toast.LENGTH_SHORT);
			adapter = new CityAdapter(array, SelectCityAct.this);
			gd.setAdapter(adapter);
		}
	}

	@Override
	public void init() {
		getHeaderTitle().setText(EasyApp.res.getString(R.string.addcity));
		curLocation = (TextView)findViewById(R.id.tv_location);
		gd = (GridView)findViewById(R.id.gd);
		getHeaderBack().setVisibility(View.VISIBLE);
		getHeaderBack().setOnClickListener(this);
		getHeaderComplet().setVisibility(View.VISIBLE);
		getHeaderComplet().setOnClickListener(this);
		getHeaderComplet().setText(EasyApp.res.getString(R.string.complete));
	}
	
	public void dataMatch(String cityStr){
		for (int i = 0; i < array.length(); i++) {
			JSONObject obj;
			try {
				obj = array.getJSONObject(i);
				if(cityStr.contains(obj.getString("name"))){
					code=obj.getString("code");
					obj.put("status", true);
					//设置当前所在的城市
					curLocation.setText(String.format("%s%s",EasyApp.res.getString(R.string.curcity),obj.getString("name")));
					array.put(i, obj);
				}
			} catch (JSONException e) {
				UToast.makeText(SelectCityAct.this, e.getMessage(), Toast.LENGTH_SHORT);
			}
		}
	}

	public JSONArray getAddressArray(){
		array = new JSONArray();
		String[] addressCode=getResources().getStringArray(R.array.addresscode);
		String[] addressName=getResources().getStringArray(R.array.addressname);
		
		for (int i = 0; i < addressName.length; i++) {
			try {
				JSONObject obj=new JSONObject();
				obj.put("status",false);
				obj.put("code", addressCode[i]);//城市代码
				obj.put("name", addressName[i]);//城市名称
				obj.put("index",i);
				//saveArray.put(obj);
				array.put(obj);
			} catch (JSONException e) {
				UToast.makeText(SelectCityAct.this, e.getMessage(), Toast.LENGTH_SHORT);
			}
		}
		return array;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		JSONObject obj=(JSONObject)adapter.getItem(arg2);
		boolean curstatus=false;
		try {
			curstatus =obj.getBoolean("status");
			if(curstatus){
				curstatus=false;
			}else{
				curstatus=true;
			}
			obj.put("status",curstatus);
			//saveArray.put(arg2, obj);
			array.put(arg2, obj);
			//saveLocalCity(saveArray);
			adapter.updateLayout(array);
			
			
		} catch (JSONException e) {
			UToast.makeText(SelectCityAct.this, e.getMessage(), Toast.LENGTH_SHORT);
		}
	}
	
	
	/**
	 * 1.若本地存在保存的选中的城市，则首先显示
	 * 2.若本地不存在显示全部城市，并将所有城市显示同时设置所有的城市是未选中状态
	 */
	public void readLocalCity(){
		String localStr=EasyApp.mPreCity.getString("alllocalcity", "");
	
		if (!TextUtils.isEmpty(localStr)) {
			try {
				array=new JSONArray(localStr);
				
			} catch (JSONException e) {
				ULog.e("error", e.getMessage());
			}
		}else{
			getAddressArray();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_indexback:
			SelectCityAct.this.finish();
			break;

		case R.id.btn_finish:
			//SelectCityAct.this.finish();
			Intent intent=new Intent(SelectCityAct.this,HomeAct.class);
			intent.putExtra("jsonarray", adapter.getDataArray().toString());
			intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
			startActivity(intent);
			SelectCityAct.this.finish();
			
			Intent serviceIntent=new Intent(SelectCityAct.this,UpdateService.class);
			stopService(serviceIntent);
			startService(serviceIntent);
			break;
		default:
			break;
		}
	}

}
