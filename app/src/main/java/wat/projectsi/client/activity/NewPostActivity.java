package wat.projectsi.client.activity;

import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wat.projectsi.R;
import wat.projectsi.client.ConnectingURL;
import wat.projectsi.client.Misc;
import wat.projectsi.client.SharedOurPreferences;
import wat.projectsi.client.adapter.GalleryAdapter;
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
//    private GridView gvGallery;
    String imageEncoded;
    private final List<String> imagesEncodedList = new ArrayList<>();
    private final int REQUEST_TAKE_PHOTO = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        sendButton = findViewById(R.id.send_button);
        addPhoto = findViewById(R.id.add_photo_button);
        newPostText = findViewById(R.id.message_text);
//        gvGallery = findViewById(R.id.gv);
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
        messageText = String.valueOf(newPostText.getText());

        if (bitmaps.size() != 0){
            if (messageText.isEmpty())
                consumeNewPostAPI("{}", imagesEncodedList.get(0));
            else
                consumeNewPostAPI(messageText, imagesEncodedList.get(0));
        }else{
            if (messageText.isEmpty())
                Toast.makeText(this, R.string.message_wrong, Toast.LENGTH_LONG).show();
            else
                consumeNewPostAPI(messageText, null);
        }
    }

    public void add_photo_from_gallery(View view) {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent,"Select Picture"), RESULT_LOAD_IMAGE);
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK ){
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            postImage.setImageBitmap(imageBitmap);
        }
        try {
            if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

//                String[] filePathColumn = { MediaStore.Images.Media.DATA };
//                imagesEncodedList = new ArrayList<>();
//                GalleryAdapter galleryAdapter;
                if(data.getData()!=null){

                    Uri mImageUri=data.getData();

//                    Cursor cursor = getContentResolver().query(mImageUri,
//                            filePathColumn, null, null, null);
//                    cursor.moveToFirst();

                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mImageUri);
                        bitmaps.add(bitmap);
                        postImage.setImageBitmap(bitmap);
                        uploadBitmap(bitmaps.get(0));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

//                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                    imageEncoded  = cursor.getString(columnIndex);
//                    cursor.close();
//
//                    ArrayList<Uri> mArrayUri = new ArrayList<>();
//                    mArrayUri.add(mImageUri);
//                    galleryAdapter = new GalleryAdapter(getApplicationContext(),mArrayUri);

//                    gvGallery.setAdapter(galleryAdapter);
//                    gvGallery.setVerticalSpacing(gvGallery.getHorizontalSpacing());
//                    ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gvGallery
//                            .getLayoutParams();
//                    mlp.setMargins(0, gvGallery.getHorizontalSpacing(), 0, 0);

                }
//                else {
//                    if (data.getClipData() != null) {
//                        ClipData mClipData = data.getClipData();
//                        ArrayList<Uri> mArrayUri = new ArrayList<>();
//                        for (int i = 0; i < mClipData.getItemCount(); i++) {
//
//                            ClipData.Item item = mClipData.getItemAt(i);
//                            Uri uri = item.getUri();
//
//                            mArrayUri.add(uri);
//                            Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
//                            cursor.moveToFirst();
//
//                            try {
//                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
//                                bitmaps.add(bitmap);
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//
//                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                            imageEncoded  = cursor.getString(columnIndex);
//                            imagesEncodedList.add(imageEncoded);
//                            cursor.close();
//
//                            galleryAdapter = new GalleryAdapter(getApplicationContext(),mArrayUri);
//                            gvGallery.setAdapter(galleryAdapter);
//                            gvGallery.setVerticalSpacing(gvGallery.getHorizontalSpacing());
//                            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gvGallery
//                                    .getLayoutParams();
//                            mlp.setMargins(0, gvGallery.getHorizontalSpacing(), 0, 0);
//                        }
//                    }
//                }
            } else {
                Toast.makeText(this, "You haven't picked image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }
    }

    public void createPhoto(){
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
        final JSONArray jsonArray = new JSONArray();
        final JSONObject jsonElement = new JSONObject();
        try {
            if(imageBitmapHashcode != null){
                    jsonElement.put("picture", imageBitmapHashcode);
                    jsonArray.put(jsonElement);
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
//                        try {
                            System.out.println(response.toString());
                            System.out.println(response.data.toString());
//                            imagesEncodedList.add(response.getJSONObject()getString("hashCode"));
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }

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
            public Map<String,String> getHeaders(){
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization","Bearer "+ SharedOurPreferences.getDefaults("token",NewPostActivity.this));
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
