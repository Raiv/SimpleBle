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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BleDeviceInfo)) return false;

        BleDeviceInfo that = (BleDeviceInfo) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return address != null ? address.equals(that.address) : that.address == null;

    }

    @Override
    public int hashCode() {
        return address != null ? address.hashCode() : 0;
    }

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
