package ru.raiv.syncblestack.tasks;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Raiv on 07.01.2017.
 */

class MultiTask implements BleTask{

    ArrayList<BleOperation> operation=new ArrayList<BleOperation>();
    int innerIndex = -1;

    MultiTask(Collection<BleOperation> tasks){
        operation.addAll(tasks);
    }


    public synchronized boolean hasNext(){

            return innerIndex<operation.size()-1;

    };

    public synchronized BleOperation next(){


                if(innerIndex<operation.size()-1) {
                    innerIndex++;
                    return operation.get(innerIndex);
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
                return operation.get(innerIndex);
            }

        return null;
    }

    @Override
    public synchronized boolean hasCurrent() {
        return innerIndex>=0;
    }
}
