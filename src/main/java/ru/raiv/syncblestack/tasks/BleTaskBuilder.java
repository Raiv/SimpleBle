package ru.raiv.syncblestack.tasks;

import android.os.Handler;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

/**
 * Created by Raiv on 27.01.2017.
 */

public class BleTaskBuilder {



    private ArrayList<BleOperation> operations = new ArrayList<>();

    private final BleOperationFactory operationFactory;

    public BleTaskBuilder(BleOperationFactory operationFactory){
        this.operationFactory=operationFactory;
    }

    public BleTaskBuilder(UUID defaultService){
        this.operationFactory= new BleOperationFactory(defaultService);
    }


    public BleTaskBuilder(String defaultService){
        this.operationFactory= new BleOperationFactory(defaultService);
    }

    public BleTaskBuilder (){

        operationFactory = new BleOperationFactory();
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


    /*read*/
    public BleTaskBuilder addReadOperation(@NonNull UUID readService, @NonNull UUID readCharacteristic){
        return addOperation(BleOperationFactory.getReadOperation(readService,readCharacteristic));
    }

    public BleTaskBuilder addReadOperation(@NonNull UUID readCharacteristic){
        return addOperation(operationFactory.getReadOperation(readCharacteristic));
    }

    public  BleTaskBuilder addReadOperation(@NonNull String readService,@NonNull String readCharacteristic){
        return addOperation(BleOperationFactory.getReadOperation(readService,readCharacteristic));
    }

    public BleTaskBuilder addReadOperation(@NonNull String readCharacteristic){
        return addOperation(operationFactory.getReadOperation(readCharacteristic));
    }


    /*write*/

    public BleTaskBuilder addWriteOperation(@NonNull UUID writeService,@NonNull UUID writeCharacteristic, byte[] writeValue){
        return addOperation(BleOperationFactory.getWriteOperation(writeService,writeCharacteristic,writeValue));
    }

    public  BleTaskBuilder addWriteOperation(@NonNull UUID writeCharacteristic, byte[] writeValue){
        return addOperation(operationFactory.getWriteOperation(writeCharacteristic,writeValue));
    }


    public  BleTaskBuilder addWriteOperation(@NonNull String writeService,@NonNull String writeCharacteristic, byte[] writeValue){
        return addOperation(BleOperationFactory.getWriteOperation(writeService,writeCharacteristic,writeValue));
    }

    public  BleTaskBuilder addWriteOperation(@NonNull String writeCharacteristic, byte[] writeValue){
        return addOperation(operationFactory.getWriteOperation(writeCharacteristic,writeValue));
    }


    /*write no response*/

    public  BleTaskBuilder addWriteNoResponseOperation(@NonNull UUID writeService,@NonNull UUID writeCharacteristic,@NonNull byte[] writeValue){
        return addOperation(BleOperationFactory.getWriteNoResponseOperation(writeService,writeCharacteristic,writeValue));
    }

    public  BleTaskBuilder addWriteNoResponseOperation(@NonNull UUID writeCharacteristic,@NonNull byte[] writeValue){
        return addOperation(operationFactory.getWriteNoResponseOperation(writeCharacteristic,writeValue));
    }


    public  BleTaskBuilder addWriteNoResponseOperation(@NonNull String writeService,@NonNull String writeCharacteristic,@NonNull byte[] writeValue){
        return addOperation(BleOperationFactory.getWriteNoResponseOperation(writeService,writeCharacteristic,writeValue));
    }

    public  BleTaskBuilder addWriteNoResponseOperation(@NonNull String writeCharacteristic,@NonNull byte[] writeValue){
        return addOperation(operationFactory.getWriteNoResponseOperation(writeCharacteristic,writeValue));
    }

    /*check*/
    public  BleTaskBuilder addCheckOperation(@NonNull UUID checkService,@NonNull UUID checkCharacteristic){
        return addOperation(BleOperationFactory.getCheckOperation(checkService,checkCharacteristic));
    }

    public  BleTaskBuilder addCheckOperation(@NonNull UUID checkCharacteristic){
        return addOperation(operationFactory.getCheckOperation(checkCharacteristic));
    }


    public  BleTaskBuilder addCheckOperation(@NonNull String checkService,@NonNull String checkCharacteristic){
        return addOperation(BleOperationFactory.getCheckOperation(checkService,checkCharacteristic));
    }

    public  BleTaskBuilder addCheckOperation(@NonNull String checkCharacteristic){
        return addOperation(operationFactory.getCheckOperation(checkCharacteristic));
    }

    /*listen*/
    public  BleTaskBuilder addListenOperation(@NonNull UUID listenService,@NonNull UUID listenCharacteristic){
        return addOperation(BleOperationFactory.getListenOperation(listenService,listenCharacteristic));
    }

    public BleTaskBuilder addListenOperation(@NonNull UUID listenCharacteristic){
        return addOperation(operationFactory.getListenOperation(listenCharacteristic));
    }


    public  BleTaskBuilder addListenOperation(@NonNull String listenService,@NonNull String listenCharacteristic){
        return addOperation(BleOperationFactory.getListenOperation(listenService,listenCharacteristic));
    }

    public  BleTaskBuilder addListenOperation(@NonNull String listenCharacteristic){
        return addOperation(operationFactory.getListenOperation(listenCharacteristic));
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

            if(isAsync){
                return new MultiTaskAsync(operations,callback,callbackHandler!=null?callbackHandler:new Handler());

            }else{
                return new MultiTaskSync(operations);

            }
    }

}
