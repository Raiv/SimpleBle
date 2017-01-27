package ru.raiv.syncblestack;

import android.annotation.SuppressLint;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ru.raiv.syncblestack.tasks.BleAsyncTask;
import ru.raiv.syncblestack.tasks.BleOperation;
import ru.raiv.syncblestack.tasks.BleSyncTask;
import ru.raiv.syncblestack.tasks.BleTask;
import ru.raiv.syncblestack.tasks.BleTaskCompleteCallback;


/**
 * Service for managing connection and data communication with a GATT server
 * hosted on a given Bluetooth LE device.
 */
@SuppressLint("NewApi") public class BluetoothLeService extends Service {
    public final static String TAG = BluetoothLeService.class.getSimpleName();

    public final static String ACTION_DEVICE_CONNECTED = "ACTION_DEVICE_CONNECTED";
    public static final String PARAM_DEVICE_NAME=ACTION_DEVICE_CONNECTED.concat("PARAM_DEVICE_NAME");
    public final static String ACTION_DEVICE_DISCONNECTED = "ACTION_DEVICE_DISCONNECTED";
    public final static String ACTION_DEVICE_ERROR = "ACTION_DEVICE_ERROR";
    public final static String PARAM_DEVICE_ERROR =ACTION_DEVICE_ERROR.concat(".ERROR");
    public final static String ACTION_DEVICES_FOUND = "ACTION_DEVICES_FOUND";
    public final static String ACTION_SEARCH_FINISHED = "ACTION_SEARCH_FINISHED";
    public final static String PARAM_DEVICES_FOUND_LIST =ACTION_DEVICES_FOUND.concat(".LIST");

    public static final IntentFilter bleServiceFilter = makeBleServiceFilter();

    private static IntentFilter makeBleServiceFilter(){
        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(ACTION_DEVICE_CONNECTED);
        iFilter.addAction(ACTION_DEVICE_DISCONNECTED);
        iFilter.addAction(ACTION_DEVICES_FOUND);
        iFilter.addAction(ACTION_DEVICE_ERROR);
        iFilter.addAction(ACTION_SEARCH_FINISHED);
        return iFilter;
    }

    private static class BluetoothDeviceWrapper {
        public BluetoothGatt gatt;
        public long scanIteration = 0;
        public boolean isReady = false;
        BluetoothDevice device = null;
    };

