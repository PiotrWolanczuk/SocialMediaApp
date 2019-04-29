package wat.projectsi.client.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import wat.projectsi.R;
import wat.projectsi.client.ConnectingURL;
import wat.projectsi.client.GsonRequest;
import wat.projectsi.client.Misc;
import wat.projectsi.client.SharedOurPreferences;
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
    private boolean finished;
    ReadWriteLock lock = new ReentrantReadWriteLock();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        finished = true;

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

        refresh();
        startTimerThread();
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

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void logOut(MenuItem item) {
        lock.writeLock().lock();
        try {
            finished = false;
        }finally {
            lock.writeLock().unlock();
        }
        SharedOurPreferences.clear(this);
        finish();
    }

    private void requestPosts()
    {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization","Bearer "+ SharedOurPreferences.getDefaults("token",this));


        GsonRequest<Post[]> request = new GsonRequest<>( ConnectingURL.URL_Posts, Post[].class, headers, new Response.Listener<Post[]>() {
            @Override
            public void onResponse(Post[] response) {

                if(mPostList== null) {
                    mPostList = new ArrayList<>();
                    mPostList.addAll(Arrays.asList(response));
                    mRecyclerPostView.setAdapter(mPostAdapter = new PostAdapter(mPostList, MainActivity.this));
                }
                else{
                    mPostList.clear();
                    mPostList.addAll(Arrays.asList(response));
                    mPostAdapter.notifyDataSetChanged();
                }
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

    private void startTimerThread() {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            private boolean finished;
            public void run() {
                while(true) {
                    try {
                        Thread.sleep(Misc.refreshTime);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    lock.readLock().lock();
                    try {
                        finished = MainActivity.this.finished;
                    }finally {
                        lock.readLock().unlock();
                    }
                    if(finished)
                        return;
                    handler.post(new Runnable(){
                        public void run() {
                            refresh();
                        }
                    });
                }
            }
        };
        new Thread(runnable).start();
    }

    public void refresh()
    {
        requestPosts();
    }

    @Override
    protected void onDestroy(){

        lock.writeLock().lock();
        try {
            finished = false;
        }finally {
            lock.writeLock().unlock();
        }
        super.onDestroy();

    }
}
