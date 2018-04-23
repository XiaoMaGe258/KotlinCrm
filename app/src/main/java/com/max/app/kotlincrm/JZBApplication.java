package com.max.app.kotlincrm;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;

import com.baidu.mapapi.SDKInitializer;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.SPCookieStore;
import com.max.app.kotlincrm.ui.LoginActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by Xmg on 2018-4-19.
 */

public class JZBApplication extends Application {

    public static Application application;
    /**保存当前所有的activity**/
    public static List<Activity> AllActivity = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        //初始化okHttp-go框架
        initOkHttp();
        //百度地图
        SDKInitializer.initialize(getApplicationContext());
        //初始化小米push推送服务
//        if(shouldInit()) {
//            MiPushClient.registerPush(this, JZBConstants.MI_PUSH_APPID_ONLINE,
//                    JZBConstants.MI_PUSH_APPKEY_ONLINE);
//        }
    }

    public void initOkHttp(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //全局的读取超时时间
        builder.readTimeout(30000L, TimeUnit.MILLISECONDS);
        //全局的写入超时时间
        builder.writeTimeout(30000L, TimeUnit.MILLISECONDS);
        //全局的连接超时时间
        builder.connectTimeout(30000L, TimeUnit.MILLISECONDS);
        /*
        使用SP保持cookie：SPCookieStore(context)；如果cookie不过期，则一直有效
        使用数据库保持cookie：DBCookieStore(context)；如果cookie不过期，则一直有效
        使用内存保持cookie：MemoryCookieStore()；app退出后，cookie消失
         */
        builder.cookieJar(new CookieJarImpl(new SPCookieStore(this)));

        //信任所有证书,不安全可能有风险
//        HttpsUtils.SSLParams sslParams1 = HttpsUtils.getSslSocketFactory();
//        builder.sslSocketFactory(sslParams1.sSLSocketFactory, sslParams1.trustManager);

//		HttpHeaders headers = new HttpHeaders();
//		headers.put("Accept", "application/json");
//		HttpParams params = new HttpParams();
        OkGo.getInstance().init(this)                       	//必须调用初始化
                .setOkHttpClient(builder.build());              //建议设置OkHttpClient，不设置将使用默认的
//                .setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
//                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
//				.setRetryCount(3)                               //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
//				.addCommonHeaders(headers)                      //全局公共头
//				.addCommonParams(params);                       //全局公共参数
    }

    /**
     * 当session过期是关闭所有activity
     */
    public static void recycleAllActivity(){
        for (Activity activity : AllActivity){
            activity.finish();
        }
//        MySharedPreferences.init_SP_Instance(application, JZBConstants.USERINFO);
//        MySharedPreferences.put_Boolean(JZBConstants.ISLOGIN, false);
        Intent intent = new Intent(application, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        application.startActivity(intent);
    }

//    private boolean shouldInit() {
//        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
//        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
//        String mainProcessName = getPackageName();
//        int myPid = android.os.Process.myPid();
//        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
//            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
//                return true;
//            }
//        }
//        return false;
//    }

}
