package com.max.app.kotlincrm.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

public class NetConnectUtil {

	/**
	 * 检查是否有可用网络
	 * 
	 * @param context
	 * @return 有(true) / 没有(false)
	 */
	public static boolean hasNetConnect(Context context) {
		return wifi(context) && net(context);
	}

	// 检查网络(wifi,2Gnet...)是否打开
	private static boolean wifi(Context context) {
		ConnectivityManager mgrConn = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		TelephonyManager mgrTel = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return ((mgrConn.getActiveNetworkInfo() != null && mgrConn
				.getActiveNetworkInfo().getState() == NetworkInfo.State.CONNECTED) || mgrTel
				.getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS);
	}

	// 网络连接是否正常
	private static boolean net(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo network = cm.getActiveNetworkInfo();
		if (network != null) {
			return network.isAvailable();
		}
		return false;
	}

	// 提示联网对话框
	public void showNetDialog(final Context context) {
		new AlertDialog.Builder(context)
				.setTitle("提示")
				.setMessage("网络连接不可用，或没有打开连接。请检查后重试。")
				.setPositiveButton("设置", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent("android.settings.SETTINGS");
						context.startActivity(intent);
					}
				})
				.setNeutralButton("返回", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				}).create().show();
	}
	
	// 提示联网Toast
	public static void showNetToast(final Context context) {
		MyToast.makeText(context, "网络不可用，请检查后重试");
	}

}
