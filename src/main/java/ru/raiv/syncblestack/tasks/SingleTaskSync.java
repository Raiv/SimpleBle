package ru.raiv.syncblestack.tasks;

import android.support.annotation.NonNull;

/**
 * Created by Raiv on 07.01.2017.
 */

class SingleTaskSync extends SingleTask implements BleSyncTask {
    SingleTaskSync(@NonNull BleOperation operation) {
        super(operation);
    }

    @Override
    public Object getSyncObject() {
        return this;
    }

    @Override
    public boolean isSync() {
        return true;
    }
}
