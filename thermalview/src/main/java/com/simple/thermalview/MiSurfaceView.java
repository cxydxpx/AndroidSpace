package com.simple.thermalview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.text.DecimalFormat;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.simple.thermalview.ThermalView.localArr;

public class MiSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    // 布局 宽度/高度
    private int currentViewMaxWidth;
    private int currentViewMaxHeight;

    private SurfaceHolder surfaceHolder;

    public MiSurfaceView(Context context) {
        super(context);
        initView();
    }

    public MiSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MiSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private Paint mPaint, mPaintText;
    // 数据源
    private ValueBean[][] localArr;
    // 默认宽高规格
    private int widthNum = 32;
    private int heigthNum = 32;
    //屏幕宽高度等份儿
    private float meanWidth;
    private float meanHeigth;

    private void initView() {
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);


        // 画主要内容 32*32的矩形
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);

        // 画主要内容中的文字
        mPaintText = new Paint();
        mPaintText.setColor(Color.BLACK);
        mPaintText.setTextSize(8);
        mPaintText.setStrokeCap(Paint.Cap.BUTT);
        mPaintText.setAntiAlias(true);

        localArr = new ValueBean[widthNum][heigthNum];

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

                localArr[i][j] = new ValueBean(temp, color);
            }
        }

    }

    private boolean isRunning;

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        isRunning = true;
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isRunning = false;
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                draw();
                TimeUnit.MICROSECONDS.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void draw() {
        Canvas canvas = surfaceHolder.lockCanvas();
        if (null != canvas) {
            try {
                if (currentViewMaxWidth > currentViewMaxHeight) {
                    mPaintText.setStrokeWidth(meanHeigth);
                } else {
                    mPaintText.setStrokeWidth(meanWidth);
                }

                if (localArr.length == 0) {
                    return;
                }

                drawThermal(canvas);

//                canvas.drawRect(0, 0, 500, 500, mPaint);

//                drawThermalImport(canvas);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    /**
     * 开始画图
     *
     * @param canvas
     */
    private void drawThermal(Canvas canvas) {
        for (int i = 0; i < localArr.length; i++) {
            ValueBean[] childenArr = localArr[i];
            for (int k = 0; k < childenArr.length; k++) {
                ValueBean bean = childenArr[k];
                if (bean == null) {
                    return;
                }
                mPaint.setColor(bean.getColor());
//                canvas.drawPoint(10 + k * meanWidth, 10 + i * meanHeigth, mPaint);
                canvas.drawRect(
                        k * meanWidth,
                        i * meanHeigth,
                        (k + 1) * meanWidth,
                        (i + 1) * meanHeigth,
                        mPaint);

                canvas.drawText(
                        fmt.format(bean.getValue()) + "",
                        k * meanWidth + meanWidth * 0.3f,
                        i * meanHeigth + meanHeigth * 0.3f, mPaintText);

            }
        }

    }

    private static DecimalFormat fmt = new DecimalFormat("#0.0");

    /**
     * 设置温度数据
     *
     * @param arrValue
     */
    public void setThermalData(ValueBean[][] arrValue) {

        if (isDraging) {
            return;
        }
        if (arrValue.length <= 0) {
            return;
        }

        // 初始化 宽度及高度
        if (isInitWindthHeight) {
            widthNum = arrValue.length;
            heigthNum = arrValue[0].length;

            meanWidth = currentViewMaxWidth / Float.valueOf(widthNum);
            meanHeigth = currentViewMaxHeight / Float.valueOf(heigthNum);
            isInitWindthHeight = false;
        }

        localArr = new ValueBean[widthNum][heigthNum];

        for (int i = 0; i < arrValue.length; i++) {
            for (int j = 0; j < arrValue[i].length; j++) {
                localArr[i][j] = arrValue[i][j];
            }
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // 我第二次拖拽的时候 这里获取的又是最大值了 比如我拖拽到500了
                // 但第二次开始拖拽的时候 这里获取的又是1000（最大值）了
                oriLeft = getLeft();
                oriRight = getRight();
                oriTop = getTop();
                oriBottom = getBottom();
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();

                Log.i(TAG, "onTouchEvent:  "
                        + "  left == " + oriLeft
                        + "  right == " + oriRight
                        + "  top == " + oriTop
                        + "  bottom == " + oriBottom
                );
                dragDirection = getDirection((int) event.getX(),
                        (int) event.getY());
                break;
            default:
                break;
        }

        // 处理拖动事件
        delDrag(event, action);
        if (isRunning) {
            draw();
        }

        return true;
    }


    private void delDrag(MotionEvent event, int action) {
        switch (action) {
            case MotionEvent.ACTION_MOVE:

                isDraging = true;

                int dx = (int) event.getRawX() - lastX;
                int dy = (int) event.getRawY() - lastY;
                switch (dragDirection) {
                    case LEFT: // 左边缘
//                        left(v, dx);
                        break;
                    case RIGHT: // 右边缘
                        right(dx);
                        break;
                    case BOTTOM: // 下边缘
                        bottom(dy);
                        break;
                    case TOP: // 上边缘
//                        top(v, dy);
                        break;
                    case CENTER: // 点击中心-->>移动
//                        center(v, dx, dy);
                        break;
                    case LEFT_BOTTOM: // 左下
                        left(dx);
                        bottom(dy);
                        break;
                    case LEFT_TOP: // 左上
//                        left(v, dx);
//                        top(v, dy);
                        break;
                    case RIGHT_BOTTOM: // 右下
                        right(dx);
                        bottom(dy);
                        break;
                    case RIGHT_TOP: // 右上
                        right(dx);
                        top(dy);
                        break;
                }
                if (dragDirection != CENTER) {
                    Log.i(TAG, "delDrag:  "
                            + "  left == " + oriLeft
                            + "  right == " + oriRight
                            + "  top == " + oriTop
                            + "  bottom == " + oriBottom
                    );


                    layout(oriLeft, oriTop, oriRight, oriBottom); //这里改掉

////                    //通过这个方法来更新view，计算出左、上、右、下的值设置进去
//                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) getLayoutParams();
//                    // 通过修改params里面的值更新view
//                    layoutParams.setMargins(
//                            oriLeft,
//                            oriTop,
//                            currentViewMaxWidth - oriRight,
//                            currentViewMaxHeight - oriBottom);
//                    setLayoutParams(layoutParams);
                }
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                dragDirection = 0;
                break;
        }

        isDraging = false;
    }

    private static final int TOP = 0x15;
    private static final int LEFT = 0x16;
    private static final int BOTTOM = 0x17;
    private static final int RIGHT = 0x18;
    private static final int LEFT_TOP = 0x11;
    private static final int RIGHT_TOP = 0x12;
    private static final int LEFT_BOTTOM = 0x13;
    private static final int RIGHT_BOTTOM = 0x14;
    private static final int CENTER = 0x19;

    private int getDirection(int x, int y) {
        int left = getLeft();
        int right = getRight();
        int bottom = getBottom();
        int top = getTop();
        if (x < 40 && y < 40) {
            return LEFT_TOP;
        }
        if (y < 40 && right - left - x < 40) {
            return RIGHT_TOP;
        }
        if (x < 40 && bottom - top - y < 40) {
            return LEFT_BOTTOM;
        }
        if (right - left - x < 40 && bottom - top - y < 40) {
            return RIGHT_BOTTOM;
        }
        if (x < 40) {
            return LEFT;
        }
        if (y < 40) {
            return TOP;
        }
        if (right - left - x < 40) {
            return RIGHT;
        }
        if (bottom - top - y < 40) {
            return BOTTOM;
        }
        return CENTER;
    }

    /**
     * 触摸点为中心->>移动
     *
     * @param v
     * @param dx
     * @param dy
     */
    private void center(View v, int dx, int dy) {
        int left = v.getLeft() + dx;
        int top = v.getTop() + dy;
        int right = v.getRight() + dx;
        int bottom = v.getBottom() + dy;
        if (left < -offset) {
            left = -offset;
            right = left + v.getWidth();
        }
        if (right > currentViewMaxWidth + offset) {
            right = currentViewMaxWidth + offset;
            left = right - v.getWidth();
        }
        if (top < -offset) {
            top = -offset;
            bottom = top + v.getHeight();
        }
        if (bottom > currentViewMaxHeight + offset) {
            bottom = currentViewMaxHeight + offset;
            top = bottom - v.getHeight();
        }
        v.layout(left, top, right, bottom);
    }

    /**
     * 触摸点为上边缘
     *
     * @param dy
     */
    private void top(int dy) {
        oriTop += dy;
        if (oriTop < offset) {
            oriTop = offset;
        }
        if (oriBottom - oriTop - offset < 400) {
            oriTop = oriBottom - offset - 400;
        }

        float currentMean = (oriBottom - oriTop) / widthNum;
        meanHeigth = currentMean;
    }

    /**
     * 触摸点为下边缘
     *
     * @param dy
     */
    private void bottom(int dy) {
        oriBottom += dy;

//        if (oriBottom > currentViewMaxHeight + offset) {
//            oriBottom = currentViewMaxHeight + offset;
//        }
//        if (oriBottom - oriTop - 2 * offset < 200) {
//            oriBottom = 200 + oriTop + 2 * offset;
//        }

        if (oriBottom > currentViewMaxHeight) {
            oriBottom = currentViewMaxHeight;
        }
        if (oriBottom - oriTop - offset < 400) {
            oriBottom = 400 + oriTop + offset;
        }

        float currentMean = (oriBottom - oriTop) / Float.valueOf(heigthNum);
        meanHeigth = currentMean;
    }

    /**
     * 触摸点为右边缘
     *
     * @param dx
     */
    private void right(int dx) {
        oriRight += dx;
        if (oriRight > currentViewMaxWidth) {
            oriRight = currentViewMaxWidth;
        }
        if (oriRight - oriLeft <= 400) {
            oriRight = oriLeft + offset + 400;
        }

        float currentMean = (oriRight - oriLeft) / Float.valueOf(widthNum);
        meanWidth = currentMean;
    }

    private final static String TAG = MiSurfaceView.class.getSimpleName();

    // 拖拽中
    private boolean isDraging = false;
    // 第一次初始化
    private boolean isInitWindthHeight = true;
    // 热成像矩形 上下左右坐标
    private int oriLeft;
    private int oriRight;
    private int oriTop;
    private int oriBottom;
    // 矩形距离屏幕边界距离
    protected int lastX;
    protected int lastY;
    // 拖动方向
    private int dragDirection;
    // 起始间距
    private int offset = 0;

    /**
     * 触摸点为左边缘
     *
     * @param dx
     */
    private void left(int dx) {
        oriLeft += dx;
        Log.i(TAG, "left: " + oriLeft);

        if (oriLeft < offset) {
            oriLeft = offset;
        }
        if (oriRight - oriLeft - offset < 400) {
            oriLeft = oriRight - offset - 400;
        }

        float currentMean = (oriRight - oriLeft) / Float.valueOf(widthNum);
        meanWidth = currentMean;
    }

}
