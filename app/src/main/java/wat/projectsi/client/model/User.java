package wat.projectsi.client.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

import wat.projectsi.client.Picture;

public class User implements Serializable {
    @SerializedName("userId")
    private long id;
    @SerializedName("firstName")
    private String name;
    @SerializedName("lastName")
    private String surname;
    @SerializedName("birthday")
    private Date birthday;
    @SerializedName("gender")
    private String gender;
    @SerializedName("pictureId")
    private Image image;


    public User(String name, String surname, int id, Image image) {
        this.name = name;
        this.surname = surname;
        this.id = id;
        this.image=image;
    }
    public User(String name, String surname, int id) {
        this.name = name;
        this.surname = surname;
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }


    public long getId() {
        return id;
    }

    public Date getBirthday() {
        return birthday;
    }

    public String getGender() {
        return gender;
    }

    public Image getImage() {
        return image;
    }
}
