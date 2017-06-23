package com.geetion.puputuan.common.utils;

/**
 * Created by jian on 2016/5/5.
 * 计算两个经纬度之间的距离
 */
public class LongitudeAndLatitudeUtils {

    /**
     * 地球半径
     */
    private static final double EARTH_RADIUS = 6378137;

    /**
     * 将角度转换为弧度
     *
     * @param d 角度
     * @return 弧度
     */
    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 根据两点间经纬度坐标（double值），计算两点间距离，单位为米
     *
     * @param longitude1 经度1
     * @param latitude1  纬度1
     * @param longitude2 经度2
     * @param latitude2  纬度2
     * @return
     */
    public static double getDistance(double longitude1, double latitude1, double longitude2, double latitude2) {
        double radLat1 = rad(latitude1);
        double radLat2 = rad(latitude2);
        //两点纬度之差
        double latDiff = radLat1 - radLat2;
        //两点经度之差
        double lngDiff = rad(longitude1) - rad(longitude2);
        double distance = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(latDiff / 2), 2) +
                Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(lngDiff / 2), 2)));
        distance = distance * EARTH_RADIUS;
        distance = Math.round(distance * 10000) / 10000;
        return distance;
    }


    /**
     * main方法测试
     *
     * @param args
     */
    public static void main(String[] args) {
        double distance = getDistance(121.491909, 31.233234, 121.411994, 31.206134);
        System.out.println("Distance is:" + distance);
    }
}
