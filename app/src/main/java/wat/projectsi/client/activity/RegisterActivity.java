package wat.projectsi.client.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wat.projectsi.R;
import wat.projectsi.client.Validator;

import static android.Manifest.permission.READ_CONTACTS;


/*
Oznaczenie	PU1
Nazwa	Zarejestrowanie
Opis	Założenie nowego konta użytkownika w portalu.
Aktorzy	Gość
Warunki wstępne	Kliknięcie przycisku „Zarejestruj”
Rezultat	Utworzenie nowego konta

Scenariusz
1.	Wybranie opcji rejestracji
2.	Wypełnienie formularza (Imię, Nazwisko, e-mail, hasło, potwierdzenie hasła)
3.	Zaakceptowanie regulaminu strony
4.	Potwierdzenie rejestracji
5.	Udane zakończenie procesu rejestracji

Scenariusz alternatywny	2.1 Błędne wypełnienie formularza (niezgodne hasła, niepoprawny email)
2.2. Wyświetlenie okienka z informacją “Nieprawidłowe dane”
3.1 Niezaakceptowanie regulaminu strony
3.2 Wyświetlenie okienka z informacją “Zaakceptuj regulamin”

 */

/**
 * A register screen that offers register by fill email, password, name surname.
 */
public class RegisterActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    private UserRegisterTask mRegTask = null;

    private static final String URL = "http://localhost:8080";
    private static final String URL_Signup = URL +"/api/auth/signup";

    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            if (error instanceof NetworkError) {
                Toast.makeText(getApplicationContext(), R.string.error_no_network_available, Toast.LENGTH_LONG).show();
                progressDialog.cancel();
                return;
            }

            progressDialog.cancel();
            Log.e("APIResponse", error.toString());
            System.out.println(error.toString());


            Toast.makeText(RegisterActivity.this, error.getMessage().equals("Fail -> Email is already in use!") ?
                    getString(R.string.error_used_email) : getString(R.string.error_used_login), Toast.LENGTH_SHORT).show();
        }

    };

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private EditText mRePasswordView;
    private EditText mNameView;
    private EditText mSurnameView;
    private CheckBox mAcceptTermsView;
    private View mProgressView;
    private View mRegisterFormView;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        // Set up the register form.
        mEmailView = findViewById(R.id.email);

        mPasswordView = findViewById(R.id.password);
        mRePasswordView = findViewById(R.id.re_password);
        mRegisterFormView = findViewById(R.id.register_form);
        mProgressView = findViewById(R.id.register_progress);
        mNameView = findViewById(R.id.name);
        mSurnameView = findViewById(R.id.surname);
        mAcceptTermsView = findViewById(R.id.acceptTerms);

        progressDialog = new ProgressDialog(this);

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
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mRegisterFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(RegisterActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    public void startTermActivity(View view) {
        //TODO: After create termsActivity
//        RegisterActivity.this.startActivity(new Intent(RegisterActivity.this, TermsActivity.class));
    }

    public void backToLoginActivity() {
        finish();
    }

    private void attemptRegistration()
    {
        progressDialog.setMessage(getString(R.string.action_sign_in));
        progressDialog.show();

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

        boolean cancel = false;
        View focusView = null;

        // Check if usser type valid characters
        if(!Validator.isNameValid(name)){
            mNameView.setError(getString(R.string.error_invalid_characters));
            focusView = mNameView;
            cancel = true;
        }
        if(!Validator.isNameValid(surname)){
            mSurnameView.setError(getString(R.string.error_invalid_characters));
            focusView = mSurnameView;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }
        else if ( !Validator.isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        //check retyped password if same as password
       else if ( !password.contentEquals(rePassword)) {
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
            progressDialog.hide();
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user registration attempt.
            showProgress(true);

            mRegTask = new UserRegisterTask(email, password,
                    mNameView.getText().toString(), mSurnameView.getText().toString());
            mRegTask.execute((Void) null);

        }
    }

    /**
     * Represents an asynchronous registration task used to register
     * the user.
     */
    public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {

        //private int error_code;

        private final String mEmail;
        private final String mPassword;
        private final String mName;
        private final String mSurname;

        UserRegisterTask(String email, String password, String name, String surname) {
            mEmail = email;
            mPassword = password;
            mName = name;
            mSurname = surname;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt registration against a network service.
            return registerRequest(mEmail, mEmail, mPassword, mName, mSurname, new Date().toString());

        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mRegTask = null;
            showProgress(false);

            if (success) {
                backToLoginActivity();
            } else {
                mEmailView.setError(getString(R.string.error_user_exist));
                mEmailView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mRegTask = null;
            showProgress(false);
        }

        private boolean registerRequest(final String login, final String email, final String password, final String name, final String surname, final String birthDate) {
            RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_Signup, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressDialog.cancel();
                    Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_LONG).show();

                    backToLoginActivity();
                }
            }, errorListener) {
                protected Map<String, String> getParams() {
                    Map<String, String> data = new HashMap<>();
                    //Add the data you'd like to send to the server.
                    //TODO : birthDate, login
                    data.put("login", login);
                    data.put("email", email);
                    data.put("password", password);
                    data.put("name", name);
                    data.put("surname", surname);
                    data.put("birthDate", birthDate);
                    return data;
                }
            };
            requestQueue.add(stringRequest);

            return false;
        }
    }
}
