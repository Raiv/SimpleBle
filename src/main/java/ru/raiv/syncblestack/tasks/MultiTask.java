package ru.raiv.syncblestack.tasks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

/**
 * Created by Raiv on 07.01.2017.
 */

class MultiTask implements BleTask{

    ArrayList<BleOperation> operations =new ArrayList<>();
    int innerIndex = -1;

    MultiTask(Collection<BleOperation> tasks){
        operations.addAll(tasks);
    }


    public synchronized boolean hasNext(){

            return innerIndex< operations.size()-1;

    };

    public synchronized BleOperation next(){


                if(innerIndex< operations.size()-1) {
                    innerIndex++;
                    return operations.get(innerIndex);
                }



        return null;
    }

    public synchronized void reset(){

            innerIndex=0;

    }

    @Override
    public boolean isSync() {
        return false;
    }

    @Override
    public synchronized BleOperation current() {

            if(innerIndex>=0){
                return operations.get(innerIndex);
            }

        return null;
    }

    @Override
    public BleOperation getByName(UUID name) {
       for(BleOperation current: operations){
           if(current.getCharacteristic().equals(name)){
               return current;
           }
       }
       return null;
    }

    @Override
    public BleOperation getByName(String name) {
        return getByName(UUID.fromString(name));
    }

    @Override
    public synchronized boolean hasCurrent() {
        return innerIndex>=0;
    }

    @Override
    public boolean allSucceed() {
        for(BleOperation op:operations){
            if(!op.isSucceed()){
                return false;
            }
        }
        return true;
    }
}
