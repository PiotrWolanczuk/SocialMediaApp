package wat.projectsi.client.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wat.projectsi.R;
import wat.projectsi.client.ConnectingURL;
import wat.projectsi.client.Picture;
import wat.projectsi.client.SharedOurPreferences;
import wat.projectsi.client.activity.BasicActivity;
import wat.projectsi.client.activity.MainActivity;
import wat.projectsi.client.activity.UsersActivity;
import wat.projectsi.client.model.User;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserListViewHolder>{
    private List<User> users;
    private Context context;
    public UserListAdapter(List<User> users, Context context) {
        this.users = users;
        this.context =  context;
    }

    @NonNull
    @Override
    public UserListAdapter.UserListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new UserListAdapter.UserListViewHolder(LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.user_list, viewGroup, false));

    }

    @Override
    public void onBindViewHolder(@NonNull UserListAdapter.UserListViewHolder userListViewHolder, int i) {
        final User user = users.get(i);

        userListViewHolder.name.setText(user.getName());
        userListViewHolder.surname.setText(user.getSurname());
        new Picture(userListViewHolder.profile).execute(user.getImage().getUrl());
        userListViewHolder.profile.setTag(user.getId());
        userListViewHolder.name.setTag(user.getId());
        userListViewHolder.surname.setTag(user.getId());

        userListViewHolder.oneUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUserDialog(context, user);
            }
        });
    }

    private void showUserDialog(final Context context, final User user) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);

        dialog.setPositiveButton(R.string.invite, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sendInvitation(context, user);
                dialog.dismiss();
            }
        });

        dialog.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setNegativeButton(R.string.prompt_view_profile, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(context instanceof BasicActivity)
                    ((BasicActivity)context).showProfile(user.getId());
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void sendInvitation(final Context context, User user) {

        if(user.getId()== MainActivity.getCurrentUser().getId())return;

        RequestQueue MyRequestQueue = Volley.newRequestQueue(context);

        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("userEntityId", user.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest MyJsonRequest = new JsonObjectRequest( Request.Method.POST,
                ConnectingURL.URL_Acquaintances + "/" + user.getId(),jsonRequest, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(context, context.getResources().getString(R.string.inviteSend), Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("APIResponse", error.toString());
                Toast.makeText(context, context.getResources().getString(R.string.message_wrong), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + SharedOurPreferences.getDefaults("token", context));
                return headers;
            }
        };
        MyRequestQueue.add(MyJsonRequest);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class UserListViewHolder extends RecyclerView.ViewHolder {
        TextView name, surname;
        ImageView profile;
        LinearLayout oneUser;

        UserListViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.profileName);
            surname = itemView.findViewById(R.id.profileSurname);
            profile = itemView.findViewById(R.id.profilePicture);
            oneUser = itemView.findViewById(R.id.row);
        }
    }
}
