package wat.projectsi.client.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import wat.projectsi.R;
import wat.projectsi.client.model.User;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserListViewHolder>{
    private List<User> users;

    public UserListAdapter(List<User> users) {
        this.users = users;
    }

    @NonNull
    @Override
    public UserListAdapter.UserListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new UserListAdapter.UserListViewHolder(LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.user_list_row, viewGroup, false));

    }

    @Override
    public void onBindViewHolder(@NonNull UserListAdapter.UserListViewHolder userListViewHolder, int i) {
        User user = users.get(i);

        userListViewHolder.name.setText(user.getName());
        userListViewHolder.surname.setText(user.getSurname());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class UserListViewHolder extends RecyclerView.ViewHolder {
        TextView name, surname;
        ImageView profile;

        UserListViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            surname = itemView.findViewById(R.id.surname);
            profile = itemView.findViewById(R.id.imageProfile);
        }
    }
}
