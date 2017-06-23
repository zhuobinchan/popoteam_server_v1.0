package com.geetion.puputuan.common.utils;

import java.util.Arrays;

/**
 * Created by yoan on 16/1/27.
 */
public class HexUtils {

    /**
     * 单个字节到十六进制字符串
     *
     * @param src
     * @return
     */
    public static String byteToHexString(byte src){
        return bytesToHexString(new byte[]{src});
    }

    /**
     * Convert byte[] to hex string.这里我们可以将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。
     * @param src byte[] data
     * @return hex string
     */
    public static String bytesToHexString(byte[] src){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /***
     * 十六进制到单个字节
     *
     * @param hextString
     * @return
     */
    public static byte hexStringToByte(String hextString){
        return hexStringToBytes(hextString)[0];
    }

    /**
     * convert Hex String to byte array
     *
     * @param hexString
     * @return
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    /**
     * Convert char to byte
     * @param c char
     * @return byte
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /**
     * 定位一个字节在字节数组中的位置
     *
     * @param byteArray 目标数组
     * @param target 目标字节
     * @param last 是否最后一个
     * @return
     */
    public static int locateByte(byte[] byteArray, byte target, boolean last){
        if(last){
            //逆向查找使用逆向循环遍历
            for(int i=byteArray.length-1;i>=0;i--){
                if(byteArray[i] == target){
                    return i;
                }
            }
        } else{
            //正向查找时使用二分搜索
            Arrays.binarySearch(byteArray, target);
        }
        return -1;//找不到
    }

    /**
     * 翻转字节数组
     *
     * @param targetArray
     * @return
     */
    public static byte[] reverseByteArray(byte[] targetArray){
        byte[] resultArray = new byte[targetArray.length];
        for (int i = 0, j = targetArray.length - 1; i < targetArray.length; i++, j--) {
            resultArray[i] = targetArray[j];
        }
        return resultArray;
    }

    /**
     * 带补位的整型转十六进制字符串
     *
     * @param data
     * @return
     */
    public static String integerToHexString(int data){
        String hexString = Integer.toHexString(data);
        return hexString.length() == 2 ? hexString : "0" + hexString;
    }

    /**
     * 计算校验码CS
     *
     * @param data
     * @return
     */
    public static String createVerifyCode(String data){
        byte[] bytes = HexUtils.hexStringToBytes(data);
        short sum = 0;
        for(short i : bytes){
            sum += i;
        }
        sum %= 256;
        return integerToHexString(sum);
    }

    /**
     * 字节数组加减字节
     *
     * @param array
     * @param key
     * @param plus
     */
    public static void computeArray(byte[] array, byte key, boolean plus){
        for(int i=0;i<array.length;i++){
            array[i] += plus ? key : -key;
        }
    }
}
