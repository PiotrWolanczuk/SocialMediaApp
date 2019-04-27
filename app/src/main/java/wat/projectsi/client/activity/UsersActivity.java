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
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wat.projectsi.R;
import wat.projectsi.client.ConnectingURL;
import wat.projectsi.client.SharedOurPreferences;
import wat.projectsi.client.adapter.UserListAdapter;
import wat.projectsi.client.model.User;

public class UsersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView emptyView;
    private List<User> userList = new ArrayList<>();

    private UserListAdapter mUserListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        recyclerView = findViewById(R.id.my_recycler_view);
        emptyView = findViewById(R.id.empty_view);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);

        Bundle name = getIntent().getExtras();
        String userName = name.getString("name");


        showPeople(userName);
    }

    private void showPeople(String userName) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.message_progress));
        progressDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,
                ConnectingURL.URL_Users + "?name="+userName, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {

                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        User user = new User(
                                jsonObject.getString("name"),
                                jsonObject.getString("surname"));
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
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + SharedOurPreferences.getDefaults("token", UsersActivity.this));
                return headers;
            }
        };

        requestQueue.add(request);

    }
}
