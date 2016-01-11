package ru.devtron.dagturism.model;

import android.os.Parcel;
import android.os.Parcelable;


public class WaylineModel implements Parcelable {
    private double pointLat, pointLng;
    private int pointNumber;
    private String pointCaption;

    public WaylineModel() {

    }

    public WaylineModel (Parcel input) {
        pointLat = input.readDouble();
        pointLng = input.readDouble();
        pointCaption = input.readString();
        pointNumber = input.readInt();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(pointLat);
        dest.writeDouble(pointLng);
        dest.writeString(pointCaption);
        dest.writeInt(pointNumber);
    }

    public static final Parcelable.Creator<WaylineModel> CREATOR = new Parcelable.Creator<WaylineModel>() {
        public WaylineModel createFromParcel(Parcel in) {
            return new WaylineModel(in);
        }

        public WaylineModel[] newArray(int size){
            return new WaylineModel[size];
        }
    };
}