package ru.raiv.syncblestack.tasks;

import android.os.Handler;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Raiv on 27.01.2017.
 */

public class BleTaskBuilder {



    private ArrayList<BleOperation> operations = new ArrayList<>();

    public BleTaskBuilder (){

    }

    private BleTaskCompleteCallback callback = null;
    private boolean isAsync = false;
    private Handler callbackHandler=null;




    public BleTaskBuilder addOperation(BleOperation operation){
        operations.add(operation);
        return this;
    }

    public BleTaskBuilder addOperations(Collection<BleOperation> operations){
        this.operations.addAll(operations);
        return this;
    }

    public BleTaskBuilder setAsync(boolean isAsync){
        this.isAsync=isAsync;
        return this;
    }


    public BleTaskBuilder addCompleteCallback(BleTaskCompleteCallback callback){
        this.callback=callback;
        isAsync=true;
        return this;
    }
    public BleTaskBuilder addCallbackHandler(Handler callbackHandler){
        this.callbackHandler=callbackHandler;
        //isAsync=true;
        return this;
    }

    public BleTask build(){
        //BleTask task;
        boolean isSingle = operations.size()==1;

        if (isSingle){
            if(isAsync){
                return new SingleTaskAsync(operations.get(0),callback,callbackHandler);

            }else{
                return new SingleTaskSync(operations.get(0));

            }
        }else{
            if(isAsync){
                return new MultiTaskAsync(operations,callback,callbackHandler);

            }else{
                return new MultiTaskSync(operations);

            }
        }



    }

}
