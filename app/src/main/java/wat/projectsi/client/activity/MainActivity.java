package wat.projectsi.client.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import wat.projectsi.R;
import wat.projectsi.client.ConnectingURL;
import wat.projectsi.client.request.GsonRequest;
import wat.projectsi.client.Misc;
import wat.projectsi.client.adapter.PostAdapter;
import wat.projectsi.client.model.Post;


//TODO: Refreshing posts
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final Response.ErrorListener errorListener = new Response.ErrorListener() {
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
//                case 415://"Unsupported Media Type" ->BadJason
//                    break;
//                case 500://"Fail! -> Cause: User Role not find."
//                    break;
                default:
                    Toast.makeText(MainActivity.this, getString(R.string.error_unknown), Toast.LENGTH_SHORT).show();
            }
        }

    };

    private RecyclerView mRecyclerPostView;
    private PostAdapter mPostAdapter;
    private List<Post> mPostList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newPostIntent = new Intent(MainActivity.this, NewPostActivity.class);
                startActivity(newPostIntent);
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mRecyclerPostView= findViewById(R.id.postRecyclerView);

        mRecyclerPostView.setLayoutManager(new LinearLayoutManager(this));
        requestPosts();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {

        } else if (id == R.id.nav_gallery) {

        } else if(id == R.id.nav_people){
            writeName();
        }else if(id == R.id.nav_friends){
            Intent userIntent = new Intent(MainActivity.this, UsersActivity.class);
            userIntent.putExtra("people", "friends"); // friends
            startActivity(userIntent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void writeName() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText name = new EditText(this);
        layout.addView(name);
        dialog.setPositiveButton("Szukaj", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent userIntent = new Intent(MainActivity.this, UsersActivity.class);
                userIntent.putExtra("people", "users"); // people
                userIntent.putExtra("name", name.getText());
                dialog.dismiss();
                startActivity(userIntent);
            }
        });

        dialog.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setView(layout);
        dialog.show();
    }

    public void logOut(MenuItem item) {
        SharedPreferences myPrefs = getSharedPreferences("token",
                MODE_PRIVATE);
        SharedPreferences.Editor editor = myPrefs.edit();
        editor.clear();
        editor.commit();
        //Log.d(TAG, "Now log out and start the activity login");
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void requestPosts()
    {
        mPostList = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization","Bearer "+ getSharedPreferences(Misc.PREFERENCES_NAME, Activity.MODE_PRIVATE).getString("token",""));


        GsonRequest<Post[]> request = new GsonRequest<>( ConnectingURL.URL_Posts, Post[].class, headers, new Response.Listener<Post[]>() {
            @Override
            public void onResponse(Post[] response) {
                mPostList.addAll(Arrays.asList(response));
                mRecyclerPostView.setAdapter(mPostAdapter = new PostAdapter(mPostList, MainActivity.this) );
            }
        }, errorListener);

        requestQueue.add(request);
    }

    public void reportRequest(View view) {
        //TODO: report body
    }

    public void commentRequest(View view) {
        //TODO: comment body
    }
}
