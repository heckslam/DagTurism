package ru.devtron.dagturism.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class ModelPlace implements Parcelable {
    private String id;
    private String title;
    private String city;
    private List<String> images;


    public static final String[] PLACES = {"Махачкала", "Буйнакск", "Дагестанские огни", "Дербент"};
    public static final String[] REST = {"Любой", "Активный отдых", "Религиозный отдых", "Исторический"};

    public ModelPlace () {

    }

    public ModelPlace (Parcel input) {
        id = input.readString();
        title = input.readString();
        city = input.readString();
        images = input.createStringArrayList();
    }

    public ModelPlace(String id, String title, String city, List<String> images) {
        this.id = id;
        this.title = title;
        this.city = city;
        this.images = images;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(city);
        dest.writeStringList(images);
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
