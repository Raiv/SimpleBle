package ru.raiv.syncblestack.tasks;

import android.os.Handler;
import android.support.annotation.NonNull;

/**
 * Created by Raiv on 07.01.2017.
 */

public class SingleTaskAsync extends SingleTask implements BleAsyncTask {
    private BleTaskCompleteCallback callback;
    private Handler callbackHandler;

    public SingleTaskAsync(@NonNull BleOperation operation) {
        super(operation);
    }
    public SingleTaskAsync(@NonNull BleOperation operation,BleTaskCompleteCallback callback) {
        super(operation);
        this.callback=callback;
    }
    public SingleTaskAsync(@NonNull BleOperation operation,BleTaskCompleteCallback callback,Handler callbackHandler) {
        super(operation);
        this.callback=callback;
        this.callbackHandler=callbackHandler;
    }

    @Override
    public BleTaskCompleteCallback getCallback() {
        return callback;
    }

    @Override
    public Handler callbackHandler() {
        return null;
    }

    public Handler getCallbackHandler() {
        return callbackHandler;
    }

    public void setCallbackHandler(Handler callbackHandler) {
        this.callbackHandler = callbackHandler;
    }

    public void setCallback(BleTaskCompleteCallback callback) {
        this.callback = callback;
    }
}
