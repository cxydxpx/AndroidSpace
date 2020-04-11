package com.fc.handlerthreaddemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        HandlerThread handlerThread = new HandlerThread("thread-1");
        handlerThread.start();


        MiHanlder handler = new MiHanlder(handlerThread.getLooper());
        handler.sendEmptyMessage(init_message);

    }

    private static final int init_message = 0;

    private class MiHanlder extends Handler {

        public MiHanlder(Looper looper) {
            super(looper);
            Log.i(TAG, "MiHanlder: " + Thread.currentThread().getName());

        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case init_message:

                    Log.i(TAG, "handleMessage: " + Thread.currentThread().getName());
                    break;
                default:
                    break;

            }
        }
    }
}
