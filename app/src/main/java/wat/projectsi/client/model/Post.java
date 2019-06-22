package wat.projectsi.client.model;

import android.arch.lifecycle.ViewModel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Post extends ViewModel implements Serializable {
    @SerializedName("postId")
    private long postId;
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
    @SerializedName("pictureEntityList")
    private List<Image> images;
    private List<Comment> mCommentList;
    private User user;


    public Post(long postId ,String postContent, Date sentDate, Long userId, String name, String surname) {
   // public Post(long postId ,String postContent, Date sentDate, Long userId, String name, String surname, List<String> images) {
        this.postId=postId;
        this.postContent = postContent;
        this.sentDate = sentDate;
        this.userId = userId;
        this.name = name;
        this.surname = surname;
        //this.images = images;
        mCommentList= new ArrayList<>();
    }

    public long getPostId() {
        return postId;
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

    public List<Image> getImages() {

        return images;
    }

    public List<Comment> getCommentList() {
        return mCommentList;
    }

    public void setCommentList(List<Comment> commentList) {
        if(mCommentList==null)
            mCommentList= new ArrayList<>();
        else mCommentList.clear();
        mCommentList.addAll(commentList);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
