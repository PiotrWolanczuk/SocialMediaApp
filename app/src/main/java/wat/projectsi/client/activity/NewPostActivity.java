package wat.projectsi.client.activity;

import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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
import wat.projectsi.client.adapter.GalleryAdapter;
import wat.projectsi.client.SharedOurPreferences;
import wat.projectsi.client.VolleyJsonRequest;

public class NewPostActivity extends AppCompatActivity {

    Button sendButton;
    Button addPhoto;
    EditText newPostText;

    private static ArrayList<Integer> images = new ArrayList<>();
    private static int RESULT_LOAD_IMAGE = 1;
    private static ArrayList<String> hashcodes = new ArrayList<>();
    String messageText;
    private GalleryAdapter galleryAdapter;
    private GridView gvGallery;
    String imageEncoded;
    List<String> imagesEncodedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        sendButton = findViewById(R.id.send_button);
        addPhoto = findViewById(R.id.add_photo_button);
        newPostText = findViewById(R.id.message_text);
        gvGallery = findViewById(R.id.gv);
    }

    public void send_new_post(View view) {
        if (hashcodes.size() == 0)
            hashcodes.add("{}");
        messageText = String.valueOf(newPostText.getText());
        if (messageText.isEmpty())
            consumeNewPostAPI("{}", hashcodes);
        else
            consumeNewPostAPI(messageText, hashcodes);
    }

    public void add_photo_from_gallery(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                imagesEncodedList = new ArrayList<>();
                if(data.getData()!=null){

                    Uri mImageUri=data.getData();

                    images.add(mImageUri.hashCode());
                    hashcodes.add(Integer.toString(images.get(images.size() - 1)));

                    Cursor cursor = getContentResolver().query(mImageUri,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imageEncoded  = cursor.getString(columnIndex);
                    cursor.close();

                    ArrayList<Uri> mArrayUri = new ArrayList<>();
                    mArrayUri.add(mImageUri);
                    galleryAdapter = new GalleryAdapter(getApplicationContext(),mArrayUri);
                    gvGallery.setAdapter(galleryAdapter);
                    gvGallery.setVerticalSpacing(gvGallery.getHorizontalSpacing());
                    ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gvGallery
                            .getLayoutParams();
                    mlp.setMargins(0, gvGallery.getHorizontalSpacing(), 0, 0);

                } else {
                    if (data.getClipData() != null) {
                        ClipData mClipData = data.getClipData();
                        ArrayList<Uri> mArrayUri = new ArrayList<>();
                        for (int i = 0; i < mClipData.getItemCount(); i++) {

                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();

                            //images.add(uri.hashCode());
                            hashcodes.add(Integer.toString(uri.hashCode()));

                            mArrayUri.add(uri);
                            Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                            cursor.moveToFirst();

                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            imageEncoded  = cursor.getString(columnIndex);
                            imagesEncodedList.add(imageEncoded);
                            cursor.close();

                            galleryAdapter = new GalleryAdapter(getApplicationContext(),mArrayUri);
                            gvGallery.setAdapter(galleryAdapter);
                            gvGallery.setVerticalSpacing(gvGallery.getHorizontalSpacing());
                            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gvGallery
                                    .getLayoutParams();
                            mlp.setMargins(0, gvGallery.getHorizontalSpacing(), 0, 0);
                        }
                    }
                }
            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void consumeNewPostAPI(final String text, final ArrayList imageHashcodes) {
        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);

        final JSONObject jsonRequest = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONObject listElement = new JSONObject();
        try {
            for (int i = 0; i < imageHashcodes.size(); i++) {
                System.out.println(imageHashcodes.get(i));
                listElement.put("hashCode", imageHashcodes.get(i));
                jsonArray.put(listElement);
            }

            jsonRequest.put("pictureEntityCollection", jsonArray);
            jsonRequest.put("postContent", text);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        VolleyJsonRequest MyJsonRequest = new VolleyJsonRequest(Request.Method.POST,
                ConnectingURL.URL_Posts, jsonRequest, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                System.out.println(jsonRequest);
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("APIResponse", error.toString());
                error.printStackTrace();
                Toast.makeText(NewPostActivity.this,
                        getApplicationContext().getResources().getString(R.string.message_wrong),
                        Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " +
                        SharedOurPreferences.getDefaults("token", NewPostActivity.this));
                return headers;
            }
        };

        MyRequestQueue.add(MyJsonRequest);
    }
}
