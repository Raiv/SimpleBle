package ru.raiv.syncblestack;

import ru.raiv.syncblestack.tasks.BleOperation;

/**
 * Created by Raiv on 17.04.2017.
 */

public class BleDefaultCallbacks implements BleCallbacks {
    @Override
    public void onDeviceError(BleDeviceInfo device, int errorCode) {

    }

    @Override
    public void onDevicesFound(BleDeviceInfo[] devices) {

    }

    @Override
    public void onDeviceConnected(BleDeviceInfo device) {

    }

    @Override
    public void onDeviceDisconnected(BleDeviceInfo device) {

    }

    @Override
    public void onNoDevicesFound() {

    }

    @Override
    public void onCharacteristicNotification(BleDeviceInfo device, BleOperation characteristic) {

    }

    @Override
    public void onServiceBind() {

    }
}
