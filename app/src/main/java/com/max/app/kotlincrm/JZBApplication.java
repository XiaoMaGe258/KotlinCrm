package com.max.app.kotlincrm;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;

import com.max.app.kotlincrm.ui.LoginActivity;

import java.util.ArrayList;
import java.util.List;

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
        //初始化小米push推送服务
//        if(shouldInit()) {
//            MiPushClient.registerPush(this, JZBConstants.MI_PUSH_APPID_ONLINE,
//                    JZBConstants.MI_PUSH_APPKEY_ONLINE);
//        }
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
