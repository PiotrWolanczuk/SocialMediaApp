package wat.projectsi.client.model;

import android.arch.lifecycle.ViewModel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class Comment extends ViewModel implements Serializable {
    @SerializedName("commentId")
    private long commentId;
    @SerializedName("postId")
    private Long postId;
    @SerializedName("user")
    private User user;
    @SerializedName("commentContest")
    private String commentContest;
    @SerializedName("sendDate")
    private Date sendDate;
    private String  profilePicture;

    public Comment(long commentId, Long postId, User user, String commentContest, Date sendDate) {
        this.commentId = commentId;
        this.postId = postId;
        this.user = user;
        this.commentContest = commentContest;
        this.sendDate = sendDate;
    }

    public long getCommentId() {
        return commentId;
    }

    public Long getPostId() {
        return postId;
    }

    public User getUser() {
        return user;
    }

    public String getCommentContest() {
        return commentContest;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
}
