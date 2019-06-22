package wat.projectsi.client.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wat.projectsi.R;
import wat.projectsi.client.ConnectingURL;
import wat.projectsi.client.Misc;
import wat.projectsi.client.SharedOurPreferences;
import wat.projectsi.client.model.Image;
import wat.projectsi.client.request.VolleyJsonRequest;
import wat.projectsi.client.request.VolleyMultipartRequest;

public class NewPostActivity extends BaseSettingChangeActivity {

    Button sendButton;
    Button addPhoto;
    Button createPhoto;
    EditText newPostText;
    ImageView postImage;

    private static int RESULT_LOAD_IMAGE = 1;
    private static ArrayList<Bitmap> bitmaps = new ArrayList<>();
    String messageText;
    private final List<String> imagesEncodedList = new ArrayList<>();
    private final int REQUEST_TAKE_PHOTO = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        sendButton = findViewById(R.id.send_button);
        addPhoto = findViewById(R.id.add_photo_button);
        newPostText = findViewById(R.id.message_text);
        postImage = findViewById(R.id.postImage);
        createPhoto = findViewById(R.id.create_photo_button);

        createPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPhoto();
            }
        });
    }

    public void send_new_post(View view) {
        messageText = newPostText.getText().toString();

        if (imagesEncodedList.size() != 0) {
            if (messageText.isEmpty())
                consumeNewPostAPI("", imagesEncodedList.get(0));
            else
                consumeNewPostAPI(messageText, imagesEncodedList.get(0));
        } else {
            if (messageText.isEmpty())
                Toast.makeText(this, R.string.message_wrong, Toast.LENGTH_LONG).show();
            else
                consumeNewPostAPI(messageText, "");
        }
    }

    public void add_photo_from_gallery(View view) {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            postImage.setImageBitmap(imageBitmap);
            uploadBitmap(imageBitmap);
        }
        try {
            if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
                    && null != data) {

                if (data.getData() != null) {

                    Uri mImageUri = data.getData();

                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mImageUri);
                        bitmaps.add(bitmap);
                        postImage.setImageBitmap(bitmap);
                        uploadBitmap(bitmaps.get(0));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                Toast.makeText(this, "You haven't picked image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }
    }

    public void createPhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 1, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void consumeNewPostAPI(final String text, final String imageBitmapHashcode) {
        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);

        final JSONObject jsonRequest = new JSONObject();
        final JSONObject jsonObject = new JSONObject();
        final JSONArray jsonArray= new JSONArray();
        try {
            jsonObject.put("hashCode",  imageBitmapHashcode);
            jsonArray.put(jsonObject);
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
                return Misc.getSecureHeaders(NewPostActivity.this);
            }
        };

        MyRequestQueue.add(MyJsonRequest);
    }

    private void uploadBitmap(final Bitmap bitmap) {
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, ConnectingURL.URL_SEND_Picture,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        StringBuilder res = new StringBuilder();

                        for (byte c : response.data) {
                            res.append((char) c);
                        }

                        Gson gson = new Gson();
                        Image image = Response.success(gson.fromJson(res.toString(), Image.class), HttpHeaderParser.parseCacheHeaders(response)).result;
                        imagesEncodedList.add(image.getUrl());
                        System.out.println(imagesEncodedList.get(0));
                        Toast.makeText(NewPostActivity.this, getString(R.string.done), Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(NewPostActivity.this, getString(R.string.message_wrong), Toast.LENGTH_SHORT).show();
                        Log.e("APIResponse", error.toString());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + SharedOurPreferences.getDefaults("token", NewPostActivity.this));
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

            /*
             * Here we are passing image by renaming it with a unique name
             * */
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("file", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                Misc.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }
}
