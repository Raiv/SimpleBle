package ru.raiv.syncblestack;

import android.content.IntentFilter;

/**
 * Created by Raiv on 03.03.2017.
 */

public class BleConst {
    public final static String ACTION_DEVICE_CONNECTED = "ACTION_DEVICE_CONNECTED";
    public static final String PARAM_DEVICE_NAME=ACTION_DEVICE_CONNECTED.concat("PARAM_DEVICE_NAME");
    public final static String ACTION_DEVICE_DISCONNECTED = "ACTION_DEVICE_DISCONNECTED";
    public final static String ACTION_DEVICE_ERROR = "ACTION_DEVICE_ERROR";
    public final static String PARAM_DEVICE_ERROR =ACTION_DEVICE_ERROR.concat(".ERROR");
    public final static String ACTION_DEVICES_FOUND = "ACTION_DEVICES_FOUND";
    public final static String PARAM_DEVICES_FOUND_LIST =ACTION_DEVICES_FOUND.concat(".LIST");
    public final static String ACTION_SEARCH_FINISHED = "ACTION_SEARCH_FINISHED";
    public final static String ACTION_CHARACTERISTIC_NOTIFICATION = "ACTION_CHARACTERISTIC_NOTIFICATION";
    public final static String PARAM_CHARACTERISTIC_NOTIFICATION =ACTION_CHARACTERISTIC_NOTIFICATION.concat(".CHARACTERISTIC");

    public static final IntentFilter bleServiceFilter = makeBleServiceFilter();

    private static IntentFilter makeBleServiceFilter(){
        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(ACTION_DEVICE_CONNECTED);
        iFilter.addAction(ACTION_DEVICE_DISCONNECTED);
        iFilter.addAction(ACTION_DEVICES_FOUND);
        iFilter.addAction(ACTION_DEVICE_ERROR);
        iFilter.addAction(ACTION_SEARCH_FINISHED);
        iFilter.addAction(ACTION_CHARACTERISTIC_NOTIFICATION);
        return iFilter;
    }
}
