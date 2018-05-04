package com.max.app.kotlincrm.api;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.BitmapCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.DBCookieStore;
import com.lzy.okgo.cookie.store.SPCookieStore;
import com.lzy.okgo.model.Response;
import com.max.app.kotlincrm.ui.LoginActivity;
import com.max.app.kotlincrm.utils.L;
import com.max.app.kotlincrm.utils.MyToast;
import com.max.app.kotlincrm.utils.NetConnectUtil;
import com.max.app.kotlincrm.utils.Sp;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.OkHttpClient;

public class JZBController {


    private static Context mContext;
    private static JZBController mJZBController;

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

    //登录
    public void postLogin (String username, String password, StringCallback responseHandler){
        String url = JZBConstants.API_IP + "/user/oauth";
        HashMap<String, Object> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        postOkHttp(url, params, responseHandler);
    }

    //企业查重(销售申请签约客户)
    public void getEnterpriseDupcheck(String customerName, StringCallback responseHandler){
        String url=JZBConstants.API_IP+"/customers/dupchecking?customer_name="+customerName;
        getOkHttp(url,responseHandler);
    }

    //企业信息
    public void getEnterpriseInfo(String customerId, StringCallback responseHandler){
        String url=JZBConstants.API_IP+"/customers/"+customerId;
        getOkHttp(url,responseHandler);
    }

    //捡回企业
    public void postPickupEnterprise(String customerId, StringCallback responseHandler){
        HashMap<String, Object> params = new HashMap<>();
        params.put("customer_id", customerId);
        String url=JZBConstants.API_IP+"/customers/pickup";
        postOkHttp(url, params, responseHandler);
    }

    //个人客户池列表
    public void getPrivateLibraryList(String level, String order, String keyword,
                                      int startIndex, int pageSize, StringCallback responseHandler){
        String url=JZBConstants.API_IP+"/customers/self" +"?start_index="+startIndex+"&page_size="+pageSize;
        if(!TextUtils.isEmpty(level)){
            url = url + "&level=" + level;
        }
        if(!TextUtils.isEmpty(order)){
            url = url + "&order=" + order;
        }
        if(!TextUtils.isEmpty(keyword)){
            url = url + "&key_word=" + keyword;
        }
        getOkHttp(url,responseHandler);
    }

    //个人下级客户池列表
    public void getJuniorLibraryList(String level, String order, String bdId, String keyword,
                                      int startIndex, int pageSize, StringCallback responseHandler){
        String url=JZBConstants.API_IP+"/customers/self/subofficer" +"?start_index="+startIndex+"&page_size="+pageSize;
        if(!TextUtils.isEmpty(level)){
            url = url + "&level=" + level;
        }
        if(!TextUtils.isEmpty(order)){
            url = url + "&order=" + order;
        }
        if(!TextUtils.isEmpty(bdId)){
            url = url + "&staff_id=" + bdId;
        }
        if(!TextUtils.isEmpty(keyword)){
            url = url + "&key_word=" + keyword;
        }
        getOkHttp(url,responseHandler);
    }

    //跟进记录
    public void getFollowLogs(String customerId, int startIndex, int pageSize, StringCallback responseHandler){
        String url=JZBConstants.API_IP+"/followuplogs?start_index=" + startIndex
                + "&page_size=" + pageSize + "&customer_id=" + customerId;
        getOkHttp(url,responseHandler);
    }

    //获取联系人列表
    public void getContactList(String customerId, StringCallback responseHandler){
        String url = JZBConstants.API_IP + "/contacts?customer_id=" + customerId;
        getOkHttp(url, responseHandler);
    }

    //获取企业信息
    public void getEnterpriseModelInfo(String customerId, StringCallback responseHandler){
        String url = JZBConstants.API_IP + "/customers/" + customerId;
        getOkHttp(url, responseHandler);
    }

    //离访
    public void postLeaveFollowup(String followupId, StringCallback responseHandler){
        HashMap<String, Object> params = new HashMap<>();
        params.put("followup_id", followupId);
        String url=JZBConstants.API_IP + "/api/app/customer/followup/finish";
        postOkHttp(url, params, responseHandler);
    }

    //跟进对象
    public void getFollowObject(String customerId, StringCallback responseHandler){
        String url=JZBConstants.API_IP + "/contacts?customer_id=" + customerId;
        getOkHttp(url,responseHandler);
    }

    //跟进内容标签
    public void getFollowTags(StringCallback responseHandler){
        String url=JZBConstants.API_IP + "//api/app/customer/followup/contentlabel";
        getOkHttp(url,responseHandler);
    }


//-------------------------------------------------------------------------------------

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
                                    activity.startActivity(new Intent(activity, LoginActivity.class));
                                    Sp.delete_SP(mContext, JZBConstants.SP_USERINFO);
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
                                    activity.startActivity(new Intent(activity, LoginActivity.class));
                                    Sp.delete_SP(mContext, JZBConstants.SP_USERINFO);
//                                    JZBApplication.recycleAllActivity();
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
                                    activity.startActivity(new Intent(activity, LoginActivity.class));
                                    Sp.delete_SP(mContext, JZBConstants.SP_USERINFO);
//                                    JZBApplication.recycleAllActivity();
                                }
                            });
                        }
                    }
                });
    }

    //下载图片用
    public void getOkHttpBitmap(String url, BitmapCallback callback) {
        L.d("xmg", "0HttpGetRequest  url=" + url);
        OkGo.<Bitmap>get(url).execute(callback);
    }


}
