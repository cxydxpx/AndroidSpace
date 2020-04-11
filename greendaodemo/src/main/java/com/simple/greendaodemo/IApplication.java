package com.simple.greendaodemo;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

public class IApplication extends Application {

    private DaoSession daoSession;

    public static IApplication getInstance() {
        return instance;
    }

    private static IApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;


        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(this, "swingcard.db");
        SQLiteDatabase writableDatabase = devOpenHelper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(writableDatabase);
        daoSession = daoMaster.newSession();

    }


    public DaoSession getDaoSession() {
        return daoSession;
    }

}
