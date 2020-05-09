package com.simple.thermalview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

public class MiViewTextView extends TextView {
    public static final String TAG = MiViewTextView.class.getSimpleName();

    public MiViewTextView(Context context) {
        super(context);
    }

    public MiViewTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MiViewTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        Log.i(TAG, MiView3.class.getSimpleName() + " onMeasure: ");
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
//        Log.i(TAG, MiView3.class.getSimpleName() + " onLayout: ");

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        Log.i(TAG, MiView3.class.getSimpleName() + " onDraw: ");

    }
}
