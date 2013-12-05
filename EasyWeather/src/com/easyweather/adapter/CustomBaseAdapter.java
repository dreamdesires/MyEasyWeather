package com.easyweather.adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class CustomBaseAdapter extends BaseAdapter {

	protected JSONArray array;
	protected JSONObject jsonObject;
	protected Context mContext;
	public LayoutInflater mLayoutinfla;

	public CustomBaseAdapter(JSONArray array, Context context) {
		this.array = array;
		this.mContext = context;
		String inflate = Context.LAYOUT_INFLATER_SERVICE;
		mLayoutinfla = (LayoutInflater) mContext.getSystemService(inflate);
	}
	
	public CustomBaseAdapter(JSONObject jsonObject, Context context) {
		this.jsonObject = jsonObject;
		this.mContext = context;
		String inflate = Context.LAYOUT_INFLATER_SERVICE;
		mLayoutinfla = (LayoutInflater) mContext.getSystemService(inflate);
	}

	@Override
	public int getCount() {
		return array.length();
	}

	@Override
	public Object getItem(int position) {
		try {
			return array.getJSONObject(position);
		} catch (JSONException e) {
			return null;
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return convertView;
	}

}
