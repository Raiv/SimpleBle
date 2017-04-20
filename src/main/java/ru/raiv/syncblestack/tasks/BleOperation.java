package ru.raiv.syncblestack.tasks;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Arrays;
import java.util.UUID;

/**
 * Created by Raiv on 07.01.2017.
 */

public class BleOperation implements Parcelable{


    private byte[] value;

    private boolean succeed = false;
    private UUID service;
    private UUID characteristic;
    private BleOperationType opType;

    BleOperation(@NonNull UUID service, @NonNull UUID characteristic, @Nullable byte[] value, @NonNull BleOperationType opType){
        init(service,characteristic,value,opType);
    }

    BleOperation(@NonNull String service, @NonNull String characteristic, @Nullable byte[] value, @NonNull BleOperationType opType){
        init(UUID.fromString(service),UUID.fromString(characteristic),value,opType);
    }


    protected BleOperation(Parcel in) {
        value = in.createByteArray();
        succeed = in.readByte() != 0;
        service = UUID.fromString(in.readString());
        characteristic=UUID.fromString(in.readString());
        int ord = in.readInt();
        opType=BleOperationType.values()[ord];
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByteArray(value);
        dest.writeByte((byte) (succeed ? 1 : 0));
        dest.writeString(service.toString());
        dest.writeString(characteristic.toString());
        dest.writeInt(opType!=null?opType.ordinal():-1);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BleOperation> CREATOR = new Creator<BleOperation>() {
        @Override
        public BleOperation createFromParcel(Parcel in) {
            return new BleOperation(in);
        }

        @Override
        public BleOperation[] newArray(int size) {
            return new BleOperation[size];
        }
    };

    private void init(@NonNull UUID service, @NonNull UUID characteristic, @Nullable byte[] value, @NonNull BleOperationType opType){
        this.service=service;
        this.characteristic=characteristic;
        this.value=value;
        this.opType=opType;
    }




  /*  private static volatile transient UUID defaultService = null;
    public static void setDefaultService(@NonNull UUID service){
        defaultService=service;
    }
    public static void setDefaultService(@NonNull String service){
        defaultService=UUID.fromString(service);
    }
*/
    public UUID getCharacteristic() {
        return characteristic;
    }
/*
    public void setCharacteristic(UUID characteristic) {
        this.characteristic = characteristic;
    }
    public void setCharacteristic(String characteristic) {
        this.characteristic = UUID.fromString(characteristic);
    }
*/
    public UUID getService() {
        return service;
    }
/*
    public void setService(UUID service) {
        this.service = service;
    }
    public void setService(String service) {
        this.service =UUID.fromString(service);
    }
*/


    public BleOperationType getOpType() {
        return opType;
    }
/*
    public void setOpType(BleOperationType opType) {
        this.opType = opType;
    }
*/
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BleOperation)) return false;

        BleOperation that = (BleOperation) o;

        if (succeed != that.succeed) return false;
        if (!Arrays.equals(value, that.value)) return false;
        if (!service.equals(that.service)) return false;
        if (!characteristic.equals(that.characteristic)) return false;
        return opType == that.opType;

    }

    @Override
    public int hashCode() {

        int result = Arrays.hashCode(value);
        result = 31 * result + service.hashCode();
        result = 31 * result + characteristic.hashCode();
        result = 31 * result + opType.hashCode();
        return result;
    }

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }

    public boolean isSucceed() {
        return succeed;
    }

    public void setSucceed(boolean succeed) {
        this.succeed = succeed;
    }


}
