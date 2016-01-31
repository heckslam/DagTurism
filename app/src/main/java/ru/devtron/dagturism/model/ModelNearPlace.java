package ru.devtron.dagturism.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by user on 30.01.2016.
 */
public class ModelNearPlace implements Parcelable {
    private String id;
    private String title;
    private int distance;
    private double lat, lng;

    private List<String> images;

    public ModelNearPlace () {

    }

    public ModelNearPlace (Parcel input) {
        id = input.readString();
        title = input.readString();
        distance = input.readInt();
        images = input.createStringArrayList();
        lat = input.readDouble();
        lng = input.readDouble();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
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

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeInt(distance);
        dest.writeStringList(images);
        dest.writeDouble(lat);
        dest.writeDouble(lng);
    }

    public static final Parcelable.Creator<ModelNearPlace> CREATOR = new Parcelable.Creator<ModelNearPlace>() {
        public ModelNearPlace createFromParcel(Parcel in) {
            return new ModelNearPlace(in);
        }

        public ModelNearPlace[] newArray(int size){
            return new ModelNearPlace[size];
        }
    };
}
