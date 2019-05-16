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
    @SerializedName("userId")
    private Long userId;
    @SerializedName("commentContest")
    private String commentContest;
    @SerializedName("sendDate")
    private Date sendDate;

    public Comment(long commentId, Long postId, Long userId, String commentContest, Date sendDate) {
        this.commentId = commentId;
        this.postId = postId;
        this.userId = userId;
        this.commentContest = commentContest;
        this.sendDate = sendDate;
    }

    public long getCommentId() {
        return commentId;
    }

    public Long getPostId() {
        return postId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getCommentContest() {
        return commentContest;
    }

    public Date getSendDate() {
        return sendDate;
    }
}
