package wat.projectsi.client.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.Calendar;

import wat.projectsi.R;
import wat.projectsi.client.ConnectingURL;
import wat.projectsi.client.DateFormatter;
import wat.projectsi.client.Validator;


public class RegisterActivity extends AppCompatActivity {

    private static final String manStr="man";
    private static final String womanStr="woman";

    private final Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            if (error instanceof NetworkError || error instanceof TimeoutError) {
                Log.e("NetworkError", error.toString());
                Toast.makeText(getApplicationContext(), R.string.error_no_network_available, Toast.LENGTH_LONG).show();
                mProgressDialog.cancel();
                return;
            } else if (error == null) {
                Toast.makeText(getApplicationContext(), R.string.error_no_network_available, Toast.LENGTH_LONG).show();
                mProgressDialog.cancel();
                return;
            }

            if (error.networkResponse == null) {
                Toast.makeText(getApplicationContext(), R.string.error_no_network_available, Toast.LENGTH_LONG).show();
                mProgressDialog.cancel();
                return;
            }

            mProgressDialog.cancel();
            Log.e("APIResponse", error.toString());
            System.out.println("Kod " + error.networkResponse.statusCode);

            switch (error.networkResponse.statusCode) {
//                case 200://"OK"
//                case 201://"Created"
//                    break;
                case 400://"Bad Request"
                    switch (error.getMessage() != null ? error.getMessage() : "") {
                        case "Fail -> Email is already in use!":
                            mEmailView.setError(getString(R.string.error_used_email));
                            mEmailView.requestFocus();
                            break;
                        case "Fail -> Username is already taken!":
                            Toast.makeText(RegisterActivity.this, getString(R.string.error_used_login), Toast.LENGTH_SHORT).show();
                            break;

                        default:
                            Toast.makeText(RegisterActivity.this, getString(R.string.error_unknown), Toast.LENGTH_SHORT).show();
                    }

                    break;
//                case 401://"Unauthorized"
//                    break;
//                case 403://"Forbidden"
//                    break;
//                case 404://"Not Found"
//                    break;
//                case 415://"Unsupported Media Type" ->BadJason
//                    break;
//                case 500://"Fail! -> Cause: User Role not find."
//                    break;
                default:
                    Toast.makeText(RegisterActivity.this, getString(R.string.error_unknown), Toast.LENGTH_SHORT).show();
            }
        }

    };

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private EditText mRePasswordView;
    private EditText mNameView;
    private EditText mSurnameView;
    private EditText mLoginView;
    private CheckBox mAcceptTermsView;
    private ProgressDialog mProgressDialog;
    private TextView mDateView;
    private Switch mGenderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Set up the register form.
        mEmailView = findViewById(R.id.email);

        mPasswordView = findViewById(R.id.password);
        mRePasswordView = findViewById(R.id.re_password);
        mNameView = findViewById(R.id.name);
        mSurnameView = findViewById(R.id.surname);
        mLoginView = findViewById(R.id.login);
        mAcceptTermsView = findViewById(R.id.acceptTerms);
        mDateView = findViewById(R.id.date);

        mProgressDialog = new ProgressDialog(this);
        mGenderView = findViewById(R.id.genderSwitch);

        mGenderView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mGenderView.setText(R.string.prompt_gender_man);
                }
                else {
                    mGenderView.setText(R.string.prompt_gender_woman);
                }
            }
        });


        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptRegistration();
                    return true;
                }
                return false;
            }
        });

        findViewById(R.id.registerButton).
                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        attemptRegistration();
                    }
                });

        final Calendar c = Calendar.getInstance(getResources().getConfiguration().locale);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, monthOfYear);
                c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                mDateView.setText(DateFormatter.viewDateFormat().format(c.getTime()));
            }
        };

        mDateView.setText( DateFormatter.viewDateFormat().format(c.getTime()));
    }

    public void startTermActivity(View view) {
        //TODO: After create termsActivity
//        RegisterActivity.this.startActivity(new Intent(RegisterActivity.this, TermsActivity.class));
    }

    private void backToLoginActivity() {
        finish();
    }

    private void attemptRegistration() {
        mProgressDialog.setMessage(getString(R.string.action_sign_in));
        mProgressDialog.show();

        mPasswordView.setError(null);
        mRePasswordView.setError(null);
        mEmailView.setError(null);
        mAcceptTermsView.setError(null);
        mNameView.setError(null);
        mSurnameView.setError(null);

        String password = mPasswordView.getText().toString();
        String email = mEmailView.getText().toString();
        String rePassword = mRePasswordView.getText().toString();
        String name = mNameView.getText().toString();
        String surname = mSurnameView.getText().toString();
        String login = mLoginView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check if usser type valid characters
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

        if (!Validator.isLoginValid(login)) {
            mLoginView.setError(getString(R.string.error_invalid_login));
            focusView = mLoginView;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (!Validator.isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        //check retyped password if same as password
        else if (!password.contentEquals(rePassword)) {
            mRePasswordView.setError(getString(R.string.error_repassword_password_not_same));
            focusView = mRePasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!Validator.isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        //Check if terms are accepted
        if (!mAcceptTermsView.isChecked()) {
            mAcceptTermsView.setError(getString(R.string.error_not_accepted_terms));
            focusView = mAcceptTermsView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt register and focus the first
            // form field with an error.
            mProgressDialog.hide();
            focusView.requestFocus();
        } else {

            mProgressDialog.show();

            registerRequest(login, email, password,
                    mNameView.getText().toString(), mSurnameView.getText().toString(),
                    DateFormatter.convertToApi(mDateView.getText().toString()),
                    mGenderView.isChecked());
        }
    }

    private void registerRequest(final String login, final String email, final String password, final String name, final String surname, final String birthDate, boolean isMan) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JSONObject data = new JSONObject();
        try {
            data.put("login", login);
            data.put("email", email);
            data.put("password", password);
            data.put("name", name);
            data.put("surname", surname);
            data.put("birthDate", birthDate);
            data.put("gender", isMan? manStr:womanStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, ConnectingURL.URL_Signup, data, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                mProgressDialog.cancel();
                Toast.makeText(RegisterActivity.this, getText(R.string.prompt_register_success), Toast.LENGTH_LONG).show();

                backToLoginActivity();
            }
        }, errorListener);
        requestQueue.add(request);

    }

    public void showDatePickerDialog(View view) {
        final Calendar c = Calendar.getInstance(getResources().getConfiguration().locale);
        final int mYear = c.get(Calendar.YEAR);
        final int mMonth = c.get(Calendar.MONTH);
        final int mDay = c.get(Calendar.DAY_OF_MONTH);

        // Launch Date Picker Dialog
        DatePickerDialog dpd = new DatePickerDialog(RegisterActivity.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // Display Selected date in textbox

                        if (year > mYear)
                            view.updateDate(mYear, mMonth, mDay);

                        if (monthOfYear > mMonth && year == mYear)
                            view.updateDate(mYear, mMonth, mDay);

                        if (dayOfMonth > mDay && year == mYear && monthOfYear == mMonth)
                            view.updateDate(mYear, mMonth, mDay);

                        mDateView.setText(dayOfMonth + "-"
                                + (monthOfYear + 1) + "-" + year);

                    }
                }, mYear, mMonth, mDay);
        dpd.getDatePicker().setMinDate(DateFormatter.minDate);
        dpd.getDatePicker().setMaxDate(DateFormatter.maxDate);
        dpd.show();

    }
}
