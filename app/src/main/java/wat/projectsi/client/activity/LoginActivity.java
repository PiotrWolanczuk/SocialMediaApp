package wat.projectsi.client.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import wat.projectsi.R;
import wat.projectsi.client.ConnectingURL;

public class LoginActivity extends AppCompatActivity {

    private static final String PREFERENCES_NAME = "myPreferences";

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private ProgressDialog progressDialog;

    private SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preferences = getSharedPreferences(PREFERENCES_NAME, Activity.MODE_PRIVATE);

        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        progressDialog = new ProgressDialog(this);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
            attemptLogin();
            }
        });
    }

    private void attemptLogin() {
        progressDialog.setMessage("Siging in...");
        progressDialog.show();
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);
        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(password)){
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
            progressDialog.cancel();
        } else {
            consumeAPI(email, password);
        }
    }

    private void consumeAPI(final String email, final String password) {
        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);

        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("login",  email);
            jsonRequest.put("password",  password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest MyJsonRequest = new JsonObjectRequest( Request.Method.POST,ConnectingURL.URL_Signin,jsonRequest, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                progressDialog.cancel();
                try {
                    saveToken(response.getString("token"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(mainIntent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.cancel();
                Log.e("APIResponse", error.toString());
                System.out.println(error.toString());
                Toast.makeText(LoginActivity.this, "Something is wrong.", Toast.LENGTH_SHORT).show();
            }
        });
        MyRequestQueue.add(MyJsonRequest);
    }

    private void saveToken(String token){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("token", token);
        editor.commit();
    }


    public void setUpNewPassword(View view) {
    }

    public void setUpNewAccount(View view) {
        Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(registerIntent);
    }

}

