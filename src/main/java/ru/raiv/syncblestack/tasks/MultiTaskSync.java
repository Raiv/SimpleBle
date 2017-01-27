package ru.raiv.syncblestack.tasks;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by Raiv on 07.01.2017.
 */

public class MultiTaskSync extends MultiTask implements BleSyncTask {
    public MultiTaskSync(Collection<BleOperation> tasks) {
        super(tasks);
    }
    public MultiTaskSync(BleOperation... tasks) {
        super(Arrays.asList(tasks));
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
