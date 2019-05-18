package wat.projectsi.client.model;

import android.graphics.Bitmap;

public class User {
    private String name;
    private String surname;
    private int id;
    private Bitmap profileImage;

    public User(){

    }

    public User(String name, String surname, int id, Bitmap profileImage) {
        this.name = name;
        this.surname = surname;
        this.id = id;
        this.profileImage = profileImage;
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

    public Bitmap getProfileImage() {
        return profileImage;
    }

    public int getId() {
        return id;
    }
}
