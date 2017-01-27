package ru.raiv.syncblestack.tasks;

import android.os.Handler;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by Raiv on 07.01.2017.
 */

public class MultiTaskAsync extends MultiTask implements BleAsyncTask {

    private BleTaskCompleteCallback callback;
    private Handler callbackHandler;

    public MultiTaskAsync(Collection<BleOperation> tasks) {
        super(tasks);
    }
    public MultiTaskAsync(BleOperation... tasks) {
        super(Arrays.asList(tasks));
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
