package wat.projectsi.client.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import wat.projectsi.R;
import wat.projectsi.client.ConnectingURL;
import wat.projectsi.client.Misc;
import wat.projectsi.client.model.Comment;
import wat.projectsi.client.model.Post;
import wat.projectsi.client.model.Profile;
import wat.projectsi.client.request.GsonRequest;

public abstract class BasicActivity extends BaseSettingChangeActivity {
    protected final Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            if (error instanceof NetworkError || error instanceof TimeoutError || error == null || error.networkResponse == null) {
                Log.e("NetworkError", error.toString());
                Toast.makeText(getApplicationContext(), R.string.error_no_network_available, Toast.LENGTH_LONG).show();
                return;
            }

            Log.e("APIResponse", error.toString());
            System.out.println("Kod " + error.networkResponse.statusCode);

            switch (error.networkResponse.statusCode) {
//                case 200://"OK"
//                case 201://"Created"
//                    break;
//                case 400://"Bad Request"
//                    break;
//                case 401://"Unauthorized"
//                    break;
//                case 403://"Forbidden"
//                    break;
//                case 404://"Not Found"
//                    break;
//                case 405: //"Method Not Allowed"
//                    break;
//                case 415://"Unsupported Media Type" ->BadJason
//                    break;
//                case 500://"Fail! -> Cause: User Role not find."
//                    break;
                default:
                    progressDialog.dismiss();
                    Toast.makeText(BasicActivity.this, getString(R.string.error_unknown), Toast.LENGTH_SHORT).show();
            }
        }
    };

    protected ProgressDialog progressDialog;
    protected RequestQueue requestQueue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestQueue = Volley.newRequestQueue(this);
        progressDialog = new ProgressDialog(this);
    }

    public void showCurrentProfile(View view) {
        progressDialog.setMessage(getResources().getString(R.string.message_progress));
        progressDialog.show();

        GsonRequest<Profile> request = new GsonRequest<>(ConnectingURL.URL_Users_Profile, Profile.class,
                Misc.getSecureHeaders(this), new Response.Listener<Profile>() {
            @Override
            public void onResponse(Profile response) {
                Intent intent= new Intent(BasicActivity.this, ProfileActivity.class);

                intent.putExtra("profile", response);
                progressDialog.dismiss();

                startActivity(intent);
            }
        }, errorListener);

        request.setRetryPolicy(new DefaultRetryPolicy(
                Misc.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(request);
    }

    public void showProfile(View view) {
        showProfile((long)view.getTag());
    }

    public void showProfile(long userID)
    {
        progressDialog.setMessage(getResources().getString(R.string.message_progress));
        progressDialog.show();

        GsonRequest<Profile> request = new GsonRequest<>(ConnectingURL.URL_Users_Profile+"/"+userID, Profile.class,
                Misc.getSecureHeaders(this), new Response.Listener<Profile>() {
            @Override
            public void onResponse(Profile response) {
                progressDialog.dismiss();
                if(response!=null) {
                    Intent intent = new Intent(BasicActivity.this, ProfileActivity.class);

                    intent.putExtra("profile", response);
                    startActivity(intent);
                }
            }
        }, errorListener);

        request.setRetryPolicy(new DefaultRetryPolicy(
                Misc.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(request);
    }
}
