package com.simple.thermalview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private ThermalImageView thermalView;
    private TextView tvThermalView;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvThermalView = findViewById(R.id.tv_thermal);

        thermalView = findViewById(R.id.thermalView);

        thermalView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this,SecondActivity.class));
            }
        });
        initData();

        handler.postDelayed(tvThermalRunnable,500);

    }

    private Runnable tvThermalRunnable = new Runnable() {
        @Override
        public void run() {
            tvThermalView.setText("35.99");
            handler.postDelayed(this,500);
        }
    };
    private void initData() {
        float[][] arrValue = new float[32][32];
        ValueBean[][] allValue = new ValueBean[32][32];

        float temp = 0;
        for (int i = 0; i < arrValue.length; i++) {
            for (int j = 0; j < arrValue[i].length; j++) {
                arrValue[i][j] = ((int) (temp * 10)) / 10.0f;
                temp += 0.1f;
                temp += 0.04;
                temp = ((int) (temp * 10)) / 10.0f;

                int color = ThermalTool.getColorByValue(temp);

                allValue[i][j] = new ValueBean(temp, color);
            }
        }

        thermalView.setThermalData(allValue);

    }
}
