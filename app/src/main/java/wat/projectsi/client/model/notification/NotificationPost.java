package wat.projectsi.client.model.notification;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class NotificationPost extends Notification {
    public static final int TYPE =3;

    @SerializedName("postId;")
    private long postId;


    public NotificationPost(Long notificationId, Date dateTimeOfSend, boolean isRead, Long notificationSenderId, String notificationSenderName, Bitmap profilePicture, String notificationSenderSurname, long postId) {
        super(notificationId, dateTimeOfSend, isRead, notificationSenderId, notificationSenderName, notificationSenderSurname, profilePicture);
        this.postId = postId;
    }

    @Override
    public int getType() {
        return TYPE;
    }

    public long getPostId() {
        return postId;
    }
}
