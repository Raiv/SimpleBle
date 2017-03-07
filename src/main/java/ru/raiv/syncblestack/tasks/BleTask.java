package ru.raiv.syncblestack.tasks;

import java.util.Iterator;
import java.util.UUID;

/**
 * Created by Raiv on 07.01.2017.
 */

public interface BleTask extends Iterator<BleOperation> {

    void reset();
    boolean isSync();
    BleOperation current();
    BleOperation getByName(UUID name);
    BleOperation getByName(String name);
    boolean hasCurrent();
}
