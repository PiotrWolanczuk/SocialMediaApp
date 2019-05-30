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
import wat.projectsi.client.model.Comment;
import wat.projectsi.client.model.User;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder>{
    private List<Comment> mCommentList;
    private Context mContext;

    CommentAdapter(Context context, List<Comment> commentItemList) {
        mCommentList = commentItemList;
        mContext = context;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new CommentViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.comments_card_main, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int i) {
        Comment comment =mCommentList.get(i);
        User user =comment.getUser();
        new Picture(holder.mProfilePictureView).execute(user.getProfileImage());
        holder.mNameView.setText(user.getName());
        holder.mSurnameView.setText(user.getSurname());
        holder.mCommentContentView.setText(comment.getCommentContest());
        holder.mCommentDateView.setText(DateFormatter.viewDateTimeFormat(mContext).format(comment.getSendDate()));
    }

    @Override
    public int getItemCount() {
        return mCommentList==null? 0: mCommentList.size();
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView mCommentContentView;
        TextView mCommentDateView;
        TextView mNameView;
        TextView mSurnameView;
        ImageView mProfilePictureView;

        CommentViewHolder(View view) {
            super(view);
            mCommentContentView = view.findViewById(R.id.commentContent);
            mCommentDateView = view.findViewById(R.id.sendCommentDate);
            mNameView = view.findViewById(R.id.profileName);
            mSurnameView = view.findViewById(R.id.profileSurname);
            mProfilePictureView=view.findViewById(R.id.profilePicture);
        }
    }
}
