package ru.raiv.syncblestack;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Raiv on 10.01.2017.
 */

public class BleDeviceInfo implements Parcelable {

    public BleDeviceInfo(String name,String address){
        this.name=name;
        this.address=address;
    }

    private String name;
    private String address;

    protected BleDeviceInfo(Parcel in) {
        name = in.readString();
        address = in.readString();
    }

    public static final Creator<BleDeviceInfo> CREATOR = new Creator<BleDeviceInfo>() {
        @Override
        public BleDeviceInfo createFromParcel(Parcel in) {
            return new BleDeviceInfo(in);
        }

        @Override
        public BleDeviceInfo[] newArray(int size) {
            return new BleDeviceInfo[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(name);
        dest.writeString(address);
    }
}
