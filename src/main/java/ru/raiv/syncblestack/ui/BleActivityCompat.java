package ru.raiv.syncblestack.ui;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Raiv on 02.03.2017.
 */

public abstract class BleActivityCompat extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mgr = new ServiceConnectionManager(this,getBleReceiver());
    }

    private ServiceConnectionManager mgr;

    protected abstract BroadcastReceiver getBleReceiver();
    public ServiceConnectionManager getConnectionManager(){
        return mgr;
    }


    @Override
    protected void onStart() {
        super.onStart();
        mgr.attach();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mgr.detach();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mgr.detach();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mgr.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mgr.onActivityResult(requestCode,resultCode,data);
    }
}
