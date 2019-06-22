package wat.projectsi.client.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Map;

import wat.projectsi.R;
import wat.projectsi.client.ConnectingURL;
import wat.projectsi.client.DateFormatter;
import wat.projectsi.client.Misc;
import wat.projectsi.client.SharedOurPreferences;
import wat.projectsi.client.Validator;
import wat.projectsi.client.model.User;
import wat.projectsi.client.request.GsonRequest;
import wat.projectsi.client.request.VolleyJsonRequest;

public class ProfileEditActivity extends BasicActivity {

    private EditText mNameView;
    private EditText mSurnameView;
    private TextView mDateView;
    private Switch mGenderView;

    private Calendar c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        mGenderView = findViewById(R.id.genderSwitch);

        mNameView = findViewById(R.id.name);
        mSurnameView = findViewById(R.id.surname);
        mDateView = findViewById(R.id.birthdayNew);

        mGenderView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mGenderView.setText(R.string.prompt_gender_man);
                } else {
                    mGenderView.setText(R.string.prompt_gender_woman);
                }
            }
        });

        mGenderView.setChecked(currentUser.getGender().equals(Misc.womanStr));

        mNameView.setText(currentUser.getName());
        mSurnameView.setText(currentUser.getSurname());

        c = Calendar.getInstance(getResources().getConfiguration().locale);

        mDateView.setText(DateFormatter.convertToLocalDate(currentUser.getBirthday()));
    }

    public void updateProfile(View view) {
        progressDialog.setMessage(getString(R.string.action_updating));
        progressDialog.show();

        mNameView.setError(null);
        mSurnameView.setError(null);

        String name = mNameView.getText().toString();
        String surname = mSurnameView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!Validator.isNameValid(name)) {
            mNameView.setError(getString(R.string.error_invalid_characters));
            focusView = mNameView;
            cancel = true;
        }
        if (!Validator.isNameValid(surname)) {
            mSurnameView.setError(getString(R.string.error_invalid_characters));
            focusView = mSurnameView;
            cancel = true;
        }

        if (!Validator.isNameValid(surname)) {
            mSurnameView.setError(getString(R.string.error_invalid_characters));
            focusView = mSurnameView;
            cancel = true;
        }

        if (cancel) {
            progressDialog.hide();
            focusView.requestFocus();
        } else {
            progressDialog.show();

            updateRequest(mNameView.getText().toString(), mSurnameView.getText().toString(),
                    DateFormatter.convertToApi(mDateView.getText().toString()),
                    mGenderView.isChecked());
        }
    }

    private void updateRequest(final String name, final String surname, final String birthDate, boolean isMan) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JSONObject data = new JSONObject();
        try {
            data.put("firstName", name);
            data.put("lastName", surname);
            data.put("birthday", birthDate);
            data.put("gender", isMan ? Misc.manStr : Misc.womanStr);
            data.put("userId", currentUser.getId());
            data.put("profilePictureId", currentUser.getImage().getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        VolleyJsonRequest request = new VolleyJsonRequest(Request.Method.PUT, ConnectingURL.URL_Users, data, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
               progressDialog.cancel();
                Toast.makeText(ProfileEditActivity.this, R.string.done, Toast.LENGTH_SHORT).show();
                requestCurrentUser();
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.cancel();
                Log.e("APIResponse", error.toString());
                error.printStackTrace();
                Toast.makeText(ProfileEditActivity.this, R.string.message_wrong, Toast.LENGTH_LONG).show();
            }
        }){
        @Override
            public Map<String, String> getHeaders() {
                return Misc.getSecureHeaders(ProfileEditActivity.this);
            }
        };
        requestQueue.add(request);
    }

    public void showDatePickerDialog(View view) {
        DatePickerDialog dpd = new DatePickerDialog(ProfileEditActivity.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        c.set(Calendar.YEAR, year);
                        c.set(Calendar.MONTH, monthOfYear);
                        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        mDateView.setText(DateFormatter.convertToLocalDate(c.getTime()));
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        dpd.getDatePicker().setMinDate(DateFormatter.minDate);
        dpd.getDatePicker().setMaxDate(DateFormatter.maxDate);
        dpd.show();
    }

    private void requestCurrentUser(){
        GsonRequest<User> request = new GsonRequest<>(ConnectingURL.URL_Users_Current, User.class,
                Misc.getSecureHeaders(this), new Response.Listener<User>() {
            @Override
            public void onResponse(User response) {
                currentUser=response;

                mGenderView.setChecked(currentUser.getGender().equals(Misc.womanStr));
                mNameView.setText(currentUser.getName());
                mSurnameView.setText(currentUser.getSurname());
                mDateView.setText(DateFormatter.convertToLocalDate(currentUser.getBirthday()));

                SharedOurPreferences.setDefaults(Misc.preferenceUserChangeStr, "true", ProfileEditActivity.this);
            }
        }, errorListener);

        request.setRetryPolicy(new DefaultRetryPolicy(
                Misc.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(request);
    }
}
