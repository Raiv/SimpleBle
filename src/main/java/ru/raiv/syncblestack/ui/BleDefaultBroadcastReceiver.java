package ru.raiv.syncblestack.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import ru.raiv.syncblestack.BleCallbacks;
import ru.raiv.syncblestack.BleConst;
import ru.raiv.syncblestack.BleDeviceInfo;
import ru.raiv.syncblestack.tasks.BleOperation;

/**
 * Created by Raiv on 03.03.2017.
 */

public class BleDefaultBroadcastReceiver extends BroadcastReceiver {



    BleDefaultBroadcastReceiver(@NonNull BleCallbacks callbacks){
        this.callbacks=callbacks;
    }


    private final BleCallbacks callbacks;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();


        if (BleConst.ACTION_DEVICE_CONNECTED.equals(action)) {
            BleDeviceInfo bdi = (BleDeviceInfo) intent.getSerializableExtra(BleConst.PARAM_DEVICE_NAME);
            callbacks.onDeviceConnected(bdi);
            return;
        }
        if (BleConst.ACTION_DEVICE_DISCONNECTED.equals(action)){
            BleDeviceInfo bdi = (BleDeviceInfo) intent.getSerializableExtra(BleConst.PARAM_DEVICE_NAME);
            callbacks.onDeviceDisconnected(bdi);
            return;
        }
        if (BleConst.ACTION_DEVICE_ERROR.equals(action)){
            BleDeviceInfo bdi = (BleDeviceInfo) intent.getSerializableExtra(BleConst.PARAM_DEVICE_NAME);
            int error = intent.getIntExtra(BleConst.PARAM_DEVICE_ERROR,0);
            callbacks.onDeviceError(bdi,error);
            return;
        }
        if (BleConst.ACTION_CHARACTERISTIC_NOTIFICATION.equals(action)){
            BleDeviceInfo bdi = (BleDeviceInfo) intent.getSerializableExtra(BleConst.PARAM_DEVICE_NAME);
            BleOperation operation =(BleOperation)intent.getParcelableExtra(BleConst.PARAM_CHARACTERISTIC_NOTIFICATION);
            callbacks.onCharacteristicNotification(bdi,operation);
            return;
        }
        if (BleConst.ACTION_DEVICES_FOUND.equals(action)){
            BleDeviceInfo[] bdi = (BleDeviceInfo[]) intent.getSerializableExtra(BleConst.PARAM_DEVICES_FOUND_LIST);
            callbacks.onDevicesFound(bdi);
            return;
        }
        if (BleConst.ACTION_SEARCH_FINISHED.equals(action)){
            callbacks.onNoDevicesFound();
            return;
        }
    }

    public BleCallbacks getCallbacks() {
        return callbacks;
    }

}
