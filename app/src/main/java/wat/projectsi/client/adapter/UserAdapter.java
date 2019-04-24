package wat.projectsi.client.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import wat.projectsi.R;
import wat.projectsi.client.model.User;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {

    private User user;

    public UserAdapter(User user) {
        this.user = user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, surname;
        ImageView profile;

        MyViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.name);
            surname = v.findViewById(R.id.surname);
            profile = v.findViewById(R.id.imageProfile);
        }
    }


    @Override
    public UserAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.user_list_row, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
       holder.name.setText(user.getName());
       holder.surname.setText(user.getSurname());
       holder.profile.setImageBitmap(user.getProfileImage());
    }

    @Override
    public int getItemCount() { return 0; }
}
