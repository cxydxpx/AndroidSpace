package com.simple.thermalview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

public class ThermalTool {

    public static int getColorByValue(float value) {

        if (value >= 0 && value <= 10) {
//            B = 255G = 0R = 255 -> 0
            return parseFirstStage(value);
        } else if (value > 10 && value <= 20) {
            return parseSecondStage(value);
        } else if (value > 20 && value <= 30) {
            return parseThirdStage(value);
        } else if (value > 30 && value <= 40) {
            return parseFourthStage(value);
        } else if (value > 40 && value <= 50) {
            return parseFifthStage(value);
        } else {
            // 低于0度及大于50度的返回白色
            return Color.argb(alphaValue, 255, 0, 255);
        }

    }

    private static int alphaValue = 125;

    /**
     * 第五阶段
     *
     * @param value
     * @return
     */
    public static int parseFifthStage(float value) {
        // R = 255 G = 0 B = 0 -> 255

        float v = (float) (255.0 / 200.0);

        /**
         *  30 - 40 一百等份儿 倒叙
         *  value 当前温度
         *  v 是每一等份儿的值
         */
        double colorValue = 10 * v * (value - 30);

        int color = Color.argb(alphaValue, 255, 0, (int) colorValue);

        return color;
    }

    /**
     * 第四阶段
     *
     * @param value
     * @return
     */
    public static int parseFourthStage(float value) {

        // R = 255 G = 0 B = 0 -> 255

        float v = (float) (255.0 / 200.0);

        /**
         *  30 - 40 一百等份儿 倒叙
         *  value 当前温度
         *  v 是每一等份儿的值
         */
        double colorValue = 10 * v * (value - 30);

        int color = Color.argb(alphaValue, 255, 0, (int) colorValue);

        return color;
    }

    /**
     * 第三阶段
     *
     * @param value
     * @return
     */
    public static int parseThirdStage(float value) {

        // R = 255 B = 0 G = 255 - > 0
        float v = (float) (255.0 / 100.0);

        /**
         *  20 - 30 一百等份儿 倒叙
         *  value 当前温度
         *  v 是每一等份儿的值
         */
        double colorValue = 255 - 10 * v * (value - 20);

        int color = Color.argb(alphaValue, 255, (int) colorValue, 0);

        return color;
    }

    /**
     * 第二阶段
     *
     * @param value
     * @return
     */
    public static int parseSecondStage(float value) {

        // B = 0 G = 255 R = 0 -> 255

        float v = (float) (255.0 / 100.0);

        /**
         *  10 - 20 一百等份儿 倒叙
         *  value 当前温度
         *  v 是每一等份儿的值
         */
        double colorValue = 10 * v * (value - 10);

        int color = Color.argb(alphaValue, (int) colorValue, 255, 0);

        return color;
    }

    /**
     * 第一阶段
     *
     * @param value
     * @return
     */
    public static int parseFirstStage(float value) {

        // R = 0 G = 255 255 -> 0
        float v = (float) (255.0 / 100.0);

        /**
         *  1 - 10 一百等份儿 倒叙
         *  value 当前温度
         *  v 是每一等份儿的值
         */
        double colorValue = 255 - 10 * v * value;

        int color = Color.argb(alphaValue, 0, 255, (int) colorValue);

        return color;

    }

    public static LocationBean[] getBottomLineData() {
        int displayWidth = ThermalTool.getDisplayWidth(MeApplication.getInstance().getApplicationContext());

        float fragment = (float) ((displayWidth - 180) / 5.0);

        /**
         * 每一等分儿是196 然后一共有255个颜色 每0.7一个颜色
         */
        int rgb = 255;
        float param = fragment / rgb;

        LocationBean[] arr = new LocationBean[rgb * 5];

        for (int i = 0; i < rgb * 5; i++) {
            LocationBean bean = new LocationBean();
            bean.setLocation(param * i);
            if (i >= 0 && i < rgb) {
                //Color.argb(alphaValue, 0, 255, (int) colorValue)
                bean.setColor(Color.argb(alphaValue, rgb - i, 0, rgb));
            } else if (i >= rgb && i < rgb * 2) {
                //B = 255 R = 0 G = 0 -> 255
                bean.setColor(Color.argb(alphaValue, 0, i - rgb, rgb));
            } else if (i >= rgb * 2 && i < rgb * 3) {
                //G = 255 R = 0 B = 255 -> 0
                bean.setColor(Color.argb(alphaValue, 0, rgb, rgb - (i - rgb * 2)));
            } else if (i >= rgb * 3 && i < rgb * 4) {
                //G = 255 B= 0 R = 0 -> 255
                bean.setColor(Color.argb(alphaValue, i - rgb * 3, rgb, 0));
            } else if (i >= rgb * 4 && i < rgb * 5) {
                //R = 255 B =0 G = 255 -> 0
                bean.setColor(Color.argb(alphaValue, rgb, rgb - (i - rgb * 4), 0));
            } else {
                bean.setColor(Color.argb(alphaValue, rgb, rgb, 0));
            }
            arr[i] = bean;
        }

        return arr;
    }

    private static String TAG = "mTag";

    /**
     * 获取屏幕宽度
     *
     * @param context
     */
    public static int getDisplayWidth(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point sizePoint = new Point();
        display.getSize(sizePoint);
        int width = sizePoint.x;
        int height = sizePoint.y;
//        Log.i(TAG, "getDisplayWidth: " + width + " ====  " + height);
        return width;
    }

    /**
     * 获取屏幕宽度
     *
     * @param context
     */
    public static int getDisplayHeight(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point sizePoint = new Point();
        display.getSize(sizePoint);
        int width = sizePoint.x;
        int height = sizePoint.y;
        Log.i(TAG, "getDisplayWidth: " + width + " ====  " + height);
        return height;
    }


}
