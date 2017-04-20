package ru.raiv.syncblestack.ui;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import ru.raiv.syncblestack.BleBinder;
import ru.raiv.syncblestack.BleCallbacks;
import ru.raiv.syncblestack.BleConst;
import ru.raiv.syncblestack.BleDeviceInfo;
import ru.raiv.syncblestack.BluetoothLeServiceSync;
import ru.raiv.syncblestack.R;
import ru.raiv.syncblestack.tasks.BleTask;

//import android.app.Fragment;

/**
 * Created by Raiv on 16.02.2017.
 */

public class ServiceConnectionManager {

    private final Activity context;

    private static final int REQUEST_ENABLE_BT = 0;
    private static final int REQUEST_ENABLE_GPS = 1;

    private static final int TYPE_BLE_PERMISSION = 128;
    private final BinderWrapper binderWrapper= new BinderWrapper();


    private BleBinder binder=null;
    private BluetoothAdapter mBluetoothAdapter;
    private LocationManager locationManager;
   // private boolean needBind=true;
    private boolean binding=false;
    private boolean receiverBound = false;
    private final BroadcastReceiver receiver;

    private android.app.Fragment frag;
    private android.support.v4.app.Fragment fragv4;

    private BleCallbacks callbacks;

//  class to incapsulate binder null checks
    public final class BinderWrapper{

        private BinderWrapper(){};

        public boolean addTask(BleTask task){
            if(binder!=null){
                binder.addTask(task);
                return true;
            }
            return false;
        }
        public boolean connectDevice(BleDeviceInfo device){
            if(binder!=null){
                binder.connectDevice(device);
                return true;
            }
            return false;
        }
        public boolean disconnectDevice(BleDeviceInfo device){
            if(binder!=null){
                binder.disconnectDevice(device);
                return true;
            }
            return false;
        }
        public boolean scanForDeviceOnce(){
            if(binder!=null){
                binder.scanForDeviceOnce();
                return true;
            }
            return false;
        }
        public boolean scanForDevices(){
            if(binder!=null){
                binder.scanForDevices();
                return true;
            }
            return false;
        }
        public boolean stopScanForDevice(){
            if(binder!=null){
                binder.stopScanForDevice();
                return true;
            }
            return false;
        }
    }




    public ServiceConnectionManager (@NonNull android.app.Fragment fragment, @NonNull BroadcastReceiver receiver){
        //android.app.Fragment frag =
        this.context=fragment.getActivity();
        frag = fragment;
        this.receiver=receiver;

    }

    public ServiceConnectionManager (@NonNull android.support.v4.app.Fragment fragment,@NonNull BroadcastReceiver receiver) {
        this.context = fragment.getActivity();
        fragv4=fragment;
        this.receiver = receiver;

    }

    public ServiceConnectionManager (@NonNull Activity context,@NonNull BroadcastReceiver receiver){
        this.context=context;
        this.receiver=receiver;
    }

    public ServiceConnectionManager (@NonNull android.app.Fragment fragment,@NonNull BleCallbacks callbacks){
        //android.app.Fragment frag =
        this.context=fragment.getActivity();
        frag = fragment;
        this.receiver=new BleDefaultBroadcastReceiver(callbacks);
        this.callbacks=callbacks;
    }

    public ServiceConnectionManager (@NonNull android.support.v4.app.Fragment fragment,@NonNull BleCallbacks callbacks) {
        this.context = fragment.getActivity();
        fragv4=fragment;
        this.receiver =new BleDefaultBroadcastReceiver(callbacks);
        this.callbacks=callbacks;
    }

    public ServiceConnectionManager (@NonNull Activity context,@NonNull BleCallbacks callbacks){
        this.context=context;
        this.receiver=new BleDefaultBroadcastReceiver(callbacks);
        this.callbacks=callbacks;
    }


