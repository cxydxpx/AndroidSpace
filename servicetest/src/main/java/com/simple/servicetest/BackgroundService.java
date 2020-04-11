package com.simple.servicetest;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class BackgroundService extends Service {

    public class InnerBinder extends Binder {

        public BackgroundService getService() {
            return BackgroundService.this;
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        return new InnerBinder();
    }

    private String TAG = "mTag";


    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "BackgroundService onCreate: ");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i(TAG, "BackgroundService onStartCommand: ");

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "BackgroundService onDestroy: ");
    }
}
