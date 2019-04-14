package wat.projectsi.client.model;

import android.arch.lifecycle.ViewModel;

import java.util.Collection;
import java.util.Date;

public class Post  extends ViewModel {
    private String postContent;
    private Date sentDate;
    private Long userId;
    private String name;
    private  String surname;
    //private Collection<PictureEntity> pictureEntityList;

    public Post(String postContent, Date sentDate, Long userId, String name, String surname) {
        this.postContent = postContent;
        this.sentDate = sentDate;
        this.userId = userId;
        this.name = name;
        this.surname = surname;
    }

    public String getPostContent() {
        return postContent;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public Long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    /*
        public PostDTO(String postContent, Date sentDate, Long userId, String name, String surname, Collection<PictureEntity> pictureEntityList) {
        this.postContent = postContent;
        this.sentDate = sentDate;
        this.userId = userId;
        this.name = name;
        this.surname = surname;
        this.pictureEntityList = pictureEntityList;
     */
}
