package ru.devtron.dagturism.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by heckslam on 31.12.2015.
 */
public class ModelGallery implements Parcelable {
    private List<String> images;

    public ModelGallery() {

    }

    public ModelGallery(Parcel input) {
        images = input.createStringArrayList();
    }

    public static final Creator<ModelGallery> CREATOR = new Creator<ModelGallery>() {
        @Override
        public ModelGallery createFromParcel(Parcel in) {
            return new ModelGallery(in);
        }

        @Override
        public ModelGallery[] newArray(int size) {
            return new ModelGallery[size];
        }
    };

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
        dest.writeStringList(images);
    }
}
