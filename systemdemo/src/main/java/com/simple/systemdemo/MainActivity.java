package com.simple.systemdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void initData() {

        try {
            Process su = Runtime.getRuntime().exec("su");
            InputStream errorStream = su.getErrorStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //创建文件
        File file = new File(getExternalCacheDir(), "test.txt");

        try {
            //判断文件是否存在
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            //开始向文件中写内容
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write("testing,,,,");
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        RootCmd.moveFileToSystem(file.getAbsolutePath(),"/system");

    }

    public void innerFile(View view) {
        initData();
    }
}
