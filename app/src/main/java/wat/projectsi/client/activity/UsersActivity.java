package wat.projectsi.client.activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import wat.projectsi.R;
import wat.projectsi.client.ConnectingURL;
import wat.projectsi.client.GsonRequest;
import wat.projectsi.client.adapter.UserListAdapter;
import wat.projectsi.client.model.User;
import wat.projectsi.client.adapter.UserAdapter;

public class UsersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<User> userList = new ArrayList<>();
    private ArrayList<Object> listOfUser;

    private UserListAdapter mUserListAdapter;
    private List<User> mUserList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

//        mAdapter = new UserAdapter(userList);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setAdapter(mAdapter);
        showPeople();
    }

    private void showPeople(){
        mUserList = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization","Bearer "+ getSharedPreferences("MyPreferences", Activity.MODE_PRIVATE).getString("token",""));

        GsonRequest<User[]> request = new GsonRequest<>( ConnectingURL.URL_Users, User[].class, headers, new Response.Listener<User[]>() {
            @Override
            public void onResponse(User[] response) {
                mUserList.addAll(Arrays.asList(response));
                recyclerView.setAdapter(mUserListAdapter = new UserListAdapter(mUserList, UsersActivity.this) );
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("APIResponse", error.toString());
                error.printStackTrace();
            }
        });
        requestQueue.add(request);

    }
}
