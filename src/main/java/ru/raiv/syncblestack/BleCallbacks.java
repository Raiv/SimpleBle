package ru.raiv.syncblestack;

import ru.raiv.syncblestack.tasks.BleOperation;

/**
 * Created by Raiv on 03.03.2017.
 */

public interface BleCallbacks {

    void onDeviceError(BleDeviceInfo device, int errorCode);
    void onDevicesFound(BleDeviceInfo[] devices);
    void onDeviceConnected(BleDeviceInfo device);
    void onDeviceDisconnected(BleDeviceInfo device);
    void onNoDevicesFound();
    void onCharacteristicNotification(BleDeviceInfo device, BleOperation characteristic);

}
