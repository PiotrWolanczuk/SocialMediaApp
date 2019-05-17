package wat.projectsi.client.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import wat.projectsi.R;
import wat.projectsi.client.DateFormatter;
import wat.projectsi.client.model.Comment;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder>{
    private List<Comment> mCommentList;
    private Context mContext;

    public CommentAdapter(Context context, List<Comment> commentItemList) {
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
        holder.mNameView.setText(mCommentList.get(i).getUserId().toString());
        //holder.mNameView.setText(mCommentList.get(i).getName() + " " + mCommentList.get(i).getSurname());
        holder.mCommentContentView.setText(mCommentList.get(i).getCommentContest());
        holder.mCommentDateView.setText(DateFormatter.viewDateTimeFormat(mContext).format(mCommentList.get(i).getSendDate()));
    }

    @Override
    public int getItemCount() {
        return mCommentList==null? 0: mCommentList.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView mCommentContentView;
        TextView mCommentDateView;
        TextView mNameView;

        public CommentViewHolder(View view) {
            super(view);
            mCommentContentView = view.findViewById(R.id.commentContent);
            mCommentDateView = view.findViewById(R.id.sendCommentDate);
            mNameView = view.findViewById(R.id.nameComment);
        }
    }
}
