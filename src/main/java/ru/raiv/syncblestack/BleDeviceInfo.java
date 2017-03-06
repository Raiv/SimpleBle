package ru.raiv.syncblestack;

import java.io.Serializable;

/**
 * Created by Raiv on 10.01.2017.
 */

public class BleDeviceInfo implements Serializable {

    public BleDeviceInfo(String name,String address){
        this.name=name;
        this.address=address;
    }

    private String name;
    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
