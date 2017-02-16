package ru.raiv.syncblestack.tasks;

import android.os.Handler;

import java.util.Collection;

/**
 * Created by Raiv on 07.01.2017.
 */

class MultiTaskAsync extends MultiTask implements BleAsyncTask {

    private BleTaskCompleteCallback callback;
    private Handler callbackHandler;

    MultiTaskAsync(Collection<BleOperation> tasks, BleTaskCompleteCallback callback, Handler callbackHandler) {
        super(tasks);
        this.callbackHandler=callbackHandler;
        this.callback=callback;
    }

    @Override
    public BleTaskCompleteCallback getCallback() {
        return callback;
    }

    @Override
    public Handler callbackHandler() {
        return callbackHandler;
    }

    public void setCallbackHandler(Handler callbackHandler) {
        this.callbackHandler = callbackHandler;
    }

    public void setCallback(BleTaskCompleteCallback callback) {
        this.callback = callback;
    }
}
