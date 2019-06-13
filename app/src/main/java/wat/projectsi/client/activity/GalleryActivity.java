package wat.projectsi.client.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import java.util.Map;

import wat.projectsi.R;
import wat.projectsi.client.ConnectingURL;
import wat.projectsi.client.Misc;
import wat.projectsi.client.UniversalImageLoader;

public class GalleryActivity extends AppCompatActivity {

    private Long currentUserId;
    private RequestQueue requestQueue;
    private String[] images;
    private UniversalImageLoader adapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        requestQueue = Volley.newRequestQueue(this);
        currentUserId = getIntent().getExtras().getLong("userId");

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);

        viewPager = findViewById(R.id.viewPager);
        takePicturesFromDB();
    }

    private void takePicturesFromDB() {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,
                ConnectingURL.URL_Pictures + "/" + currentUserId, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                Log.e("APIResponse", response.toString());
                showPhotos(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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

    private void showPhotos(JSONArray response) {
        images = new String[response.length()];
        JSONObject picture = null;
        if (response.length() > 1)
            try {
                for (int i = 1; i < response.length(); i++) {
                    picture = response.getJSONObject(i);
                    images[i - 1] = picture.getString("hashCode");
                }
                adapter = new UniversalImageLoader(GalleryActivity.this, ImageLoader.getInstance(), images);
                viewPager.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
    }
}
