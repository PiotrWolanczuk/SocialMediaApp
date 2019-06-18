package wat.projectsi.client.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import wat.projectsi.R;
import wat.projectsi.client.ConnectingURL;
import wat.projectsi.client.Misc;
import wat.projectsi.client.adapter.ViolationAdapter;
import wat.projectsi.client.model.Violation;

public class ViolationActivity extends BasicActivity {

    private RecyclerView recyclerView;
    private TextView emptyView;
    private List<Violation> violationsList = new ArrayList<>();

    private ViolationAdapter violationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_violation);

        recyclerView = findViewById(R.id.my_recycler_view_violations);
        emptyView = findViewById(R.id.empty_view);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);

        showViolationsPosts();
        showViolationsComments();
    }

    private void showViolationsPosts() {
        progressDialog.setMessage(getResources().getString(R.string.message_progress));
        progressDialog.show();

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,
                ConnectingURL.URL_ViolationsPosts, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        Log.e("APIResponse", jsonObject.toString());
                        Violation violation = new Violation(jsonObject.getJSONObject("post").getString("postContent"),
                                                            jsonObject.getString("violationDescription"));

                        violationsList.add(violation);
                    }

                    progressDialog.dismiss();
                    recyclerView.setAdapter(violationAdapter = new ViolationAdapter(violationsList, ViolationActivity.this));
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
                Toast.makeText(ViolationActivity.this, getResources().getString(R.string.message_wrong), Toast.LENGTH_SHORT).show();
                Log.e("APIResponse", error.toString());
                System.out.println(error.getMessage());
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() {
                return Misc.getSecureHeaders(getApplicationContext());
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                Misc.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
    }
    
    private void showViolationsComments() {

    }
}
