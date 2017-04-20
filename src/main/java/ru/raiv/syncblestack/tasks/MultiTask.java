package ru.raiv.syncblestack.tasks;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Raiv on 07.01.2017.
 */

class MultiTask implements BleTask{

    LinkedHashMap<UUID,BleOperation> operations =new LinkedHashMap<>();
    //int innerIndex = -1;
    Iterator<Map.Entry<UUID,BleOperation>> current;
    BleOperation currentOp = null;
    MultiTask(Collection<BleOperation> tasks)
    {
        for(BleOperation op:tasks) {
            operations.put(op.getCharacteristic(), op);
        }
        reset();
    }


    public synchronized boolean hasNext(){

            return current.hasNext();

    };

    public synchronized BleOperation next(){
        if(current.hasNext()){
            currentOp= current.next().getValue();

        }else{
            currentOp=null;
        }
        return currentOp;
    }

    public synchronized void reset(){

        current=operations.entrySet().iterator();
        currentOp=null;
        //switch to first element


    }

    @Override
    public boolean isSync() {
        return false;
    }

    @Override
    public synchronized BleOperation current() {

            return currentOp;
    }

    @Override
    public BleOperation getByName(UUID name) {
       return operations.get(name);
    }

    @Override
    public BleOperation getByName(String name) {
        return getByName(UUID.fromString(name));
    }

    @Override
    public synchronized boolean hasCurrent() {
        return currentOp!=null;
    }

    @Override
    public boolean allSucceed() {
        for(BleOperation op:operations.values()){
            if(!op.isSucceed()){
                return false;
            }
        }
        return true;
    }
}
