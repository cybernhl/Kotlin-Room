package com.guadou.kt_demo.demo.demo16_record;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

import com.guadou.lib_baselib.utils.log.YYLogUtils;

/**
 * 获取定位服务
 */
public class LocationService extends Service {

    private LocationManager lm;
    private MyLocationListener listener;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate() {
        super.onCreate();

        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        listener = new MyLocationListener();

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setAltitudeRequired(false);//不要求海拔
        criteria.setBearingRequired(false);//不要求方位
        criteria.setCostAllowed(true);//允许有花费
        criteria.setPowerRequirement(Criteria.POWER_LOW);//低功耗

        String provider = lm.getBestProvider(criteria, true);

        YYLogUtils.w("定位的provider:" + provider);

        Location location = lm.getLastKnownLocation(provider);

        YYLogUtils.w("location-" + location);

        if (location != null) {
            //不为空,显示地理位置经纬度
            String longitude = "Longitude:" + location.getLongitude();
            String latitude = "Latitude:" + location.getLatitude();

            YYLogUtils.w("getLastKnownLocation:" + longitude + "-" + latitude);

            stopSelf();

        }

        lm.requestLocationUpdates(provider, 3000, 10, listener);
    }

    class MyLocationListener implements LocationListener {
        // 位置改变时获取经纬度
        @Override
        public void onLocationChanged(Location location) {

            String longitude = "Longitude:" + location.getLongitude();
            String latitude = "Latitude:" + location.getLatitude();

            YYLogUtils.w("onLocationChanged:" + longitude + "-" + latitude);


            stopSelf();  // 获取到经纬度以后，停止该service
        }

        // 状态改变时
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            YYLogUtils.w("onStatusChanged - provider:"+provider +" status:"+status);
        }

        // 提供者可以使用时
        @Override
        public void onProviderEnabled(String provider) {
            YYLogUtils.w("GPS开启了");
        }

        // 提供者不可以使用时
        @Override
        public void onProviderDisabled(String provider) {
            YYLogUtils.w("GPS关闭了");
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        lm.removeUpdates(listener); // 停止所有的定位服务
    }

}
