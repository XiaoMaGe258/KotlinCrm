package com.max.app.kotlincrm.utils;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * 定位工具
 * Created by Max on 2017-10-13.
 *
 * 使用条件：
 * 1、先拷贝SDK中的jar包，so包。
 * 2、在Application中先初始化地图SDK：
 * SDKInitializer.initialize(getApplicationContext());
 *
 * 使用：
 * LocationUtil.getInstance(mContext).getLocation(new OnGetLocation());
 */

public class LocationUtil implements BDLocationListener {

    private static Context mContext;
    private static LocationUtil mLocationUtil;
    private LocationClient mLocClient;
    private OnGetLocation mOnGetLocation;

    public static LocationUtil getInstance(Context context){
        mContext = context;
        if(mLocationUtil == null){
            mLocationUtil = new LocationUtil();
        }
        return mLocationUtil;
    }

    private void initBaiDuLocation(){

        mLocClient = new LocationClient(mContext);
        mLocClient.registerLocationListener(this);
        // 设置定位参数
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(0);
        option.setIsNeedAddress(true);
//        // 需要地址信息，设置为其他任何值（string类型，且不能为null）时，都表示无地址信息。
//        option.setAddrType("all");
//        // 设置产品线名称。强烈建议您使用自定义的产品线名称，方便我们以后为您提供更高效准确的定位服务。
//        option.setProdName("首页定位");
        // 设置定位方式的优先级。
        // 当gps可用，而且获取了定位结果时，不再发起网络请求，直接返回给用户坐标。这个选项适合希望得到准确坐标位置的用户。如果gps不可用，再发起网络请求，进行定位。
        option.setPriority(LocationClientOption.NetWorkFirst);
        mLocClient.setLocOption(option);
    }

    public void getLocation(OnGetLocation mOnGetLocation){
        initBaiDuLocation();
        this.mOnGetLocation = mOnGetLocation;
        L.mi("开始定位");
        mLocClient.start();
    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        L.mi("定位结束");
        if(mOnGetLocation != null){
            mOnGetLocation.onReceive(bdLocation);
        }
        mLocClient.stop();
    }

    public interface OnGetLocation{
        void onReceive(BDLocation bdLocation);
    }

}
