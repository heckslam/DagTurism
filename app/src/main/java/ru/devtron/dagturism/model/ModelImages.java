package ru.devtron.dagturism.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by heckslam on 31.12.2015.
 */
public class ModelImages implements Parcelable {
    private List<String> images;

    public ModelImages () {

    }

    public ModelImages (Parcel input) {
        images = input.createStringArrayList();
    }

    public static final Creator<ModelImages> CREATOR = new Creator<ModelImages>() {
        @Override
        public ModelImages createFromParcel(Parcel in) {
            return new ModelImages(in);
        }

        @Override
        public ModelImages[] newArray(int size) {
            return new ModelImages[size];
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
