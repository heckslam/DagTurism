package ru.devtron.dagturism.model;

import java.io.Serializable;

public class WaylineModel implements Serializable {
    private double pointLat, pointLng;
    private int pointNumber;
    private String pointCaption;

    public WaylineModel() {

    }

    public double getPointLat() {
        return pointLat;
    }

    public void setPointLat(double pointLat) {
        this.pointLat = pointLat;
    }

    public double getPointLng() {
        return pointLng;
    }

    public void setPointLng(double pointLng) {
        this.pointLng = pointLng;
    }

    public int getPointNumber() {
        return pointNumber;
    }

    public void setPointNumber(int pointNumber) {
        this.pointNumber = pointNumber;
    }

    public String getPointCaption() {
        return pointCaption;
    }

    public void setPointCaption(String pointCaption) {
        this.pointCaption = pointCaption;
    }
}