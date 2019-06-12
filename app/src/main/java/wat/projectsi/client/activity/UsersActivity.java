package wat.projectsi.client.activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wat.projectsi.R;
import wat.projectsi.client.ConnectingURL;
import wat.projectsi.client.Misc;
import wat.projectsi.client.SharedOurPreferences;
import wat.projectsi.client.adapter.FriendAdapter;
import wat.projectsi.client.adapter.UserListAdapter;
import wat.projectsi.client.model.User;

public class UsersActivity extends BasicActivity {

    private RecyclerView recyclerView;
    private TextView emptyView;
    private List<User> userList = new ArrayList<>();

    private UserListAdapter mUserListAdapter;
    private FriendAdapter mFriendListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        Bundle bundle = getIntent().getExtras();
        String whichGroupShows = bundle.getString("people");

        recyclerView = findViewById(R.id.my_recycler_view);
        emptyView = findViewById(R.id.empty_view);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);

        if(whichGroupShows.equals("users")){
            String name = bundle.getString("name");
            String surname = bundle.getString("surname");
            showPeople(name, surname);
        }
        else if(whichGroupShows.equals("friends"))
            showFriends();
    }

    private void showPeople(String name, String surname) {
        progressDialog.setMessage(getResources().getString(R.string.message_progress));
        progressDialog.show();


        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,
                ConnectingURL.URL_Users_ByName + "?name="+name+"&surname="+surname, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                Log.e("APIResponse", response.toString());
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        User user = new User(
                                jsonObject.getString("firstName"),
                                jsonObject.getString("lastName"),
                                jsonObject.getInt("userId"),
                                jsonObject.getJSONObject("pictureId").getString("hashCode"));

                        userList.add(user);
                    }

                    progressDialog.dismiss();
                    recyclerView.setAdapter(mUserListAdapter = new UserListAdapter(userList, UsersActivity.this));
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(UsersActivity.this, getResources().getString(R.string.message_wrong), Toast.LENGTH_SHORT).show();
                Log.e("APIResponse", error.toString());
                System.out.println(error.getMessage());
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() {
                return Misc.getSecureHeaders(getApplicationContext());
            }
        }
        ;

        requestQueue.add(request);
    }
    private void showFriends( ) {
        progressDialog.setMessage(getResources().getString(R.string.message_progress));
        progressDialog.show();

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,
                ConnectingURL.URL_Acquaintances, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        if(response.getJSONObject(i).getString("state").equals("APPROVED")){
                            JSONObject friend = jsonObject.getJSONObject("friend");
                            User user = new User(
                                    friend.getString("firstName"),
                                    friend.getString("lastName"),
                                    friend.getInt("userId"),
                                    friend.getJSONObject("pictureId").getString("hashCode"));

                            userList.add(user);
                        }
                    }

                    progressDialog.dismiss();
                    recyclerView.setAdapter(mFriendListAdapter = new FriendAdapter(userList, UsersActivity.this));
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(UsersActivity.this, getResources().getString(R.string.message_wrong), Toast.LENGTH_SHORT).show();
                Log.e("APIResponse", error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                return Misc.getSecureHeaders(getApplicationContext());
            }
        };

        requestQueue.add(request);
    }
}
