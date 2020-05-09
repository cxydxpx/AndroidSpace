package com.simple.android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {

    private static ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2, new ThreadFactory() {
        private final AtomicInteger mThreadNum = new AtomicInteger(1);

        @Override
        public Thread newThread(@NonNull Runnable r) {
            Thread t = new Thread(r, "fcthread-" + mThreadNum.getAndIncrement());
            Log.d(TAG, t.getName() + " has been created");
            return t;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView viewById = findViewById(R.id.tv);

        boolean aBoolean = initData();

        viewById.setText(" " + aBoolean);


    }

    private boolean initData() {
        try {
            Future<Boolean> submit = executorService.submit(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return getCurrentNetStatus();
                }
            });
            Boolean aBoolean = submit.get();

            return aBoolean;

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static String TAG = MainActivity.class.getSimpleName();

    // 使用HTTP服务器同步
    public static boolean getCurrentNetStatus() {


        String netUrl = "http://www.baidu.com/";

        HttpURLConnection httpConn = null;
        long dateTick = 0;
        try {
            URL url = new URL(netUrl);
            URLConnection urlConn = url.openConnection();
            httpConn = (HttpURLConnection) urlConn;
            httpConn.setRequestMethod("GET");
            httpConn.setReadTimeout(30 * 1000); //30秒
            httpConn.setConnectTimeout(30 * 1000); //30秒
            httpConn.connect();
            if (httpConn.getResponseCode() >= 200) {
                Log.i(TAG, "getCurrentNetStatus: " + httpConn.getResponseCode());
                dateTick = httpConn.getHeaderFieldDate("Date", 0);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
//			e.printStackTrace();
        } finally {
            if (httpConn != null) {
                httpConn.disconnect();
            }
        }
        return dateTick != 0;
    }

}
