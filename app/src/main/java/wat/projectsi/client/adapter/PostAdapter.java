package wat.projectsi.client.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import wat.projectsi.R;
import wat.projectsi.client.DateFormatter;
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
        holder.mNameView.setText(mPostItemList.get(i).getName() + " " + mPostItemList.get(i).getSurname());
        holder.mPostContentView.setText(mPostItemList.get(i).getPostContent());
        holder.mPostDateView.setText(DateFormatter.viewPostDateFormat(mContext).format(mPostItemList.get(i).getSentDate()));

        holder.mPostImageRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayout.HORIZONTAL, false));
        holder.mPostImageRecyclerView.setAdapter(new ImageRecyclerAdapter(mContext, mPostItemList.get(i).getImages()));
    }

    @Override
    public int getItemCount() {
        return mPostItemList.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        RecyclerView mPostImageRecyclerView;
        TextView mPostContentView;
        TextView mPostDateView;
        TextView mNameView;

        public PostViewHolder(View view) {
            super(view);
            mPostImageRecyclerView = view.findViewById(R.id.imageRecyclerView);
            mPostContentView = view.findViewById(R.id.postContent);
            mPostDateView = view.findViewById(R.id.sendPostDate);
            mNameView = view.findViewById(R.id.namePost);
        }
    }
}
