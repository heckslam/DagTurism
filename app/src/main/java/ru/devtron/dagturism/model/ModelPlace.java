package ru.devtron.dagturism.model;

public class ModelPlace {
    private int id;
    private String title;
    private String city;
    private String firstImage;


    public static final String[] PLACES = {"Махачкала", "Буйнакск", "Дагестанские огни", "Дербент"};
    public static final String[] REST = {"Любой", "Активный отдых", "Религиозный отдых", "Исторический"};

    public ModelPlace () {

    }

    public ModelPlace(int id, String title, String city, String firstImage) {
        this.id = id;
        this.title = title;
        this.city = city;
        this.firstImage = firstImage;
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


    public String getFirstImage() {
        return firstImage;
    }

    public void setFirstImage(String firstImage) {
        this.firstImage = firstImage;
    }
}
