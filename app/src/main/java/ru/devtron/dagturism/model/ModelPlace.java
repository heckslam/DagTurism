package ru.devtron.dagturism.model;
/**
 * Класс для создания типа для элементов ListView. Поля: id -Это идентификатор элемента.
 * img -будет хронить url до картинок на сервере
 * title  - будет хронить заголовки элементов списка из сервера
 * descrition - описание достопримечательности
 * @created 14.10.2015
 * @version $Revision 738 $
 * @author Эльвира Темирханова
 * since 0.0.1
 */
public class ModelPlace {
    private long id;
    private String img;
    private String title;
    private String description;

    public static final String[] PLACES = {"Махачкала", "Буйнакск", "Дагестанские огни", "Дербент"};
    public static final String[] REST = {"Любой", "Активный отдых", "Религиозный отдых", "Исторический"};

    public ModelPlace () {

    }
    public ModelPlace(long id, String img, String title, String description) {
        this.id = id;
        this.img = img;
        this.title = title;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
