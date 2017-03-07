package ru.raiv.syncblestack.tasks;

import android.support.annotation.NonNull;

import java.util.UUID;

/**
 * Created by Raiv on 07.01.2017.
 */

class SingleTask implements BleTask{


    final BleOperation operation;
    boolean innerHasNext = true;


    SingleTask(@NonNull BleOperation operation){
        this.operation=operation;
    }


    public synchronized boolean hasNext(){
            return innerHasNext;

    };


    public synchronized BleOperation next(){

            synchronized (this) {
                if(innerHasNext) {
                    innerHasNext = false;
                    return operation;
                }
            }


        return null;
    }

    public synchronized void reset(){

            innerHasNext=true;

    }

    @Override
    public boolean isSync() {
        return false;
    }

    @Override
    public synchronized BleOperation current() {
        if(!innerHasNext) {
            return operation;
        }return null;
    }

    @Override
    public BleOperation getByName(UUID name) {
        if(operation.getCharacteristic().equals(name)){
            return operation;
        }
        return null;
    }

    @Override
    public BleOperation getByName(String name) {
        return getByName(UUID.fromString(name));
    }

    @Override
    public synchronized boolean hasCurrent() {
        return !innerHasNext;
    }


}
