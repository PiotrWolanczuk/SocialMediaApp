package wat.projectsi.client.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {
    @SerializedName("messageId")
    private Long messageId;
    @SerializedName("sender")
    private User sender;
    @SerializedName("reciever")
    private User reciever;
    @SerializedName("content")
    private String content;
    @SerializedName("date")
    private Date date;

    public Long getMessageId() {
        return messageId;
    }

    public User getSender() {
        return sender;
    }

    public User getReciever() {
        return reciever;
    }

    public String getContent() {
        return content;
    }

    public Date getDate() {
        return date;
    }
}
