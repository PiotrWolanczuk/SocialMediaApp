package wat.projectsi.client.model;

import android.arch.lifecycle.ViewModel;
import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class Post extends ViewModel implements Serializable {
    @SerializedName("postContent")
    private String postContent;
    @SerializedName("sentDate")
    private Date sentDate;
    @SerializedName("userId")
    private Long userId;
    @SerializedName("name")
    private String name;
    @SerializedName("surname")
    private  String surname;
//TODO: Implements pictures
    //@SerializedName("pictureEntityList")
    private List<Bitmap> images;
//    private Collection<PictureEntity> pictureEntityList;

    public Post(String postContent, Date sentDate, Long userId, String name, String surname, List<Bitmap> images) {
        this.postContent = postContent;
        this.sentDate = sentDate;
        this.userId = userId;
        this.name = name;
        this.surname = surname;
        this.images = images;
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

    public List<Bitmap> getImages() {
        return images;
    }

}
