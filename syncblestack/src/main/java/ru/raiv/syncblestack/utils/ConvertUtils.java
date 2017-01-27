package ru.raiv.syncblestack.utils;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * Created by Raiv on 07.01.2017.
 */

public class ConvertUtils {
    private static final String TAG = ConvertUtils.class.getSimpleName();
    final private static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public  static String bytesToHex(byte[] bytes) {
        if (bytes==null)
            return "null";
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
    public static byte[] numberToCharsWithTerminator(String number){
        if(number==null){
            return null;
        }
        try {
            byte[] data=number.getBytes("UTF-8");
            if(data[data.length-1]!=0){
                data=new byte[data.length+1];
                System.arraycopy(number.getBytes("UTF-8"),0,data,0,data.length-1);
                data[data.length-1]=0;
            }
            return data;

        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }
    public static byte[] stringToBytes(String number){
        if(number==null){
            return null;
        }
        try {
            return number.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }
    public static String bytesToString(byte[] data){
        if(data==null||data.length==0){
            return "";
        }
        try {
            return new String(data,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    // big endian
    public static byte[] fromShort(int data){
        byte[] result = new byte[2];
        result[0]=(byte)(data&0xff);
        result[1]=(byte) ((data >> 8) & 0xff);
        return result;
    }

    public static int toShort(byte[] data){
        Log.d(TAG,"toShort: "+bytesToHex(data));
        if(data==null ||data.length<2){
            return -1;
        }
        int result = (data[0]&0xff);
        result+=((data[1]<<8)&0xff00);
        return result;
    }
    // big endian
    public static byte[] fromFloat(float data){
        int bits = Float.floatToIntBits(data);
        byte[] bytes = new byte[4];
        bytes[3] = (byte)(bits & 0xff);
        bytes[2] = (byte)((bits >> 8) & 0xff);
        bytes[1] = (byte)((bits >> 16) & 0xff);
        bytes[0] = (byte)((bits >> 24) & 0xff);
        return bytes;
    }

    public static float toFloat(byte[] data){
        Log.d(TAG,"toFloat: "+bytesToHex(data));
        if(data==null ||data.length<4){
            return -1;
        }
        byte[] reverted = new byte[data.length];
        for(int i=0;i<data.length;i++){
            reverted[i]=data[data.length-i-1];
        }
        return ByteBuffer.wrap(reverted).getFloat();

    }

    public static byte[] fromByte(int data)
    {
        byte [] bData = new byte[1];
        bData[0]= (byte)(data&0xff);
        return bData;
    }

    public static int toByte(byte[] data){

        Log.d(TAG,"toByte: "+bytesToHex(data));
        if(data==null ||data.length<1){
            return -1;
        }
        return (data[0]&0xff);
    }
}
