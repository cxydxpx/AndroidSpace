package com.simple.thermalview;

import android.app.Application;

public class MeApplication extends Application {

    public static MeApplication getInstance() {
        return instance;
    }

    private static MeApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
    }

}
