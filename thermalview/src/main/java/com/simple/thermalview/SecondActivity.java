package com.simple.thermalview;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {

    private View view1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fifth);
        view1 = findViewById(R.id.view1);
    }

    public void click(View view) {
        view1.layout(view1.getLeft(), view1.getTop(), view1.getRight(), view1.getBottom() - 100);
    }
}
