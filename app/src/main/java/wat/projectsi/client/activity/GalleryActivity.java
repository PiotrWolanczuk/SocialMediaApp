package wat.projectsi.client.activity;

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
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import wat.projectsi.R;
import wat.projectsi.client.ConnectingURL;
import wat.projectsi.client.Misc;
import wat.projectsi.client.adapter.PictureAdapter;
import wat.projectsi.client.model.Image;

public class GalleryActivity extends BasicActivity {

    private Long currentUserId;
    private RequestQueue requestQueue;

    private List<Image> imageList = new ArrayList<>();
    private PictureAdapter pictureAdapter;
    private RecyclerView recyclerView;
    private TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        requestQueue = Volley.newRequestQueue(this);
        currentUserId = getIntent().getExtras().getLong("userId");

        recyclerView = findViewById(R.id.my_recycler_view);
        emptyView = findViewById(R.id.empty_view);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);
        takePicturesFromDB();
    }

    private void takePicturesFromDB() {
        progressDialog.setMessage(getResources().getString(R.string.message_progress));
        progressDialog.show();
        final JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,
                ConnectingURL.URL_Pictures + "/" + currentUserId, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                progressDialog.dismiss();
                recyclerView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
                Log.e("APIResponse", response.toString());
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        Image image = new Image(jsonObject.getLong("pictureId"), jsonObject.getString("hashCode"));
                        imageList.add(image);
                    }
                    recyclerView.setAdapter(pictureAdapter = new PictureAdapter(imageList, GalleryActivity.this));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(GalleryActivity.this, getResources().getString(R.string.message_wrong), Toast.LENGTH_SHORT).show();
                Log.e("APIResponse", error.toString());
                System.out.println(error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                return Misc.getSecureHeaders(GalleryActivity.this);
            }
        };
        requestQueue.add(request);
    }
}