    public BleBinder getBinder(){
        return binder;
    }
    public BinderWrapper getWrapper(){
        return binderWrapper;
    }
    private void rebindService(){
       // if(needBind) {
           // showWaitDialog(getString(R.string.bindingService));
            Intent intent = new Intent(context, BluetoothLeServiceSync.class);
            context.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
            binding = true;
       // }
    }

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            if(service!=null && (service instanceof BleBinder)){
                binder = (BleBinder) service;
                binding=false;
                if(callbacks!=null){
                    callbacks.onServiceBind();
                }
            }else {
                rebindService();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            binder=null;
        }
    };

    private void closeService(){
        if (binder!=null){
            context.unbindService(mConnection);
            binder=null;
        }
    }

    private void enableAll(){
        final BluetoothManager bluetoothManager =
                (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        if(Build.VERSION.SDK_INT>=23) {
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        }
        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(context, context.getResources().getString(R.string.error_bluetooth_not_supported), Toast.LENGTH_SHORT).show();
            context.finish();
        }else{

            if(checkGps(true)&&checkBluetooth(true)) {
                // Bind to LocalService
                rebindService();
                if(!receiverBound){
                    context.registerReceiver(receiver, BleConst.bleServiceFilter);
                    receiverBound=true;
                }
            }
        }
    }
    /**Function needed to determine which type of container we have and call proper function
     *
     * function works as usual
     *
     * @param intent
     * @param requestCode
     */
    private void startActivityForResult(Intent intent, int requestCode){
            if (frag != null) {
                frag.startActivityForResult(intent, requestCode);
                return;
            }
            if (fragv4 != null) {
                fragv4.startActivityForResult(intent, requestCode);
                return;
            }
            context.startActivityForResult(intent, requestCode);
    }


    /**Function needed to determine which type of container we have and call proper function
     *
     * function works as usual
     *
     * @param permissions
     * @param requestCode
     */
    private void requestPermissions(String[] permissions, int requestCode){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (frag != null) {
                frag.requestPermissions(permissions, requestCode);
                return;
            }
            if (fragv4 != null) {
                fragv4.requestPermissions(permissions, requestCode);
                return;
            }
            context.requestPermissions(permissions, requestCode);
        }
    }

    /**Must be called in activity|fragment connected to manager
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch(requestCode) {
            case TYPE_BLE_PERMISSION:

                if(grantResults!= null ){
                    boolean all = true;
                    for(int gr:grantResults){
                        if (gr!= PackageManager.PERMISSION_GRANTED){
                            all=false;
                            break;
                        }
                    }
                    if(all) {

                        enableAll();
                    }else{
                        Toast.makeText(context,R.string.needAllPermissions,Toast.LENGTH_LONG).show();
                        context.finish();
                    }
                }
                return;
        }

    }

    /**Must be called in activity/fragment connected to manager
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // User chose not to enable Bluetooth.
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_CANCELED) {
                context.finish();
            } else {
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        enableAll();
                    }
                });
            }
        }
        if (requestCode == REQUEST_ENABLE_GPS) {
            if (!checkGps(false)) {
                context.finish();
            } else {
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        enableAll();
                    }
                });
            }
        }
    }


    public void attach(){

        if ((ContextCompat.checkSelfPermission(context.getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED)&&(ContextCompat.checkSelfPermission(context.getApplicationContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED)) {
            enableAll();
        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.BLUETOOTH_ADMIN,Manifest.permission.BLUETOOTH,Manifest.permission.ACCESS_COARSE_LOCATION}, TYPE_BLE_PERMISSION);
            }

        }


       // rebindService();


    }

    public void detach(){
        closeService();
        if(receiverBound){
            context.unregisterReceiver(receiver);
            receiverBound=false;
        }
    }

    private boolean checkGps(boolean askUser){
        if( locationManager!=null && !locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER )&& !locationManager.isProviderEnabled( LocationManager.NETWORK_PROVIDER )){
            if(askUser) {

                Intent enableGpsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(enableGpsIntent, REQUEST_ENABLE_GPS);
            }
            return false;
        }
        return true;
    }

    private boolean checkBluetooth(boolean askUser){
        if(!mBluetoothAdapter.isEnabled())
        {
            //   ArrayAdapter
            if(askUser) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
            return false;

        }
        return true;
    }

}
