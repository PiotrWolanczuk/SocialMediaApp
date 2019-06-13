package wat.projectsi.client.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Map;

import wat.projectsi.R;
import wat.projectsi.client.ConnectingURL;
import wat.projectsi.client.DateFormatter;
import wat.projectsi.client.Misc;
import wat.projectsi.client.Validator;
import wat.projectsi.client.model.User;
import wat.projectsi.client.request.GsonRequest;

public class ProfileEditActivity extends BasicActivity {

    private EditText mNameView;
    private EditText mSurnameView;
    private TextView mDateView;
    private Switch mGenderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        mGenderView = findViewById(R.id.genderSwitch);

        mNameView = findViewById(R.id.name);
        mSurnameView = findViewById(R.id.surname);
        mDateView = findViewById(R.id.birthdayNew);

        final Calendar c = Calendar.getInstance(getResources().getConfiguration().locale);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, monthOfYear);
                c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                mDateView.setText(DateFormatter.convertToLocalDate(c.getTime()));
            }
        };

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

        User user=MainActivity.getCurrentUser();

        mGenderView.setChecked(user.getGender().equals(Misc.womanStr));

        mNameView.setText(user.getName());
        mSurnameView.setText(user.getSurname());
        mDateView.setText(DateFormatter.convertToLocalDate(user.getBirthday()));
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
            data.put("birthDate", birthDate);
            data.put("gender", isMan ? Misc.manStr : Misc.womanStr);
            data.put("userId", MainActivity.getCurrentUser().getId());
            data.put("hashCode", MainActivity.getCurrentUser().getProfileImage());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, ConnectingURL.URL_Users, data, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
               progressDialog.cancel();
                Toast.makeText(ProfileEditActivity.this, getText(R.string.prompt_register_success), Toast.LENGTH_LONG).show();

                requestCurrentUser();

                finish();
            }
        }
        , errorListener){
            @Override
            public Map<String, String> getHeaders() {
                return Misc.getSecureHeaders(getApplicationContext());
            }
        };
        requestQueue.add(request);
    }

    public void showDatePickerDialog(View view) {
        final Calendar c = Calendar.getInstance(getResources().getConfiguration().locale);
        final int mYear = c.get(Calendar.YEAR);
        final int mMonth = c.get(Calendar.MONTH);
        final int mDay = c.get(Calendar.DAY_OF_MONTH);

        // Launch Date Picker Dialog
        DatePickerDialog dpd = new DatePickerDialog(ProfileEditActivity.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        mDateView.setText(DateFormatter.convertToLocalDate(c.getTime()));
                    }
                }, mYear, mMonth, mDay);
        dpd.getDatePicker().setMinDate(DateFormatter.minDate);
        dpd.getDatePicker().setMaxDate(DateFormatter.maxDate);
        dpd.show();

    }

    private void requestCurrentUser(){
        GsonRequest<User> request = new GsonRequest<>(ConnectingURL.URL_Users_Current, User.class,
                Misc.getSecureHeaders(this), new Response.Listener<User>() {
            @Override
            public void onResponse(User response) {
                MainActivity.setCurrentUser(response);
            }
        }, errorListener);

        request.setRetryPolicy(new DefaultRetryPolicy(
                Misc.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(request);
    }
}
