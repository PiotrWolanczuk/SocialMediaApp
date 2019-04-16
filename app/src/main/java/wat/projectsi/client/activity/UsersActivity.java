package wat.projectsi.client.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import wat.projectsi.R;
import wat.projectsi.client.ConnectingURL;
import wat.projectsi.client.User;
import wat.projectsi.client.UserAdapter;

public class UsersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<User> userList = new ArrayList<>();
    private ArrayList<Object> listOfUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new UserAdapter(userList);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
//        showPeople();
userList.add(new  User("Piotr", "Wolanczk"));
userList.add(new  User("zbych", "Pawlowski"));
userList.add(new  User("Yxq", "Xyz"));

    }

    private void clickOnUser() {

    }

    private void showPeople() {
        listOfUser = new ArrayList<>();
        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest MyJsonRequest = new JsonObjectRequest( Request.Method.GET,
                ConnectingURL.URL_Users,null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                System.out.println("dupa");
                System.out.println(response.toString());
//                adapter = new ArrayAdapter <>
//                        (UsersActivity.this, android.R.layout.simple_list_item_1, listOfUser);
//                peopleListView.setAdapter(adapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UsersActivity.this, "Something is wrong.", Toast.LENGTH_SHORT).show();
            }
        });
        MyRequestQueue.add(MyJsonRequest);
    }
}
