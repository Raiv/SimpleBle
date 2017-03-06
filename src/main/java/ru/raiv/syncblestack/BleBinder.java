package ru.raiv.syncblestack;

import android.os.Binder;

import ru.raiv.syncblestack.tasks.BleTask;

/**
 * Created by Raiv on 03.03.2017.
 */

public class BleBinder extends Binder {
    private BluetoothLeServiceSync owner;
    public BleBinder( BluetoothLeServiceSync owner){
        this.owner = owner;
    }

    public void addTask(BleTask task){
        owner.addTask(task);
    }

    public void connectDevice(String deviceAddress){

        owner.setScanning(false,false);
        owner.connect(deviceAddress);
    }
    public void disconnectDevice(String deviceAddress){
        owner.disconnectDevice(deviceAddress);

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