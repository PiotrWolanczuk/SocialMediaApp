package wat.projectsi.client.model;

import android.graphics.Bitmap;

public class User {
    private String name;
    private String surname;
    private Bitmap profileImage;

    public User(){

    }

    public User(String name, String surname,  Bitmap profileImage ) {
        this.name = name;
        this.surname = surname;
        this.profileImage = profileImage;
    }
    public User(String name, String surname) {
        this.name = name;
        this.surname = surname;
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
}
