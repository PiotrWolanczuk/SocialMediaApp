package wat.projectsi.client.model.notification;

import java.util.Date;

import com.google.gson.annotations.SerializedName;

public class NotificationAcquaintance extends Notification {
    public static final int TYPE =0;
    public static final int TYPE2 =1;
    public enum AcquaintanceState{
        Rejected(0), Invitation(1), Accepted(2);

        public final int value;
        AcquaintanceState(int i){
            value=i;
        }

        public static AcquaintanceState getState(int i)
        {
            switch(i)
            {
                case 1:
                    return Invitation;
                case 2:
                    return Accepted;

                default:
                    return Rejected;
            }
        }
    }

    @SerializedName("acquaintanceId")
    private long acquaintanceId;
    @SerializedName("acquaintanceState")
    private int acquaintanceState;

    public NotificationAcquaintance(Long notificationId, Date dateTimeOfSend, boolean isRead, Long notificationSenderId, String notificationSenderName, String notificationSenderSurname, long acquaintanceId, int acquaintanceState) {
        super(notificationId, dateTimeOfSend, isRead, notificationSenderId, notificationSenderName, notificationSenderSurname);
        this.acquaintanceId = acquaintanceId;
        this.acquaintanceState = acquaintanceState;
    }

    @Override
    public int getType() {
        return acquaintanceState==AcquaintanceState.Invitation.value? TYPE : TYPE2;
    }

    public long getAcquaintanceId() {
        return acquaintanceId;
    }

    public int getAcquaintanceState() {
        return acquaintanceState;
    }
}
