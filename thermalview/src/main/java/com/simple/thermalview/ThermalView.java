package com.simple.thermalview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;


import java.text.DecimalFormat;

public class ThermalView extends View implements View.OnTouchListener {


    private int screenWidth;
    private int screenHeight;

    public ThermalView(Context context) {
        super(context);
        initView(context);
    }

    public ThermalView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ThermalView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private Paint mPaint, mPaintLine, mPaintText, mPaintImportance;

    static ValueBean[][] localArr = new ValueBean[32][32];

    private Context mContext;

    private void initView(Context context) {

//        Log.i(TAG, "initView:  初始化");

        setOnTouchListener(this);

        mContext = context;

        // 画主要内容 32*32的矩形
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        // 画底部线
        mPaintLine = new Paint();
        mPaintLine.setStrokeWidth(20);
        mPaintLine.setAntiAlias(true);
        // 画主要内容中的文字
        mPaintText = new Paint();
        mPaintText.setColor(Color.BLACK);
        mPaintText.setTextSize(8);
        mPaintText.setStrokeCap(Paint.Cap.BUTT);
        mPaintText.setAntiAlias(true);
        // 画人脸识别 被标记的部分
        mPaintImportance = new Paint();
        mPaintImportance.setColor(Color.BLACK);
        mPaintImportance.setStrokeCap(Paint.Cap.BUTT);
        mPaintImportance.setStyle(Paint.Style.STROKE);
        mPaintImportance.setAntiAlias(true);

    }

    private float meanWidth;
    private float meanHeigth;


    private static String TAG = "mTag";


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        if (screenWidth > screenHeight) {
            mPaintText.setStrokeWidth(meanHeigth);
        } else {
            mPaintText.setStrokeWidth(meanWidth);
        }

        if (localArr.length == 0) {
            return;
        }

        drawThermal(canvas);

        canvas.drawRect(
                100,
                100,
                100 + 150,
                100 + 200,
                mPaintImportance);

//        drawThermalBottomLine(canvas);

//        bitmap = Bitmap.createBitmap(640, 640, Bitmap.Config.ARGB_8888);

//        Canvas canvas1 = new Canvas(bitmap);

//        drawThermal(canvas1);

//        drawThermalBottomLine(canvas1);
//
//        bitmap.getWidth();


    }

    /**
     * 绘制底部线彩色线
     *
     * @param canvas
     */
    private void drawThermalBottomLine(Canvas canvas) {

        for (int i = 0; i < bottomDatas.length; i++) {
            LocationBean bean = bottomDatas[i];
            if (bean == null) {
                return;
            }
            mPaintLine.setColor(bean.getColor());
//            Log.i(TAG, "drawThermalBottomLine: " + i);
            canvas.drawPoint(20 + bean.getLocation(), screenHeight + 50, mPaintLine);
        }

    }


    private LocationBean[] bottomDatas = new LocationBean[255 * 5];


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
//                Log.i(TAG, "onDraw: " + i + " == " + k);
                mPaint.setColor(bean.getColor());
//                canvas.drawPoint(10 + k * meanWidth, 10 + i * meanHeigth, mPaint);

                canvas.drawRect(
                        k * meanWidth,
                        i * meanHeigth,
                        (k + 1) * meanWidth,
                        (i + 1) * meanHeigth,
                        mPaint);


                String str = fmt.format(bean.getValue());

                Paint.FontMetrics fontMetrics = mPaintText.getFontMetrics();
                float fontX = mPaintText.measureText(str);

                canvas.drawText(
                        str,
                        k * meanWidth + meanWidth * 0.5f - fontX * 0.5f,
                        i * meanHeigth + meanHeigth * 0.75f,
                        mPaintText);

