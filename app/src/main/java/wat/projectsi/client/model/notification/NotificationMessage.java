package wat.projectsi.client.model.notification;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class NotificationMessage extends Notification {
    public static final int TYPE =2;

    @SerializedName("messageId")
    private long messageId;


    public NotificationMessage(Long notificationId, Date dateTimeOfSend, boolean isRead, Long notificationSenderId, String notificationSenderName, Bitmap profilePicture, String notificationSenderSurname, long messageId) {
        super(notificationId, dateTimeOfSend, isRead, notificationSenderId, notificationSenderName, notificationSenderSurname, profilePicture);
        this.messageId = messageId;
    }

    @Override
    public int getType() {
        return TYPE;
    }

    public long getMessageId() {
        return messageId;
    }
}
