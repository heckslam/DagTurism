package ru.devtron.dagturism.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;


public class WaylineModel extends SugarRecord implements Parcelable {
    private String placeId;
    private double pointLat, pointLng;
    private int pointNumber;
    private String pointCaption;



    private int finalPrice;

    public WaylineModel() {

    }

    public WaylineModel(String placeId, double pointLat, double pointLng, int pointNumber, String pointCaption, int finalPrice) {
        this.placeId = placeId;
        this.pointLat = pointLat;
        this.pointLng = pointLng;
        this.pointNumber = pointNumber;
        this.pointCaption = pointCaption;
        this.finalPrice = finalPrice;
    }

    public WaylineModel (Parcel input) {
        placeId = input.readString();
        pointLat = input.readDouble();
        pointLng = input.readDouble();
        pointCaption = input.readString();
        pointNumber = input.readInt();
        finalPrice = input.readInt();
    }

    public int getFinalPrice() {
        return finalPrice;
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
        dest.writeString(placeId);
        dest.writeInt(pointNumber);
        dest.writeInt(finalPrice);
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