package ru.raiv.syncblestack.tasks;

import android.support.annotation.NonNull;

import java.util.UUID;

/**
 * Created by Raiv on 18.04.2017.
 */

public class BleOperationFactory {

    private UUID defaultService = null;


    public BleOperationFactory(){
        defaultService = null;
    }



    public BleOperationFactory(@NonNull String defaultService){
        this.defaultService=UUID.fromString(defaultService);
    }

    public BleOperationFactory(@NonNull UUID defaultService){
        this.defaultService=defaultService;
    }

    /*read*/
    public static BleOperation getReadOperation(@NonNull UUID readService,@NonNull UUID readCharacteristic){
        return new BleOperation(readService,readCharacteristic,null,BleOperationType.READ);
    }

    public BleOperation getReadOperation(@NonNull UUID readCharacteristic){
        if(defaultService==null){
            throw new RuntimeException("BleOperationFactory: no default service set!");
        }
        return getReadOperation(defaultService,readCharacteristic);
    }


    public static BleOperation getReadOperation(@NonNull String readService,@NonNull String readCharacteristic){
        return new BleOperation(readService,readCharacteristic,null,BleOperationType.READ);
    }

    public BleOperation getReadOperation(@NonNull String readCharacteristic){
        if(defaultService==null){
            throw new RuntimeException("BleOperationFactory: no default service set!");
        }
        return getReadOperation(defaultService,UUID.fromString(readCharacteristic));
    }


    /*write*/

    public static BleOperation getWriteOperation(@NonNull UUID writeService,@NonNull UUID writeCharacteristic,@NonNull byte[] writeValue){
        return new BleOperation(writeService,writeCharacteristic,writeValue,BleOperationType.WRITE);
    }

    public BleOperation getWriteOperation(@NonNull UUID writeCharacteristic,@NonNull byte[] writeValue){
        if(defaultService==null){
            throw new RuntimeException("BleOperationFactory: no default service set!");
        }
        return getWriteOperation(defaultService,writeCharacteristic,writeValue);
    }


    public static BleOperation getWriteOperation(@NonNull String writeService,@NonNull String writeCharacteristic,@NonNull byte[] writeValue){
        return new BleOperation(writeService,writeCharacteristic,writeValue,BleOperationType.WRITE);
    }

    public BleOperation getWriteOperation(@NonNull String writeCharacteristic,@NonNull byte[] writeValue){
        if(defaultService==null){
            throw new RuntimeException("BleOperationFactory: no default service set!");
        }
        return getWriteOperation(defaultService,UUID.fromString(writeCharacteristic),writeValue);
    }


    /*write no response*/

    public static BleOperation getWriteNoResponseOperation(@NonNull UUID writeService,@NonNull UUID writeCharacteristic,@NonNull byte[] writeValue){
        return new BleOperation(writeService,writeCharacteristic,writeValue,BleOperationType.WRITE_NO_RESPONSE);
    }

    public BleOperation getWriteNoResponseOperation(@NonNull UUID writeCharacteristic,@NonNull byte[] writeValue){
        if(defaultService==null){
            throw new RuntimeException("BleOperationFactory: no default service set!");
        }
        return getWriteNoResponseOperation(defaultService,writeCharacteristic,writeValue);
    }


    public static BleOperation getWriteNoResponseOperation(@NonNull String writeService,@NonNull String writeCharacteristic,@NonNull byte[] writeValue){
        return new BleOperation(writeService,writeCharacteristic,writeValue,BleOperationType.WRITE_NO_RESPONSE);
    }

    public BleOperation getWriteNoResponseOperation(@NonNull String writeCharacteristic,@NonNull byte[] writeValue){
        if(defaultService==null){
            throw new RuntimeException("BleOperationFactory: no default service set!");
        }
        return getWriteNoResponseOperation(defaultService,UUID.fromString(writeCharacteristic),writeValue);
    }

    /*check*/
    public static BleOperation getCheckOperation(@NonNull UUID checkService,@NonNull UUID checkCharacteristic){
        return new BleOperation(checkService,checkCharacteristic,null,BleOperationType.CHECK);
    }

    public BleOperation getCheckOperation(@NonNull UUID checkCharacteristic){
        if(defaultService==null){
            throw new RuntimeException("BleOperationFactory: no default service set!");
        }
        return getCheckOperation(defaultService,checkCharacteristic);
    }


    public static BleOperation getCheckOperation(@NonNull String checkService,@NonNull String checkCharacteristic){
        return new BleOperation(checkService,checkCharacteristic,null,BleOperationType.CHECK);
    }

    public BleOperation getCheckOperation(@NonNull String checkCharacteristic){
        if(defaultService==null){
            throw new RuntimeException("BleOperationFactory: no default service set!");
        }
        return getCheckOperation(defaultService,UUID.fromString(checkCharacteristic));
    }

    /*listen*/
    public static BleOperation getListenOperation(@NonNull UUID listenService,@NonNull UUID listenCharacteristic){
        return new BleOperation(listenService,listenCharacteristic,null,BleOperationType.LISTEN);
    }

    public BleOperation getListenOperation(@NonNull UUID listenCharacteristic){
        if(defaultService==null){
            throw new RuntimeException("BleOperationFactory: no default service set!");
        }
        return getListenOperation(defaultService,listenCharacteristic);
    }


    public static BleOperation getListenOperation(@NonNull String listenService,@NonNull String listenCharacteristic){
        return new BleOperation(listenService,listenCharacteristic,null,BleOperationType.LISTEN);
    }

    public BleOperation getListenOperation(@NonNull String listenCharacteristic){
        if(defaultService==null){
            throw new RuntimeException("BleOperationFactory: no default service set!");
        }
        return getListenOperation(defaultService,UUID.fromString(listenCharacteristic));
    }


    public UUID getDefaultService() {
        return defaultService;
    }

    public void setDefaultService(UUID defaultService) {
        this.defaultService = defaultService;
    }
}
