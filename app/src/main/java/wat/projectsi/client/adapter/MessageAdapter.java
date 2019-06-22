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
import wat.projectsi.client.Picture;
import wat.projectsi.client.model.Message;
import wat.projectsi.client.model.User;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{
    private List<Message> mMessageList;
    private Context mContext;

    public MessageAdapter(Context context, List<Message> messageItemList) {
        mMessageList = messageItemList;
        mContext = context;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MessageViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.messages_card_chat, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int i) {
        Message message = mMessageList.get(i);
        User user =message.getSender();
        new Picture(holder.mProfilePictureView).execute(user.getImage().getUrl());
        holder.mProfilePictureView.setTag(user.getId());
        holder.mNameView.setText(user.getName());
        holder.mNameView.setTag(user.getId());
        holder.mSurnameView.setTag(user.getId());
        holder.mSurnameView.setText(user.getSurname());
        holder.mMessageContentView.setText(message.getContent());
        holder.mMessageDateView.setText(DateFormatter.viewDateTimeFormat(mContext).format(message.getDate()));
    }

    @Override
    public int getItemCount() {
        return mMessageList ==null? 0: mMessageList.size();
    }

    class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView mMessageContentView;
        TextView mMessageDateView;
        TextView mNameView;
        TextView mSurnameView;
        ImageView mProfilePictureView;

        MessageViewHolder(View view) {
            super(view);
            mMessageContentView = view.findViewById(R.id.messageContent);
            mMessageDateView = view.findViewById(R.id.messageDate);
            mNameView = view.findViewById(R.id.profileName);
            mSurnameView = view.findViewById(R.id.profileSurname);
            mProfilePictureView=view.findViewById(R.id.profilePicture);
        }
    }
}
