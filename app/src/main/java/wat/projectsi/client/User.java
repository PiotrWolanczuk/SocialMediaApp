package wat.projectsi.client;

public class User {
    private String name;
    private String surname;
    private String imageHashCode;

    public User(){

    }

    public User(String name, String surname ) {
        this.name = name;
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getImageHashCode() {
        return imageHashCode;
    }

    public void setImageHashCode(String imageHashCode) {
        this.imageHashCode = imageHashCode;
    }
}
