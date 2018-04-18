package com.max.app.kotlincrm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class TBaseAdapter<T> extends BaseAdapter {
	
	protected Context mContext;//上下文
	protected List<T> mList;//泛型
	public LayoutInflater mLayoutInflater;
	
	public TBaseAdapter(Context context, List<T> list) {
		super();
		this.mContext = context;
		this.mList = list;
		mLayoutInflater = LayoutInflater.from(context);
	}

	public TBaseAdapter() {
		super();
	}
	
	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/*
	 * 欲把以下的方法交给子类去实现
	 * */
	/*
	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		return null;
	}
	*/

}
