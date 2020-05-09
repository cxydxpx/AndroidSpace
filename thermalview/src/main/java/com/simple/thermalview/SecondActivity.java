package com.simple.thermalview;

import android.os.Bundle;
import android.os.Handler;
import android.text.PrecomputedText;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class SecondActivity extends AppCompatActivity {

     MiSurfaceView miSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fifth);
        miSurfaceView = findViewById(R.id.surfaceview);

        initData();
    }

    private void initData() {
        float[][] arrValue = new float[32][32];
        ValueBean[][] allValue = new ValueBean[32][32];

        float temp = 0;
        Random random = new Random();
        for (int i = 0; i < arrValue.length; i++) {
            for (int j = 0; j < arrValue[i].length; j++) {
                temp = random.nextInt(50);

                int color = ThermalTool.getColorByValue(temp);

                allValue[i][j] = new ValueBean(temp, color);
            }
        }

        miSurfaceView.setThermalData(allValue);

    }

}
