package ru.raiv.syncblestack.tasks;

import android.os.Handler;
import android.support.annotation.NonNull;

import java.util.Collection;

/**
 * Created by Raiv on 07.01.2017.
 */

class MultiTaskAsync extends MultiTask implements BleAsyncTask {

    private final BleTaskCompleteCallback callback;
    private final Handler callbackHandler;

    MultiTaskAsync(Collection<BleOperation> tasks, BleTaskCompleteCallback callback,@NonNull Handler callbackHandler) {
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

}
