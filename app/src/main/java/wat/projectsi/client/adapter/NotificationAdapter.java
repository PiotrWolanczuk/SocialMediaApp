package wat.projectsi.client.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import wat.projectsi.R;
import wat.projectsi.client.DateFormatter;
import wat.projectsi.client.model.notification.Notification;
import wat.projectsi.client.model.notification.NotificationAcquaintance;
import wat.projectsi.client.model.notification.NotificationAcquaintance.AcquaintanceState;
import wat.projectsi.client.model.notification.NotificationMessage;
import wat.projectsi.client.model.notification.NotificationPost;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {
    private Context mContext;
    private LayoutInflater mInflater;

    private List<Notification> mNotificationItemList;

    public NotificationAdapter(List<Notification> notificationItemList, Context context) {
        mContext = context;
        mNotificationItemList = notificationItemList;
        mInflater = (LayoutInflater.from(context));
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        switch (viewType)
        {
            case NotificationAcquaintance.TYPE:
                return new NotificationAcquaintanceViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notifications_acquainstance_card_main, viewGroup, false));
            case NotificationAcquaintance.TYPE2:
            default:
                return new NotificationAcquaintanceViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notifications_default_card_main, viewGroup, false));
            case NotificationMessage.TYPE:
                return new NotificationMessageViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notifications_message_card_main, viewGroup, false));
            case NotificationPost.TYPE:
                return new NotificationPostViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notifications_default_card_main, viewGroup, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder viewHolder, int i) {

        viewHolder.mNotificationDateTimeOfSend.setText(DateFormatter.viewDateTimeFormat(mContext).format(mNotificationItemList.get(i).getDateTimeOfSend()));
        viewHolder.mNotificationIsRead.setText(mNotificationItemList.get(i).isRead() ? mContext.getString(R.string.read) : mContext.getString(R.string.not_read));
        viewHolder.mNotificationSender.setText(mNotificationItemList.get(i).getNotificationSenderName() + " " + mNotificationItemList.get(i).getNotificationSenderSurname());
        viewHolder.mNotificationProfilePictureView.setImageBitmap(mNotificationItemList.get(i).getProfilePicture());

        switch (viewHolder.getItemViewType()) {
            case NotificationAcquaintance.TYPE2:{

                NotificationAcquaintanceViewHolder holder = (NotificationAcquaintanceViewHolder) viewHolder;
                holder.setAcquaintanceState(
                        AcquaintanceState.getState(((NotificationAcquaintance) mNotificationItemList.get(i)).getAcquaintanceState()));

                switch (holder.mAcquaintanceState) {
                    case Accepted:
                        viewHolder.mNotificationDescription.setText(R.string.prompt_notification_desc_accept);
                        break;
                    case Rejected:
                        viewHolder.mNotificationDescription.setText(R.string.prompt_notification_desc_reject);
                        break;
                }
            }
                break;
            case NotificationAcquaintance.TYPE: {
                viewHolder.mNotificationDescription.setText(R.string.prompt_notification_desc_invitation);

                View parent= (View)viewHolder.mNotificationDescription.getParent().getParent();
                long acquaintanceId= ((NotificationAcquaintance)mNotificationItemList.get(i)).getAcquaintanceId();
                        parent.findViewById(R.id.accept_invitation_button).setTag(acquaintanceId);
                        parent.findViewById(R.id.reject_invitation_button).setTag(acquaintanceId);
            }
            break;

            case NotificationMessage.TYPE: {
                viewHolder.mNotificationDescription.setText(R.string.prompt_notification_desc_message);
//                NotificationMessageViewHolder holder = (NotificationMessageViewHolder) viewHolder;
            }
            break;

            case NotificationPost.TYPE: {
                viewHolder.mNotificationDescription.setText(R.string.prompt_notification_desc_post);
//                NotificationPostViewHolder holder = (NotificationPostViewHolder) viewHolder;
            }
            break;
        }
    }

    @Override
    public int getItemCount() {
        return mNotificationItemList.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return mNotificationItemList.get(position).getType();
    }

    public abstract class NotificationViewHolder extends RecyclerView.ViewHolder{
        protected TextView mNotificationDateTimeOfSend;
        protected TextView mNotificationIsRead;
        protected TextView mNotificationSender;
        protected ImageView mNotificationProfilePictureView;
        protected TextView mNotificationDescription;

        public NotificationViewHolder(View view) {
            super(view);
            mNotificationDateTimeOfSend = view.findViewById(R.id.notificationDateTimeOfSend);
            mNotificationIsRead = view.findViewById(R.id.notificationIsRead);
            mNotificationSender = view.findViewById(R.id.notificationSender);
            mNotificationProfilePictureView = view.findViewById(R.id.notificationSenderPicture);
            mNotificationDescription = view.findViewById(R.id.notificationDescription);
        }
    }

    public class NotificationAcquaintanceViewHolder extends NotificationViewHolder {
        private AcquaintanceState mAcquaintanceState;

        public NotificationAcquaintanceViewHolder(View view) {
            super(view);
        }

        public void setAcquaintanceState(AcquaintanceState acquaintanceState)
        {
            mAcquaintanceState=acquaintanceState;
        }
    }

    public class NotificationMessageViewHolder extends NotificationViewHolder {

        public NotificationMessageViewHolder(View view) {
            super(view);
        }
    }

    public class NotificationPostViewHolder extends NotificationViewHolder {

        public NotificationPostViewHolder(View view) {
            super(view);
        }
    }
}