    // Device scan callback.
    private class LeScanCallback implements BluetoothAdapter.LeScanCallback {

        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            boolean notExists = true;
            boolean needUpdate = false;
            synchronized (gattSync) {
                for (BluetoothDeviceWrapper found : foundDevices) {
                    if (device.getAddress().equals(found.device.getAddress())) {
                        if(!found.device.getName().equals(device.getName())){
                            found.device=device;
                        }
                        if (found.scanIteration < scanIteration) {
                            found.scanIteration = scanIteration;
                            needUpdate = true;
                        }
                        notExists = false;

                        break;
                    }
                }
            }
            if (notExists) {
                needUpdate = true;
                BluetoothDeviceWrapper wrapper = new BluetoothDeviceWrapper();
                wrapper.device = device;
                wrapper.scanIteration = scanIteration;
                synchronized (gattSync) {
                    foundDevices.add(wrapper);
                }

                Log.d(TAG,myNum()+ "Le device added to processing");

            }
            if (needUpdate) {
                broadcastDeviceList();
            }
        }
    };


    private static volatile int instanceNumCount =0;
    private volatile int instanceNum =0;

    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private static final long SCAN_PERIOD = 30000;
    private volatile boolean mScanning = false;
    private volatile boolean continousScanning = true;
    private Handler mHandler;
    private volatile Queue<BleTask> taskQueue = new ArrayBlockingQueue<>(128,false);

    private final Object gattSync=new Object();
    BluetoothDeviceWrapper currentGatt=null;
    private final List<BluetoothDeviceWrapper> foundDevices=Collections.synchronizedList(new ArrayList<BluetoothDeviceWrapper>());
    private final List<BluetoothDeviceWrapper> prevFoundDevices=Collections.synchronizedList(new ArrayList<BluetoothDeviceWrapper>());
    private volatile LeScanCallback currentScan = null;


    @Override
    public void onCreate() {
        super.onCreate();
        instanceNumCount++;
        instanceNum=instanceNumCount;
        initialize();

    }

    private volatile long scanIteration = 0;

    @Override
    public String toString(){
        return instanceNum+" ";
    }

    public String myNum(){
        return instanceNum+" ";
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        close();
    }

    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {


            if (newState == BluetoothProfile.STATE_CONNECTED) {
                synchronized (gattSync) {
                    if (gatt.getDevice().getAddress().equals(currentGatt.device.getAddress())) {
                        if(status==BluetoothGatt.GATT_SUCCESS) {
                            Log.i(TAG, myNum() + "Connected to GATT server.");
                            currentGatt.gatt = gatt;
                            currentGatt.gatt.discoverServices();
                        }else{
                            broadcastGattError(status);
                        }
                    }
                }
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                synchronized (gattSync) {
                    if (gatt.equals(currentGatt.gatt)) {
                        currentGatt.gatt = null;
                        broadcastDeviceState(ACTION_DEVICE_DISCONNECTED);
                        Log.i(TAG, myNum() + "Disconnected from GATT server.");
                    }
                }
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            synchronized (gattSync) {
                if (gatt.equals(currentGatt.gatt) ) {
                    if(status == BluetoothGatt.GATT_SUCCESS) {
                        currentGatt.isReady=true;
                        broadcastDeviceState(ACTION_DEVICE_CONNECTED);
                        doJob();
                    }else{
                        broadcastGattError(status);
                    }
                }else{
                  //  broadcastDeviceState(ACTION_DEVICE_CONNECTED);
                }
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            if(gatt.equals(currentGatt.gatt)){
                if(status == BluetoothGatt.GATT_SUCCESS) {
                    finishRW(characteristic);
                }else{
                    broadcastGattError(status);
                    finishTask();
                }
            }
        }

        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            Log.d(TAG,myNum()+ "------------- onCharacteristicWrite status: " + status);
            if(gatt.equals(currentGatt.gatt)){
                if(status == BluetoothGatt.GATT_SUCCESS) {
                    finishRW(characteristic);
                }else{
                    broadcastGattError(status);
                    finishTask();
                }
            }

        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            if(gatt.equals(currentGatt.gatt)){
              // do nothing???
            }
        }

    };
    private ExecutorService syncTaskExecutor = Executors.newSingleThreadExecutor();

    public class BLEServiceBinder extends Binder {

        public void addTask(BleSyncTask task){
            synchronized (gattSync){
                taskQueue.offer(task);
                if(currentGatt!=null && currentGatt.isReady){
                    syncTaskExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            doJob();
                        }
                    });

                }
            }


        }
        public void addAsyncTask(BleAsyncTask task){
            synchronized (gattSync){
                taskQueue.add(task);
                if(currentGatt!=null && currentGatt.isReady){
                    doJob();
                }
            }
        }
        public void connectDevice(String deviceAddress){

            setScanning(false,false);
            connect(deviceAddress);
        }
        public void disconnectDevice(String deviceAddress){
            synchronized (gattSync){
                if(currentGatt!=null && currentGatt.device!=null && currentGatt.device.getAddress().equals(deviceAddress)) {
                    disconnect();
                }
            }

        }
        public void scanForDeviceOnce(){
            setScanning(true,false);
        }
        public void scanForDevices(){
            setScanning(true,true);
        }
        public void stopScanForDevice(){
            setScanning(false,false);
        }

    }
    private BluetoothGattCharacteristic findCharacteristic(BluetoothGatt gatt, BleOperation operation){
        BluetoothGattService service =gatt.getService(operation.getService());
        if(service!=null){
            return service.getCharacteristic(operation.getCharacteristic());
        }
        return null;
    }


    private void finishTask(){
        inJob=false;
        synchronized (gattSync) {
            BleTask task=taskQueue.poll();
            task.reset();
            if(task instanceof BleSyncTask){
                BleSyncTask syncTask = (BleSyncTask)task;
                synchronized (syncTask.getSyncObject()){
                    syncTask.getSyncObject().notifyAll();
                }
            }else if(task instanceof BleAsyncTask){
                final BleAsyncTask asyncTask = (BleAsyncTask)task;
                Handler callbackHandler = asyncTask.callbackHandler();
                if(callbackHandler==null && asyncTask.getCallback()!=null){
                    callbackHandler=mHandler;
                }
                if(callbackHandler!=null) {
                    callbackHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            BleTaskCompleteCallback callback = asyncTask.getCallback();
                            if (callback != null) {
                                callback.onTaskComplete(asyncTask);
                            }
                        }
                    });
                }
            }
        }
        doJob();
    }


    private void finishRW(BluetoothGattCharacteristic characteristic){
        BleTask task = null;
        synchronized (gattSync) {
            task = taskQueue.peek();
        }
        if(task!=null){
            final BleOperation operation =task.current();
            operation.setValue(characteristic.getValue());
            operation.setSucceed(true);
            finishOperation(task);
        }

    }

    private void finishOperation(BleTask task){
        if(task.hasNext()){
            inJob=false;
            doJob();
        }else{
            finishTask();
        }
    }

    private volatile boolean inJob=false;

    private void doJob(){
        if(inJob){
            return;
        }
        boolean queueEmpty;
        synchronized (gattSync) {
            queueEmpty = taskQueue.isEmpty();
        }
        if(queueEmpty){
            stopSelfIfNeeded();
        }else{
            BleTask task = null;
            boolean check = false;
            synchronized (gattSync) {
                task = taskQueue.peek();
                if (currentGatt.gatt != null && task != null && task.hasNext()) {
                    BleOperation operation = task.next();
                    BluetoothGattCharacteristic characteristic = findCharacteristic(currentGatt.gatt, operation);
                    if (characteristic == null) {
                        // return
                        operation.setSucceed(false);
                        finishTask();
                        return;
                    }
                    switch (operation.getOpType()) {
                        case READ:
                            currentGatt.gatt.readCharacteristic(characteristic);
                            break;
                        case WRITE_NO_RESPONCE:
                            characteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);

                        case WRITE:
                            characteristic.setValue(operation.getValue());
                            currentGatt.gatt.writeCharacteristic(characteristic);
                            break;
                        case CHECK:
                            operation.setSucceed(true);
                            check=true;
                            break;
                    }

                }
            }
            if(check) {
                finishOperation(task);
            }else{
                inJob=true;
            }
        }
    }

    private void broadcastScanFinish(){
        Intent i = new Intent( ACTION_SEARCH_FINISHED);
        sendBroadcast(i);
    }

    private void broadcastDeviceState(String action){
        Intent i = new Intent(action);
        synchronized (gattSync) {
            BleDeviceInfo info = new BleDeviceInfo();
            info.name=currentGatt.device.getName();
            info.address=currentGatt.device.getAddress();
            i.putExtra(PARAM_DEVICE_NAME,info );
        }
        sendBroadcast(i);
    }
    private void broadcastGattError(int status){
        Intent i = new Intent(ACTION_DEVICE_ERROR);
        synchronized (gattSync) {
            BleDeviceInfo info = new BleDeviceInfo();
            info.name=currentGatt.device.getName();
            info.address=currentGatt.device.getAddress();
            i.putExtra(PARAM_DEVICE_NAME,info );
        }
        i.putExtra(PARAM_DEVICE_ERROR,status);
        sendBroadcast(i);
    }
    private void broadcastDeviceList(){
        Intent i = new Intent(ACTION_DEVICES_FOUND);

        ArrayList<BleDeviceInfo> devices = new ArrayList<>();
        synchronized (gattSync){
            for(BluetoothDeviceWrapper wrapper:foundDevices){
                BleDeviceInfo info = new BleDeviceInfo();
                info.name=wrapper.device.getName();
                info.address=wrapper.device.getAddress();

                devices.add(info);
            }
        }
        i.putExtra(PARAM_DEVICES_FOUND_LIST,devices.toArray(new BleDeviceInfo[devices.size()]));
        sendBroadcast(i);
    }

    @Override
    public IBinder onBind(Intent intent) {
        bound = true;
        return mBinder;
    }


    private volatile boolean bound =false;


    public void stopSelfIfNeeded(){
        if(bound) return;
        synchronized (taskQueue) {
            if(taskQueue.isEmpty()) {
                close();
                setScanning(false,false);
                stopSelf();
            }
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {

        bound = false;
        stopSelfIfNeeded();
        return false;
    }

    private final IBinder mBinder = new BLEServiceBinder();


    public boolean initialize() {

        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                Log.e(TAG,myNum()+ "Unable to initialize BluetoothManager.");
                return false;
            }
        }

        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Log.e(TAG, myNum()+"Unable to obtain a BluetoothAdapter.");
            return false;
        }
        mHandler = new Handler(Looper.getMainLooper());// to ensure it runs on UI thread

        return true;
    }

    public boolean connect(final String address) {
        if (mBluetoothAdapter == null || address == null) {
            Log.w(TAG,myNum()+ "BluetoothAdapter not initialized or unspecified address.");
            return false;
        }

        BluetoothDevice device =mBluetoothAdapter.getRemoteDevice(address);
        //if(device==null){
            synchronized (gattSync) {
                 for(BluetoothDeviceWrapper bdw: foundDevices){
                     if(bdw.device!=null&& bdw.device.getAddress().equals(address)){
                         currentGatt=bdw;
                         device =bdw.device;
                         break;
                     }
                 }
            }
        //}
        if(device==null){
        synchronized (gattSync) {
            for(BluetoothDeviceWrapper bdw: prevFoundDevices){
                if(bdw.device!=null&& bdw.device.getAddress().equals(address)){
                    currentGatt=bdw;
                    device =bdw.device;
                    break;
                }
            }
        }
        }

        if (device == null) {
            Log.w(TAG, myNum()+"Device not found.  Unable to connect.");
            return false;
        }
        // We want to directly connect to the device, so we are setting the
        // autoConnect
        // parameter to false.
        synchronized (gattSync) {
            if(currentGatt==null){
                currentGatt=new BluetoothDeviceWrapper();
                currentGatt.isReady=false;
            }
            currentGatt.device=device;
            currentGatt.gatt = device.connectGatt(this, false, mGattCallback);

        }
        Log.d(TAG, myNum()+"Trying to create a new connection.");
        return true;
    }

    /**
     * Disconnects an existing connection or cancel a pending connection. The
     * disconnection result is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public void disconnect() {
        synchronized (gattSync) {
            if (currentGatt!=null &&currentGatt.gatt != null) {
                currentGatt.gatt.disconnect();
                currentGatt.gatt.close();
                currentGatt.gatt = null;
            }
        }
    }

    /**
     * After using a given BLE device, the app must call this method to ensure
     * resources are released properly.
     */
    public void close() {

        if (isScanning()) {
            setScanning(false,false);
        }
       disconnect();
    }

    public boolean isScanning() {
        return mScanning;
    }


    public void resetCardsList() {

            synchronized (gattSync) {
                prevFoundDevices.clear();
                prevFoundDevices.addAll(foundDevices);
                foundDevices.clear();
            }
    }

    public void reconnect(){
        synchronized(gattSync) {
            if (currentGatt != null && currentGatt.device != null && currentGatt.gatt == null) {
                connect(currentGatt.device.getAddress());
            }
        }
    }


    private Runnable stopScanRunnable = new Runnable() {
        @SuppressWarnings("deprecation")
        @Override
        public void run() {

            synchronized (gattSync) {
                if(currentScan == null)
                {
                    return;
                }
                mBluetoothAdapter.stopLeScan(currentScan);
                broadcastDeviceList();
              //  resetCardsList();
                if(continousScanning) {
                    scanIteration++;
                    currentScan = new LeScanCallback();
                    mBluetoothAdapter.startLeScan(currentScan);
                    mHandler.postDelayed(stopScanRunnable, SCAN_PERIOD);
                }else{
                    mScanning=false;
                    currentScan=null;
                    broadcastScanFinish();
                }
            }
        }
    };



    @SuppressWarnings("deprecation")
    public void setScanning(boolean enable, boolean continous) {
        continousScanning=continous;
        if (mScanning == enable)
            return;
        if (mBluetoothAdapter == null) {
            mScanning = false;
            return;
        }
        mScanning = enable;
        synchronized(gattSync){
            if (enable) {
                // Stops scanning after a pre-defined scan period.
                mHandler.postDelayed(stopScanRunnable, SCAN_PERIOD);
                scanIteration++;
                if(currentScan !=null)
                {
                    throw new RuntimeException("Something dublicates scans!!!");
                }
                currentScan =new LeScanCallback();
                mBluetoothAdapter.startLeScan(currentScan);
            } else {
                mHandler.removeCallbacks(stopScanRunnable);
                mBluetoothAdapter.stopLeScan(currentScan);
                currentScan = null;
                //broadcastDeviceList();
               // resetCardsList();
            }
        }
    }
}