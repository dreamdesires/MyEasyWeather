package com.easyweather;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.easyweather.adapter.ViewPagerAdapter;
import com.easyweather.util.UToast;

public class HomeAct extends BaseAct implements OnClickListener{

	private ViewPager viewpager;
	private LocalActivityManager manager;
	ArrayList<TabHolder> list=null;
	ArrayList<View> viewlist=null;
	JSONArray jsonArray=null;
	private ViewPagerAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
		manager = new LocalActivityManager(this, false);
		manager.dispatchCreate(savedInstanceState);
		
		getIntentData();
		adapter = new ViewPagerAdapter(this, viewlist);
		viewpager.setAdapter(adapter);
	}
	
	@Override
	public void init() {
		getHeaderTitle().setText(EasyApp.res.getString(R.string.watchcityweather));
		getHeaderBack().setVisibility(View.VISIBLE);
		getHeaderBack().setOnClickListener(this);
		
		getHeaderComplet().setVisibility(View.VISIBLE);
		getHeaderComplet().setOnClickListener(this);
		getHeaderComplet().setText(EasyApp.res.getString(R.string.headerbtntext));
		
		viewpager = (ViewPager)findViewById(R.id.viewpager);
		list=new ArrayList<TabHolder>();
		viewlist=new ArrayList<View>();
	}
	
	/**
	 * 获取用户定制的城市
	 */
	public void getIntentData(){
		Intent intent = getIntent();
		if(intent.hasExtra("jsonarray")&& intent.getStringExtra("jsonarray").length()>0){
			try {
				jsonArray=new JSONArray(intent.getStringExtra("jsonarray"));
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject obj=jsonArray.getJSONObject(i);
					if(obj.getBoolean("status")){
						addTab(ContentAct.class, obj, obj.getString("name"), true,i);
					}
				}
				
			} catch (JSONException e) {
				UToast.makeText(HomeAct.this, e.getMessage(),Toast.LENGTH_SHORT);
			}
		}
	}
	
	public void addActivity(Class<?> t,JSONObject obj,String title,boolean isload){
		TabHolder holder=new TabHolder(title, t, obj, isload);
		list.add(holder);
	}

	/**
	 * 设置用户定制的所有城市页面
	 * @param t
	 * @param obj
	 * @param title
	 * @return
	 */
	public void  addTab(Class<?> t,JSONObject obj,String title,boolean isload,int index){
		Intent addIntent=new Intent(HomeAct.this,t);
		addIntent.putExtra("jsonobj", obj.toString());
		//addIntent.putExtra("index", index);
		destroy(title, manager);
		View view =manager.startActivity(title, addIntent).getDecorView();
		viewlist.add(view);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_indexback:
			this.finish();
			break;
		case R.id.btn_finish:
			Intent intent=new Intent(HomeAct.this,SelectCityAct.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
			startActivity(intent);
			this.finish();
			break;

		default:
			break;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
	}
	
	
	/**
	 * 销毁activity
	 * @param id
	 * @param manager
	 * @return
	 */
    public boolean destroy(String id , LocalActivityManager manager) {

    	if(manager != null){
    		manager.destroyActivity(id, false);
    		try {
    			final Field mActivitiesField = LocalActivityManager.class.getDeclaredField("mActivities");
    			if(mActivitiesField != null){
    				mActivitiesField.setAccessible(true);
    				@SuppressWarnings("unchecked")
    				final Map<String, Object> mActivities = (Map<String, Object>)mActivitiesField.get(manager);
    				if(mActivities != null){
    					mActivities.remove(id);
    				}
    				final Field mActivityArrayField = LocalActivityManager.class.getDeclaredField("mActivityArray");
    				if(mActivityArrayField != null){
    					mActivityArrayField.setAccessible(true);
		             @SuppressWarnings("unchecked")
		             final ArrayList<Object> mActivityArray = (ArrayList<Object>)mActivityArrayField.get(manager);
		             if(mActivityArray != null){
		                 for(Object record : mActivityArray){
		                     final Field idField = record.getClass().getDeclaredField("id");
		                     if(idField != null){
		                         idField.setAccessible(true);
		                         final String _id = (String)idField.get(record);
		                         if(id.equals(_id)){
		                             mActivityArray.remove(record);
		                             break;
		                         }
		                     }
		                 }
		             }
		         }
		     }
		 } catch (Exception e) {
		     e.printStackTrace();
		 }
		
		 return true;
		}
		
		return false;
    }
}
@SuppressWarnings("rawtypes")
class TabHolder { // 源数据模型
	String title;
	Class t;
	Boolean isload;
	JSONObject obj;
	View view;

	public TabHolder(String title, Class t,JSONObject obj, boolean isload) {
		this.title = title;
		this.t = t;
		this.obj = obj;
		this.isload = isload;
	}

	public TabHolder(View view, String title) { // 直接添加渲染好的View
		this.view = view;
		this.title = title;
		this.isload = true;
	}

}
