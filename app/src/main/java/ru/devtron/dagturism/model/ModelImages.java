package ru.devtron.dagturism.model;

import com.orm.SugarRecord;

/**
 * Created by user on 27.01.2016.
 */
public class ModelImages extends SugarRecord{
    private String placeId;
    private String url;

    public ModelImages() {
    }

    public ModelImages(String placeId, String url) {
        this.placeId = placeId;
        this.url = url;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
