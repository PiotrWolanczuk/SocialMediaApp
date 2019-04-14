package wat.projectsi.client.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import wat.projectsi.R;
import wat.projectsi.client.ConnectingURL;

public class NotificationActivity extends AppCompatActivity {

    ListView notificationListView;
    ArrayAdapter<String> adapter;
    List<String> listOfNotifications;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        notificationListView = findViewById(R.id.notification_list);

        showNotifications();
    }

    private void showNotifications() {
        listOfNotifications = new ArrayList<>();
        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest MyJsonRequest = new JsonObjectRequest( Request.Method.GET,
                ConnectingURL.URL_Notifications,null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                if(response.toString().isEmpty())
                    listOfNotifications.add(String.valueOf(R.string.empty));
                else
                    listOfNotifications.add(response.toString());
                adapter = new ArrayAdapter<>(NotificationActivity.this,
                        android.R.layout.simple_list_item_1, listOfNotifications);
                notificationListView.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(NotificationActivity.this, "Something is wrong.",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        MyRequestQueue.add(MyJsonRequest);
    }
}
