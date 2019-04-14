package wat.projectsi.client.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class UsersActivity extends AppCompatActivity {

    ListView peopleListView;
    List<String> listOfUser;
    ArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        peopleListView = findViewById(R.id.list_of_people);

        showPeople();
        peopleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                clickOnUser();
            }
        });

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
                listOfUser.add(response.toString());
                adapter = new ArrayAdapter <>
                        (UsersActivity.this, android.R.layout.simple_list_item_1, listOfUser);
                peopleListView.setAdapter(adapter);
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
