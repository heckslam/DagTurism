package ru.devtron.dagturism.model;

import java.util.ArrayList;
import java.util.List;

public class ModelPlace {
    private int id;
    private String title;
    private String city;
    private List<String> images;



    public static final String[] PLACES = {"Махачкала", "Буйнакск", "Дагестанские огни", "Дербент"};
    public static final String[] REST = {"Любой", "Активный отдых", "Религиозный отдых", "Исторический"};

    public ModelPlace () {

    }

    public ModelPlace(int id, String title, String city, List<String> images) {
        this.id = id;
        this.title = title;
        this.city = city;
        this.images = images;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

}
