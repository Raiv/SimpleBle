package ru.raiv.syncblestack.tasks;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.UUID;

/**
 * Created by Raiv on 07.01.2017.
 */

public class BleOperation {
    private UUID service;
    private UUID characteristic;

    private byte[] value;
    private BleOperationType opType;
    private boolean succeed = false;

    public BleOperation(@NonNull UUID service,@NonNull UUID characteristic, @Nullable byte[] value,@NonNull BleOperationType opType){
        init(service,characteristic,value,opType);
    }
    public BleOperation(@NonNull UUID service,@NonNull UUID characteristic, @NonNull BleOperationType opType){
        init(service,characteristic,null,opType);
    }
    public BleOperation(@NonNull String service,@NonNull String characteristic, @Nullable byte[] value,@NonNull BleOperationType opType){
        init(UUID.fromString(service),UUID.fromString(characteristic),value,opType);
    }
    public BleOperation(@NonNull String service,@NonNull String characteristic, @NonNull BleOperationType opType){
        init(UUID.fromString(service),UUID.fromString(characteristic),null,opType);
    }

    private void init(@NonNull UUID service, @NonNull UUID characteristic, @Nullable byte[] value,@NonNull BleOperationType opType){
        this.service=service;
        this.characteristic=characteristic;
        this.value=value;
        this.opType=opType;
    }

    public UUID getCharacteristic() {
        return characteristic;
    }

    public void setCharacteristic(UUID characteristic) {
        this.characteristic = characteristic;
    }
    public void setCharacteristic(String characteristic) {
        this.characteristic = UUID.fromString(characteristic);
    }
    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }

    public BleOperationType getOpType() {
        return opType;
    }

    public void setOpType(BleOperationType opType) {
        this.opType = opType;
    }

    public UUID getService() {
        return service;
    }

    public void setService(UUID service) {
        this.service = service;
    }

    public boolean isSucceed() {
        return succeed;
    }

    public void setSucceed(boolean succeed) {
        this.succeed = succeed;
    }
}
