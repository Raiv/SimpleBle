package ru.raiv.syncblestack.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.raiv.syncblestack.BleCallbacks;

/**
 * Created by Raiv on 02.03.2017.
 */

public abstract class BleFragmentCompat extends android.support.v4.app.Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mgr = new ServiceConnectionManager(this,getBleCallbacks());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private ServiceConnectionManager mgr;

    protected abstract BleCallbacks getBleCallbacks();
    public ServiceConnectionManager getConnectionManager(){
        return mgr;
    }


    @Override
    public void onStart() {
        super.onStart();
        mgr.attach();
    }

    @Override
    public void onStop() {
        super.onStop();
        mgr.detach();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mgr.detach();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mgr.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mgr.onActivityResult(requestCode,resultCode,data);
    }
}
