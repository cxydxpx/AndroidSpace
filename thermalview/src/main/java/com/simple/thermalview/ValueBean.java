package com.simple.thermalview;

public class ValueBean {

    private float value;
    private int color;

    public ValueBean() {
    }

    public ValueBean(float value, int color) {
        this.value = value;
        this.color = color;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
