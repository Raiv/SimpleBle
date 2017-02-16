package ru.raiv.syncblestack.tasks;

import java.util.Collection;

/**
 * Created by Raiv on 07.01.2017.
 */

class MultiTaskSync extends MultiTask implements BleSyncTask {
    MultiTaskSync(Collection<BleOperation> tasks) {
        super(tasks);
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
