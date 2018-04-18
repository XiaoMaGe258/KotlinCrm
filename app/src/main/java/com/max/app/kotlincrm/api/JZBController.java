package com.max.app.kotlincrm.api;

//import android.text.TextUtils;
//
//import com.jianzhibao.crm.mobile.application.JZBApplication;
//import com.jianzhibao.crm.mobile.utils.L;
//import com.jianzhibao.crm.mobile.utils.Utils;
//import com.loopj.android.http.AsyncHttpClient;
//import com.loopj.android.http.AsyncHttpResponseHandler;
//import com.loopj.android.http.PersistentCookieStore;
//import com.loopj.android.http.RequestParams;
//
//import java.io.File;
//
//import cz.msebera.android.httpclient.Header;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.BitmapCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.max.app.kotlincrm.LoginActivity;
import com.max.app.kotlincrm.utils.L;
import com.max.app.kotlincrm.utils.MyToast;
import com.max.app.kotlincrm.utils.NetConnectUtil;
import com.max.app.kotlincrm.utils.Sp;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class JZBController {


    private static Context mContext;
    private static JZBController mJZBController;
    private static boolean mIs401 = false;

    private JZBController() {
    }

    public synchronized static JZBController getInstance(final Context context) {
        if (mJZBController == null) {
            mJZBController = new JZBController();
        }
        mContext = context;
        boolean hasNet = NetConnectUtil.hasNetConnect(context);
        if (!hasNet) {
            if (context instanceof Activity) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        NetConnectUtil.showNetToast(context);
                    }
                });
            }
        }
        return mJZBController;
    }

    public void postLogin (String username, String password, StringCallback responseHandler){
        String url = JZBConstants.API_IP + "/user/oauth";
        HashMap<String, Object> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        postOkHttp(url, params, responseHandler);
    }



    public void getOkHttp(String url, final StringCallback responseHandler) {
        L.d("xmg", "HttpGetRequest  url=" + url);
        try {
            ((JzbResponseHandler) responseHandler).showProgressDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }
        OkGo.<String>get(url)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        mIs401 = false;
                        responseHandler.onSuccess(response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        responseHandler.onError(response);
                        if (response.code() != 401 || mIs401) {
                            return;
                        }
                        mIs401 = true;
                        if (mContext instanceof Activity) {
                            final Activity activity = (Activity) mContext;
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    MyToast.makeText(mContext, "未登录或者登录已经过期,请重新登录");
//                                    LoginActivity.actionLoginAcivity(activity, false);
//                                    Sp.delete_SP(mContext, JZBConstants.USERCONFIG);
                                }
                            });
                        }
                    }
                });
    }

    public void postOkHttp(String url, HashMap<String, Object> params,
                           final StringCallback responseHandler) {
        L.d("xmg", "HttpPostRequest  url=" + url);
        try {
            ((JzbResponseHandler) responseHandler).showProgressDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (params != null) {
            L.d("xmg", "HttpPostRequest  params=" + params.toString());
        }
        com.lzy.okgo.model.HttpParams httpParams = new com.lzy.okgo.model.HttpParams();
        if (params != null && params.size() > 0) {
            Iterator iterator = params.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                String key = (String) entry.getKey();
                Object value = entry.getValue();
                if (value instanceof Integer) {
                    httpParams.put(key, (Integer) value);
                } else {
                    httpParams.put(key, String.valueOf(value));
                }
            }
        }
        OkGo.<String>post(url).params(httpParams)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        L.d("xmg", "callback  body=" + response.body());
                        responseHandler.onSuccess(response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        if (response.code() != 401) {
                            responseHandler.onError(response);
                            return;
                        }
                        if (mContext instanceof Activity) {
                            final Activity activity = (Activity) mContext;
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    MyToast.makeText(mContext, "未登录或者登录已经过期,请重新登录");
//                                    LoginActivity.actionLoginAcivity(activity, false);
//                                    MySharedPreferences.delete_SP(mContext, JZBConstants.USERCONFIG);
                                }
                            });
                        }
                    }
                });
    }

    public void postOkHttpWithFile(String url, HashMap<String, Object> params, HashMap<String,
            File> files, final StringCallback responseHandler) {
        L.d("xmg", "HttpPostFileRequest  url=" + url);
        try {
            ((JzbResponseHandler) responseHandler).showProgressDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }
        com.lzy.okgo.model.HttpParams httpParams = new com.lzy.okgo.model.HttpParams();

        if (params != null && params.size() > 0) {
            Iterator iterator = params.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                String key = (String) entry.getKey();
                Object value = entry.getValue();
                if (value instanceof Integer) {
                    httpParams.put(key, (Integer) value);
                } else {
                    httpParams.put(key, String.valueOf(value));
                }
            }
        }
        if (files != null && files.size() > 0) {
            Iterator<String> fileIterator = files.keySet().iterator();
            while (fileIterator.hasNext()) {
                String key = (String) fileIterator.next();
                httpParams.put(key, files.get(key));
            }
        }
        OkGo.<String>post(url).params(httpParams).isMultipart(true)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        L.d("xmg", "callback  body=" + response.body());
                        responseHandler.onSuccess(response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        responseHandler.onError(response);
                    }
                });
    }

    //下载图片用
    public void getOkHttpBitmap(String url, BitmapCallback callback) {
        L.d("xmg", "0HttpGetRequest  url=" + url);
        OkGo.<Bitmap>get(url).execute(callback);
    }


}
