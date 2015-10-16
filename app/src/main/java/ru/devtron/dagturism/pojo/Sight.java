package ru.devtron.dagturism.pojo;

public class Sight {
    private long id;
    private String img;
    private String title;
    private String descrition;

    public Sight(long id, String img, String title, String descrition) {
        this.id = id;
        this.img = img;
        this.title = title;
        this.descrition = descrition;
    }




    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescrition() {
        return descrition;
    }

    public void setDescrition(String descrition) {
        this.descrition = descrition;
    }
}
