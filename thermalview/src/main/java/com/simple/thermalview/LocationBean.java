package com.simple.thermalview;

public class LocationBean {

    private float location;
    private int color;

    public LocationBean() {
    }

    public LocationBean(float location, int color) {
        this.location = location;
        this.color = color;
    }

    public float getLocation() {
        return location;
    }

    public void setLocation(float location) {
        this.location = location;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
