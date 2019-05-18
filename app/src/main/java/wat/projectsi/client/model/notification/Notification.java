package wat.projectsi.client.model.notification;

import android.arch.lifecycle.ViewModel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

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

    public Notification(Long notificationId, Date dateTimeOfSend, boolean isRead, Long notificationSenderId, String notificationSenderName, String notificationSenderSurname) {
        this.notificationId = notificationId;
        this.dateTimeOfSend = dateTimeOfSend;
        this.isRead = isRead;
        this.notificationSenderId = notificationSenderId;
        this.notificationSenderName = notificationSenderName;
        this.notificationSenderSurname = notificationSenderSurname;
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
}
