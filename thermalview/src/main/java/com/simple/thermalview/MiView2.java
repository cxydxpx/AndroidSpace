package com.simple.thermalview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class MiView2 extends android.view.View {

    public static final String TAG = MiView2.class.getSimpleName();

    public MiView2(Context context) {
        super(context);
    }

    public MiView2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MiView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.i(TAG, MiView2.class.getSimpleName() + " onMeasure: ");
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.i(TAG, MiView2.class.getSimpleName() + " onLayout: ");

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.i(TAG, MiView2.class.getSimpleName() + " onDraw: ");

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x = getX();
        float y = getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, "onTouchEvent: down");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG, "onTouchEvent: move");

                layout(getLeft() + 1, getTop() + 1, getRight() - 1, getBottom() - 1);
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }

        return super.onTouchEvent(event);
    }
}
