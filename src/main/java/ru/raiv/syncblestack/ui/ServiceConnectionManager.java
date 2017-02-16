package ru.raiv.syncblestack.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import ru.raiv.syncblestack.BluetoothLeService;

/**
 * Created by Raiv on 16.02.2017.
 */

public class ServiceConnectionManager {

    Context context;


    BluetoothLeService.BLEServiceBinder binder=null;
    boolean needBind=true;
    boolean binding=false;

    public ServiceConnectionManager (Context context){
        this.context=context;
    }


    public BluetoothLeService.BLEServiceBinder getBinder(){
        return binder;
    }

    private void rebindService(){
        if(needBind) {
           // showWaitDialog(getString(R.string.bindingService));
            Intent intent = new Intent(context, BluetoothLeService.class);
            context.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
            binding = true;
        }
    }

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            if(service!=null && (service instanceof BluetoothLeService.BLEServiceBinder)){
                binder = (BluetoothLeService.BLEServiceBinder) service;
                binding=false;
                //hideWaitDialog();
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

    public void attach(){
        rebindService();
    }

    public void detach(){
        closeService();
    }


}
