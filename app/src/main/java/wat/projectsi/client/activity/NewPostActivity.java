package wat.projectsi.client.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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

public class NewPostActivity extends AppCompatActivity {

    Button sendButton;
    Button addPhoto;
    EditText newPostText;
    ImageView imageView;

    private static int RESULT_LOAD_IMAGE = 1;
    private ArrayList<String> hashcodes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        sendButton = findViewById(R.id.send_button);
        addPhoto = findViewById(R.id.add_photo_button);
        newPostText = findViewById(R.id.message_text);


    }

    public void send_new_post(View view) {
        consumeNewPostAPI(String.valueOf(newPostText.getText()), hashcodes);
    }

    public void add_photo_from_gallery(View view) {
        imageView = findViewById(R.id.new_photo);
        Intent gallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(gallery, RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            imageView.setImageURI(selectedImage);
            hashcodes.add(Integer.toString(imageView.hashCode()));
        }
    }

    private void consumeNewPostAPI(final String text, final ArrayList imageHashcodes) {
        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);

        JSONObject jsonRequest = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONObject listElement = new JSONObject();
        try {
            for(int i =0; i < imageHashcodes.size(); i++){
                listElement.put("hashCode", imageHashcodes.get(i));
            }
            jsonArray.put(listElement);
            jsonRequest.put("pictureEntityCollection",  jsonArray);
            jsonRequest.put("postContent",  text);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest MyJsonRequest = new JsonObjectRequest( Request.Method.POST,
                ConnectingURL.URL_AddPost,jsonRequest, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response.toString());
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("APIResponse", error.toString());
                Toast.makeText(NewPostActivity.this,
                        getApplicationContext().getResources().getString(R.string.message_wrong),
                        Toast.LENGTH_LONG).show();
            }
        });
        MyRequestQueue.add(MyJsonRequest);
    }
}
