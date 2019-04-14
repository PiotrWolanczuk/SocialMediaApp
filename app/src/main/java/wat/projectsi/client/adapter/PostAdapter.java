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
import wat.projectsi.client.Validator;
import wat.projectsi.client.model.Post;

//TODO: Implement PostAdapter
public class PostAdapter extends  RecyclerView.Adapter<PostAdapter.PostViewHolder>{
    private List<Post> mPostItemList;
    Context mContext;

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
        //TODO: Implement
        //holder.mPostImage.setImageResource(mPostItemList.get(i));
        holder.mPostContentView.setText(mPostItemList.get(i).getPostContent());
        holder.mPostDateView.setText(Validator.viewPostDateFormat.format(mPostItemList.get(i).getSentDate()));

        holder. mPostImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Implement
            }
        });
    }


    @Override
    public int getItemCount() {
        return mPostItemList.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        ImageView mPostImageView;
        TextView mPostContentView;
        TextView mPostDateView;

        public PostViewHolder(View view) {
            super(view);
            mPostImageView = view.findViewById(R.id.postImage);
            mPostContentView = view.findViewById(R.id.postContent);
            mPostDateView = view.findViewById(R.id.sendPostDate);
        }
    }
}
