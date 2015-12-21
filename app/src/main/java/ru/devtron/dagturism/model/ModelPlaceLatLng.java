package ru.devtron.dagturism.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class ModelPlaceLatLng implements Parcelable {
    private double lat;
    private double lng;
    private String description;

    public ModelPlaceLatLng () {

    }

    public ModelPlaceLatLng (Parcel input) {
        lat = input.readDouble();
        lng = input.readDouble();
        description = input.readString();
    }

    public ModelPlaceLatLng(Double lat, double lng, String description) {
        this.lat = lat;
        this.lng = lng;
        this.description = description;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(lat);
        dest.writeDouble(lng);
        dest.writeString(description);
    }

    public static final Parcelable.Creator<ModelPlace> CREATOR = new Parcelable.Creator<ModelPlace>() {
        public ModelPlace createFromParcel(Parcel in) {
            return new ModelPlace(in);
        }

        public ModelPlace[] newArray(int size){
            return new ModelPlace[size];
        }
    };
}