//                thermalViewBottom = 20 + i * meanHeigth + meanHeigth / 2;
//                thermalViewRight = 20 + k * meanWidth + meanWidth / 2;

            }
        }
    }

    private static DecimalFormat fmt = new DecimalFormat("#0.0");


    public void setData(ValueBean[][] arrValue) {

        if (isDraging) {
            return;
        }
        for (int i = 0; i < arrValue.length; i++) {
            for (int j = 0; j < arrValue[i].length; j++) {
                localArr[i][j] = arrValue[i][j];
            }
        }

        LocationBean[] bottomLineData = ThermalTool.getBottomLineData();

        for (int i = 0; i < bottomLineData.length; i++) {
            bottomDatas[i] = bottomLineData[i];
        }

        invalidate();
    }

    private int oriLeft;
    private int oriRight;
    private int oriTop;
    private int oriBottom;

    protected int lastX;
    protected int lastY;

    private int dragDirection;

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                oriLeft = v.getLeft();
                oriRight = v.getRight();
                oriTop = v.getTop();
                oriBottom = v.getBottom();
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();

                dragDirection = getDirection(v, (int) event.getX(),
                        (int) event.getY());
                break;
            default:
                break;
        }

        // 处理拖动事件
        delDrag(v, event, action);
        // 如果没有拖拽事件 不执行刷新
        invalidate();
        return false;
    }

    // 拖拽中
    private boolean isDraging = false;

    private void delDrag(View v, MotionEvent event, int action) {
        switch (action) {
            case MotionEvent.ACTION_MOVE:

                isDraging = true;

                int dx = (int) event.getRawX() - lastX;
                int dy = (int) event.getRawY() - lastY;
                switch (dragDirection) {
                    case LEFT: // 左边缘
                        left(v, dx);
                        break;
                    case RIGHT: // 右边缘
                        right(v, dx);
                        break;
                    case BOTTOM: // 下边缘
                        bottom(v, dy);
                        break;
                    case TOP: // 上边缘
                        top(v, dy);
                        break;
                    case CENTER: // 点击中心-->>移动
//                        center(v, dx, dy);
                        break;
                    case LEFT_BOTTOM: // 左下
                        left(v, dx);
                        bottom(v, dy);
                        break;
                    case LEFT_TOP: // 左上
                        left(v, dx);
                        top(v, dy);
                        break;
                    case RIGHT_BOTTOM: // 右下
                        right(v, dx);
                        bottom(v, dy);
                        break;
                    case RIGHT_TOP: // 右上
                        right(v, dx);
                        top(v, dy);
                        break;
                }
                if (dragDirection != CENTER) {
                    v.layout(oriLeft,
                            oriTop,
                            oriRight,
                            oriBottom);
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

    private int getDirection(View v, int x, int y) {
        int left = v.getLeft();
        int right = v.getRight();
        int bottom = v.getBottom() - 200;
        int top = v.getTop();
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

    private int offset = 0;

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
        if (right > screenWidth + offset) {
            right = screenWidth + offset;
            left = right - v.getWidth();
        }
        if (top < -offset) {
            top = -offset;
            bottom = top + v.getHeight();
        }
        if (bottom > screenHeight + offset) {
            bottom = screenHeight + offset;
            top = bottom - v.getHeight();
        }
        v.layout(left, top, right, bottom);
    }

    /**
     * 触摸点为上边缘
     *
     * @param v
     * @param dy
     */
    private void top(View v, int dy) {
        oriTop += dy;
        if (oriTop < offset) {
            oriTop = offset;
        }
        if (oriBottom - oriTop - offset < 400) {
            oriTop = oriBottom - offset - 400;
        }

        if (oriBottom > screenHeight) {
            oriBottom = screenHeight;
        }

        float currentMean = (oriBottom - oriTop) / 32.0f;
        meanHeigth = currentMean;
    }

    /**
     * 触摸点为下边缘
     *
     * @param v
     * @param dy
     */
    private void bottom(View v, int dy) {
        oriBottom += dy;

//        if (oriBottom > screenHeight + offset) {
//            oriBottom = screenHeight + offset;
//        }
//        if (oriBottom - oriTop - 2 * offset < 200) {
//            oriBottom = 200 + oriTop + 2 * offset;
//        }

        if (oriBottom > screenHeight) {
            oriBottom = screenHeight;
        }
        if (oriBottom - oriTop < 400) {
            oriBottom = 400 + oriTop;
        }

        if (oriBottom > screenHeight) {
            oriBottom = screenHeight;
        }

        float currentMean = (oriBottom - oriTop) / 32.0f;
        meanHeigth = currentMean;
    }

    /**
     * 触摸点为右边缘
     *
     * @param v
     * @param dx
     */
    private void right(View v, int dx) {
        oriRight += dx;
        if (oriRight > screenWidth) {
            oriRight = screenWidth;
        }
        if (oriRight - oriLeft < 400) {
            oriRight = oriLeft + 400;
        }

        float currentMean = (oriRight - oriLeft) / 32.0f;
        meanWidth = currentMean;
    }

    /**
     * 触摸点为左边缘
     *
     * @param v
     * @param dx
     */
    private void left(View v, int dx) {
        oriLeft += dx;
        Log.i(TAG, "left: " + oriLeft);

        if (oriLeft <= 0) {
            oriLeft = 0;
        }
        if (oriRight - oriLeft < 400) {
            oriLeft = oriRight - 400;
        }

        float currentMean = ((oriRight - 20) - (oriLeft + 20)) / 32.0f;
        meanWidth = currentMean;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 获取宽-测量规则的模式和大小
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        // 获取高-测量规则的模式和大小
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        // 设置wrap_content的默认宽 / 高值
        // 默认宽/高的设定并无固定依据,根据需要灵活设置
        // 类似TextView,ImageView等针对wrap_content均在onMeasure()对设置默认宽 / 高值有特殊处理,具体读者可以自行查看

        // 1217 * 800
        int mWidth = widthSize - 250;
        int mHeight = heightSize - 50;

        setMeasuredDimension(mWidth, mHeight);

        // 当布局参数设置为wrap_content时，设置默认值
//        if (getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT
//                && getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT) {
//            setMeasuredDimension(mWidth, mHeight);
//            // 宽 / 高任意一个布局参数为= wrap_content时，都设置默认值
//        } else if (getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT) {
//            setMeasuredDimension(mWidth, heightSize);
//        } else if (getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT) {
//            setMeasuredDimension(widthSize, mHeight);
//        }
    }

    private boolean onlyOne = true;
    private int currentViewTop;

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (onlyOne) {

            currentViewTop = getTop();

            onlyOne = false;
            screenWidth = getWidth();
            screenHeight = getHeight();
            meanWidth = (float) (screenWidth / 32.0);
            meanHeigth = (float) (screenHeight / 32.0);
        }


    }
}
