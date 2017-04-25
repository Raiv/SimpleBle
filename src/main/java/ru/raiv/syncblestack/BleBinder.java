package ru.raiv.syncblestack;

import android.os.Binder;

import ru.raiv.syncblestack.tasks.BleTask;

/**
 * Created by Raiv on 03.03.2017.
 */

public final class BleBinder extends Binder {
    private BluetoothLeServiceSync owner;
    public BleBinder( BluetoothLeServiceSync owner){
        this.owner = owner;
    }

    public void addTask(BleTask task){
        owner.addTask(task);
    }

    public void connectDevice(BleDeviceInfo device,boolean autoReconnect){
        if(device!=null) {
            owner.setScanning(false, false);
            owner.connect(device.getAddress(),autoReconnect);
        }
    }
    public void disconnectDevice(BleDeviceInfo device){
        if(device!=null) {
            owner.disconnectDevice(device.getAddress());
        }

    }
    public void scanForDeviceOnce(){
        owner.setScanning(true,false);
    }
    public void scanForDevices(){
        owner.setScanning(true,true);
    }
    public void stopScanForDevice(){
        owner.setScanning(false,false);
    }

}