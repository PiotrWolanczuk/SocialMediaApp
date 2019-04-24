package wat.projectsi.client.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

import wat.projectsi.R;
import wat.projectsi.client.model.User;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserListViewHolder>{
    private List<User> users;
    private Context context;

    public UserListAdapter(List<User> users, Context context) {
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public UserListAdapter.UserListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new UserListAdapter.UserListViewHolder(LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.activity_users, viewGroup, false));

    }

    @Override
    public void onBindViewHolder(@NonNull UserListAdapter.UserListViewHolder userListViewHolder, int i) {
        userListViewHolder.mRecycleView.setLayoutManager(new LinearLayoutManager(context, LinearLayout.HORIZONTAL, false));
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class UserListViewHolder extends RecyclerView.ViewHolder {
        private RecyclerView mRecycleView;
        UserListViewHolder(@NonNull View itemView) {
            super(itemView);
            mRecycleView = itemView.findViewById(R.id.my_recycler_view);
        }
    }
}
