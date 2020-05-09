package com.simple.thermalview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;

public class MiViewRelativeLayout extends RelativeLayout {
    public static final String TAG = MiViewRelativeLayout.class.getSimpleName();

    public MiViewRelativeLayout(Context context) {
        super(context);
    }

    public MiViewRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MiViewRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.i(TAG, MiViewRelativeLayout.class.getSimpleName() + " onMeasure: ");
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.i(TAG, MiViewRelativeLayout.class.getSimpleName() + " onLayout: ");

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.i(TAG, MiViewRelativeLayout.class.getSimpleName() + " onDraw: ");

    }
}
