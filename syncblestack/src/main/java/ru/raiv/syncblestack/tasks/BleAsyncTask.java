package ru.raiv.syncblestack.tasks;

import android.os.Handler;

/**
 * Created by Raiv on 07.01.2017.
 */

public interface BleAsyncTask extends BleTask{
    public BleTaskCompleteCallback getCallback();
    public Handler callbackHandler();

}
