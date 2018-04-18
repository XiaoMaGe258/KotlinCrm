package com.max.app.kotlincrm.utils;

import android.util.Log;

import com.max.app.kotlincrm.api.JZBConstants;


public class L {
	public static void d(String tag, String msg) {
		if(JZBConstants.DEBUG_MODE){
			Log.d(tag, msg);
		}
	}
	public static void i(String tag, String msg) {
		if(JZBConstants.DEBUG_MODE){			
			Log.i(tag, msg);
		}
	}
	public static void e(String tag, String msg) {
		if(JZBConstants.DEBUG_MODE){			
			Log.e(tag, msg);
		}
	}
	public static void w(String tag, String msg) {
		if(JZBConstants.DEBUG_MODE){			
			Log.w(tag, msg);
		}
	}
	public static void v(String tag, String msg) {
		if(JZBConstants.DEBUG_MODE){			
			Log.v(tag, msg);
		}
	}

	public static void m(String msg){
		md(msg);
	}
	public static void md(String msg){
		if(JZBConstants.DEBUG_MODE){
			Log.d("xmg", msg);
		}
	}
	public static void mi(String msg){
		if(JZBConstants.DEBUG_MODE){
			Log.i("xmg", msg);
		}
	}
	public static void me(String msg){
		if(JZBConstants.DEBUG_MODE){
			Log.e("xmg", msg);
		}
	}
}
