package wat.projectsi.client.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import wat.projectsi.R;
import wat.projectsi.client.DateFormatter;
import wat.projectsi.client.Picture;
import wat.projectsi.client.activity.MainActivity;
import wat.projectsi.client.model.Post;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder>{
    private List<Post> mPostItemList;
    private Context mContext;

    public PostAdapter(List<Post> postItemList, Context context) {
        mPostItemList = postItemList;
        mContext = context;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new PostViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.posts_card_main, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int i) {
        Post post=mPostItemList.get(i);
        new Picture(holder.mProfilePictureView).execute(MainActivity.getCurrentUser().getImage().getUrl());
        holder.mProfilePictureView.setTag(post.getUserId());
        holder.mNameView.setText(post.getName());
        holder.mNameView.setTag(post.getUserId());
        holder.mSurnameView.setText(post.getSurname());
        holder.mSurnameView.setTag(post.getUserId());
        holder.mPostContentView.setText(post.getPostContent());
        holder.mPostContentView.setTag(post.getUserId());
        holder.mPostDateView.setText(DateFormatter.viewDateTimeFormat(mContext).format(post.getSentDate()));
        View parent= ((View)holder.mPostContentView.getParent().getParent());
        parent.findViewById(R.id.comment_button).setTag(post.getPostId());
        parent.findViewById(R.id.delete_button).setTag(post.getPostId());

        holder.mPostImageRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayout.HORIZONTAL, false));
        holder.mPostImageRecyclerView.setAdapter(new ImageRecyclerAdapter(mContext, post.getImages()));
        holder.mPostCommentRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayout.VERTICAL, false));
        holder.mPostCommentRecyclerView.setAdapter( new CommentAdapter(mContext, post.getCommentList()));
    }

    @Override
    public int getItemCount() {
        return mPostItemList.size();
    }

    class PostViewHolder extends RecyclerView.ViewHolder {
        RecyclerView mPostImageRecyclerView;
        RecyclerView mPostCommentRecyclerView;
        TextView mPostContentView;
        TextView mPostDateView;
        TextView mNameView;
        TextView mSurnameView;
        ImageView mProfilePictureView;

        PostViewHolder(View view) {
            super(view);
            mPostImageRecyclerView = view.findViewById(R.id.imageRecyclerView);
            mPostCommentRecyclerView = view.findViewById(R.id.commentsRecyclerView);
            mPostContentView = view.findViewById(R.id.postContent);
            mPostDateView = view.findViewById(R.id.sendPostDate);
            mNameView = view.findViewById(R.id.profileName);
            mSurnameView = view.findViewById(R.id.profileSurname);
            mProfilePictureView=view.findViewById(R.id.profilePicture);
        }
    }
}
