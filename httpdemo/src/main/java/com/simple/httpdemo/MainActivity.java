package com.simple.httpdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getData();
    }


    public void getData() {
        new Thread(new Runnable() {
            @Override
            public void run() {


                try {
                    URL url = new URL("http://192.168.2.102:8080/hello");
                    URLConnection connection = url.openConnection();
                    connection.connect();
                    InputStreamReader isr = new InputStreamReader(connection.getInputStream());
                    BufferedReader br = new BufferedReader(isr);
                    String str = "";
                    StringBuilder sb = new StringBuilder();
                    while ((str = br.readLine()) != null) {
                        sb.append(str);
                    }
                    String str_log = sb.toString() + "";
                    Log.i("mainactivity", "run: " +str_log);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }

}
