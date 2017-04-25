package ru.raiv.syncblestack.tasks;

import java.util.Collection;

/**
 * Created by Raiv on 07.01.2017.
 */

class MultiTaskSync extends MultiTask implements BleSyncTask {
    MultiTaskSync(Collection<BleOperation> tasks) {
        super(tasks);
    }
    private final Object sync=new Object();
    @Override
    public Object getSyncObject() {
        return sync;
    }

    @Override
    public boolean isSync() {
        return true;
    }
}
