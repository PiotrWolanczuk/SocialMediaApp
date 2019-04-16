package wat.projectsi.client;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import wat.projectsi.R;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {
    private List<User> userList;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, surname;
        public ImageView profile;

        public MyViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.name);
            surname = v.findViewById(R.id.surname);
            profile = v.findViewById(R.id.imageProfile);
        }
    }

    public UserAdapter(List<User> userList) {
        this.userList = userList;
    }

    @Override
    public UserAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.user_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
       User user = userList.get(position);
       holder.name.setText(user.getName());
       holder.surname.setText(user.getSurname());
       //holder.profile.setIma
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}
