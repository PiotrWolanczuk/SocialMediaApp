package wat.projectsi.client.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import wat.projectsi.R;
import wat.projectsi.client.ConnectingURL;
import wat.projectsi.client.Picture;
import wat.projectsi.client.model.Comment;
import wat.projectsi.client.model.Profile;
import wat.projectsi.client.model.User;
import wat.projectsi.client.request.GsonRequest;
import wat.projectsi.client.Misc;
import wat.projectsi.client.SharedOurPreferences;
import wat.projectsi.client.adapter.NotificationAdapter;
import wat.projectsi.client.adapter.PostAdapter;
import wat.projectsi.client.model.notification.Notification;
import wat.projectsi.client.model.notification.NotificationAcquaintance;
import wat.projectsi.client.model.notification.NotificationMessage;
import wat.projectsi.client.model.notification.NotificationPost;
import wat.projectsi.client.model.Post;


//user1 UserPass1

public class MainActivity extends BasicActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView mRecyclerPostView;
    private PostAdapter mPostAdapter;
    private List<Post> mPostList;
    private RecyclerView mRecyclerNotificationsView;
    private NotificationAdapter mNotificationAdapter;
    private List<NotificationAcquaintance> mNotificationAcquaintanceList;
    private List<NotificationMessage> mNotificationMessageList;
    private List<NotificationPost> mNotificationPostList;
    private List<Notification> mNotificationList;
    private PopupWindow mPopupWindow;
    private NavigationView navigationView;

    private boolean isResponse = false;
    private static volatile boolean finished;
    private Handler handler;
    private static Lock lock = new ReentrantLock();
    private static User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        finished = false;

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.action_bar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newPostIntent = new Intent(MainActivity.this, NewPostActivity.class);
                if(mPopupWindow!=null)
                    closeNotification();
                startActivity(newPostIntent);
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                if(mPopupWindow!=null)
                    closeNotification();
                super.onDrawerOpened(drawerView);
            }
        };

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mRecyclerPostView= findViewById(R.id.postRecyclerView);
        mRecyclerPostView.setLayoutManager(new LinearLayoutManager(this));

        mNotificationList = new ArrayList<>();
        mNotificationAcquaintanceList = new ArrayList<>();
        mNotificationMessageList = new ArrayList<>();
        mNotificationPostList = new ArrayList<>();
        mPostList = new ArrayList<>();

        mNotificationAdapter = new NotificationAdapter(mNotificationList, MainActivity.this);
        mRecyclerPostView.setAdapter(mPostAdapter = new PostAdapter(mPostList, MainActivity.this));


        handler = new Handler();
        requestCurrentUser();
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
            showCurrentProfile(findViewById(R.id.nav_view));

        } else if (id == R.id.nav_gallery) {

        } else if(id == R.id.nav_people){
            writeName();
        }else if(id == R.id.nav_friends){
            Intent userIntent = new Intent(MainActivity.this, UsersActivity.class);
            userIntent.putExtra("people", "friends"); // friends
            startActivity(userIntent);
        } else if(id == R.id.nav_statute){
            showStatute();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showStatute() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final TextView statute = new TextView(this);
        statute.setText(R.string.terms_text);
        layout.addView(statute);

        dialog.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setView(layout);
        dialog.show();
    }

    private void writeName() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText name = new EditText(this);
        name.setHint("Name");
        layout.addView(name);

        final EditText surname = new EditText(this);
        surname .setHint("Surname");
        layout.addView(surname);

        dialog.setPositiveButton("Szukaj", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent userIntent = new Intent(MainActivity.this, UsersActivity.class);
                userIntent.putExtra("people", "users"); // users
                userIntent.putExtra("name", name.getText().toString());
                userIntent.putExtra("surname", surname.getText().toString());
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
        lock.lock();
        try {
            finished = true;
            requestQueue.stop();
        }finally {
            lock.unlock();
        }
        SharedOurPreferences.clear(this);
        finish();
    }

    private void requestPosts()
    {
        GsonRequest<Post[]> request = new GsonRequest<>( ConnectingURL.URL_Posts, Post[].class, Misc.getSecureHeaders(this), new Response.Listener<Post[]>() {
            @Override
            public void onResponse(Post[] response) {

                List<Post> list =new ArrayList<>();

                outerLoop:
                for(Post post: response)
                {
                    for(Post oldPost:mPostList)
                    {
                        if(post.getPostId()==oldPost.getPostId())
                            continue outerLoop;
                    }
                    list.add(post);
                }

                if(list.size()>0) {
                    mPostList.addAll(list);
                    Collections.sort(mPostList, new Comparator<Post>() {
                        @Override
                        public int compare(Post o1, Post o2) {
                            return o2.getSentDate().compareTo(o1.getSentDate());
                        }
                    });
                    mPostAdapter.notifyDataSetChanged();
                }

                for (Post post : mPostList) {
                    requestComments(post.getPostId());
                }
            }
        }, errorListener);

        requestQueue.add(request);
    }

    private void requestComments(final long postID)
    {
        GsonRequest<Comment[]> request = new GsonRequest<>( ConnectingURL.URL_Comments+"/"+postID, Comment[].class, Misc.getSecureHeaders(this), new Response.Listener<Comment[]>() {
            @Override
            public void onResponse(Comment[] response) {
                for(Post post:mPostList)
                    if(post.getPostId()==postID) {
                        if (post.getCommentList() != null) {
                            List<Comment> list =new ArrayList<>();

                            outerLoop:
                            for (Comment comment : response) {
                                for (Comment oldComment : post.getCommentList()) {
                                    if (comment.getCommentId() == oldComment.getCommentId())
                                        continue outerLoop;
                                }
                                list.add(comment);
                            }
                            if (list.size() > 0) {
                                post.getCommentList().addAll(list);
                                Collections.sort(post.getCommentList(), new Comparator<Comment>() {
                                    @Override
                                    public int compare(Comment o1, Comment o2) {
                                        return o1.getSendDate().compareTo(o2.getSendDate());
                                    }
                                });
                                mPostAdapter.notifyDataSetChanged();
                            }
                        }
                        else{
                            post.setCommentList(Arrays.asList(response));
                            mPostAdapter.notifyDataSetChanged();
                        }
                        break;
                    }
            }
        }, errorListener);

        requestQueue.add(request);
    }

    private void requestNotifications()
    {
        {
            GsonRequest<NotificationAcquaintance[]> request = new GsonRequest<>(ConnectingURL.URL_Notifications_Acquaintance, NotificationAcquaintance[].class,
                    Misc.getSecureHeaders(this), new Response.Listener<NotificationAcquaintance[]>() {
                @Override
                public void onResponse(NotificationAcquaintance[] response) {
                    mNotificationAcquaintanceList.clear();
                    mNotificationAcquaintanceList.addAll(Arrays.asList(response));
                    isResponse = true;
                }
            }, errorListener);

            requestQueue.add(request);
        }

        {
            GsonRequest<NotificationMessage[]> request = new GsonRequest<>(ConnectingURL.URL_Notifications_Messages, NotificationMessage[].class,
                    Misc.getSecureHeaders(this), new Response.Listener<NotificationMessage[]>() {
                @Override
                public void onResponse(NotificationMessage[] response) {
                    mNotificationMessageList.clear();
                    mNotificationMessageList.addAll(Arrays.asList(response));
                    isResponse = true;
                }
            }, errorListener);

            requestQueue.add(request);
        }

        {
            GsonRequest<NotificationPost[]> request = new GsonRequest<>(ConnectingURL.URL_Notifications_Post, NotificationPost[].class,
                    Misc.getSecureHeaders(this), new Response.Listener<NotificationPost[]>() {
                @Override
                public void onResponse(NotificationPost[] response) {
                    mNotificationPostList.clear();
                    mNotificationPostList.addAll(Arrays.asList(response));
                    isResponse = true;
                }
            }, errorListener);

            requestQueue.add(request);
        }

        if(isResponse)
        {
            mNotificationList.clear();
            mNotificationList.addAll(mNotificationAcquaintanceList);
            mNotificationList.addAll(mNotificationMessageList);
            mNotificationList.addAll(mNotificationPostList);

            Collections.sort(mNotificationList, new Comparator<Notification>() {
                @Override
                public int compare(Notification o1, Notification o2) {
                    return o2.getDateTimeOfSend().compareTo(o1.getDateTimeOfSend());
                }
            });

            mNotificationAdapter.notifyDataSetChanged();
            isResponse=false;
        }
    }

    public void reportRequest(View view) {
        //TODO: report body
    }

    public void addCommentRequest(View view) {

        TextView newCommentView =((View)(view.getParent().getParent())).findViewById(R.id.postNewComment);
        JSONObject data = new JSONObject();
        try {

            data.put("commentContest", newCommentView.getText());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, ConnectingURL.URL_Comments + "/" + view.getTag(), data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(MainActivity.this, getString(R.string.prompt_successful_comment), Toast.LENGTH_SHORT).show();
                    }
                }, errorListener) {
            @Override
            public Map<String, String> getHeaders() {
                return Misc.getSecureHeaders(MainActivity.this);
            }
        };
        requestQueue.add(request);

        newCommentView.setText(null);

    }

    public void acceptInvitation(View view) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, ConnectingURL.URL_Acquaintances + "/" + view.getTag() + "/accept", new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                    }
                }, errorListener) {
            @Override
            public Map<String, String> getHeaders() {
                return Misc.getSecureHeaders(MainActivity.this);
            }
        };
        requestQueue.add(request);
    }

    public void rejectInvitation(View view) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, ConnectingURL.URL_Acquaintances + "/" + view.getTag() + "/reject", new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                    }
                }, errorListener) {
            @Override
            public Map<String, String> getHeaders() {
                return Misc.getSecureHeaders(MainActivity.this);
            }
        };
        requestQueue.add(request);
    }

    private void requestCurrentUser(){
        GsonRequest<User> request = new GsonRequest<>(ConnectingURL.URL_Users_Current, User.class,
                Misc.getSecureHeaders(this), new Response.Listener<User>() {
            @Override
            public void onResponse(User response) {
                currentUser=response;
                new Picture((ImageView)navigationView.findViewById(R.id.profilePicture)).execute(currentUser.getProfileImage());
                ((TextView)navigationView.findViewById(R.id.profileName)).setText(currentUser.getName()+" "+currentUser.getSurname());
            }
        }, errorListener);

        requestQueue.add(request);
    }

    private void startTimerThread() {

        Runnable mTimerThread = new Runnable() {
            public void run() {
                lock.lock();
                try {
                    if (!MainActivity.finished) {
                        refresh();
                        handler.postDelayed(this, Misc.refreshTime);
                    }
                } finally {
                    lock.unlock();
                }
            }
        };

        handler.postDelayed(mTimerThread, Misc.refreshTime);
    }

    public void refresh()
    {
        requestNotifications();
        requestPosts();
    }

    @Override
    protected void onDestroy(){

        if(!finished ){
            lock.lock();
            try {
                finished = false;
                requestQueue.stop();
            } finally {
                lock.unlock();
            }
        }

        if(mPopupWindow!=null)
            closeNotification();

        super.onDestroy();

    }

    private void closeNotification()
    {
        mPopupWindow.dismiss();
        mRecyclerNotificationsView=null;
        mPopupWindow=null;
    }

    public void display_notifications(View view) {
        if(mPopupWindow==null) {
            View popupView = LayoutInflater.from(MainActivity.this).inflate(R.layout.notification_popup_main, null);
            mPopupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);

            mRecyclerNotificationsView=popupView.findViewById(R.id.notificationRecyclerView);
            mRecyclerNotificationsView.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerNotificationsView.setAdapter(mNotificationAdapter);

            mPopupWindow.showAsDropDown(findViewById(R.id.notifications_button));
        }
        else closeNotification();
    }

    public void onToolbarClick(View view) {
        if(mPopupWindow!=null)
            closeNotification();
    }

    public static User getCurrentUser()
    {
        return currentUser;
    }

}
