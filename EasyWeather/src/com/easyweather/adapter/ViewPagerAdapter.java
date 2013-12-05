/**
 * viewpager内容加载页面
 */
package com.easyweather.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

public class ViewPagerAdapter  extends PagerAdapter{
	ArrayList<View > mviewlist=null;
	Context mContext;
	public ViewPagerAdapter(Context context,ArrayList<View > viewlist){
		this.mContext=context;
		this.mviewlist=viewlist;	
		
	}

	@Override
	public int getCount() {
		return mviewlist.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		//return false;
		 return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		  ((ViewPager) container).removeView(mviewlist.get(position));
	}

	@Override
	public int getItemPosition(Object object) {
		return super.getItemPosition(object);
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return super.getPageTitle(position);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		   ((ViewPager) container).addView(mviewlist.get(position));
		   return mviewlist.get(position);
	}
	
	

}
