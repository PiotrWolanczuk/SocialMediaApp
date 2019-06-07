package wat.projectsi.client.model.notification;

import android.arch.lifecycle.ViewModel;
import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

import wat.projectsi.client.Misc;
import wat.projectsi.client.Picture;

public abstract class Notification extends ViewModel implements Serializable {
    @SerializedName("notificationId")
    private Long notificationId;
    @SerializedName("dateTimeOfSend")
    private Date dateTimeOfSend;
    @SerializedName("isRead")
    private boolean isRead;
    @SerializedName("notificationSenderId")
    private Long notificationSenderId;
    @SerializedName("notificationSenderName")
    private String notificationSenderName;
    @SerializedName("notificationSenderSurname")
    private String notificationSenderSurname;
    //@SerializedName("notificationSenderPicture")
    //private PictureEntity notificationSenderPicture;
    private Bitmap profilePicture;

    public Notification(Long notificationId, Date dateTimeOfSend, boolean isRead, Long notificationSenderId, String notificationSenderName, String notificationSenderSurname, Bitmap profilePicture) {
        this.notificationId = notificationId;
        this.dateTimeOfSend = dateTimeOfSend;
        this.isRead = isRead;
        this.notificationSenderId = notificationSenderId;
        this.notificationSenderName = notificationSenderName;
        this.notificationSenderSurname = notificationSenderSurname;
        this.profilePicture=profilePicture;
    }

    public abstract int getType();

    public Long getNotificationId() {
        return notificationId;
    }

    public Date getDateTimeOfSend() {
        return dateTimeOfSend;
    }

    public boolean isRead() {
        return isRead;
    }

    public Long getNotificationSenderId() {
        return notificationSenderId;
    }

    public String getNotificationSenderName() {
        return notificationSenderName;
    }

    public String getNotificationSenderSurname() {
        return notificationSenderSurname;
    }

    public Bitmap getProfilePicture() {
        return profilePicture==null? Misc.defaultAvatar :profilePicture;
    }
}
