package ru.devtron.dagturism.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;

import java.util.List;

public class ModelPlace extends SugarRecord implements Parcelable {

    private String placeId;
    private String title;
    private String city;
    private List<String> images;
    private double lat;
    private double lng;
    private String description;

    public static final String[] PLACES = {"Махачкала", "Буйнакск", "Дагестанские огни", "Дербент"};
    public static final String[] REST = {"Любой", "Активный", "Религиозный", "Культурно-исторический", "Детский"};

    public ModelPlace () {

    }

    public ModelPlace (Parcel input) {
        placeId = input.readString();
        title = input.readString();
        city = input.readString();
        images = input.createStringArrayList();
        lat = input.readDouble();
        lng = input.readDouble();
        description = input.readString();
    }



    public ModelPlace(String placeId, String title, String city, double lat, double lng, String description) {
        this.placeId = placeId;
        this.title = title;
        this.city = city;
        this.lat = lat;
        this.lng = lng;
        this.description = description;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String id) {
        this.placeId = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
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
        dest.writeString(placeId);
        dest.writeString(title);
        dest.writeString(city);
        dest.writeStringList(images);
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

    @Override
    public String toString() {
        return "ModelPlace{" +
                "placeId='" + placeId + '\'' +
                ", title='" + title + '\'' +
                ", city='" + city + '\'' +
                ", images=" + images +
                ", lat=" + lat +
                ", lng=" + lng +
                ", description='" + description + '\'' +
                '}';
    }


}
