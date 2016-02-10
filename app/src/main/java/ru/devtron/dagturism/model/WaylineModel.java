package ru.devtron.dagturism.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;


public class WaylineModel extends SugarRecord implements Parcelable {
    private String placeId;
    private double pointLat, pointLng;
    private int pointNumber;
    private String pointCaption;

    public WaylineModel() {

    }

    public WaylineModel(String placeId, double pointLat, double pointLng, int pointNumber, String pointCaption) {
        this.placeId = placeId;
        this.pointLat = pointLat;
        this.pointLng = pointLng;
        this.pointNumber = pointNumber;
        this.pointCaption = pointCaption;
    }

    public WaylineModel (Parcel input) {
        placeId = input.readString();
        pointLat = input.readDouble();
        pointLng = input.readDouble();
        pointNumber = input.readInt();
        pointCaption = input.readString();
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
        dest.writeString(placeId);
        dest.writeDouble(pointLat);
        dest.writeDouble(pointLng);
        dest.writeInt(pointNumber);
        dest.writeString(pointCaption);
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