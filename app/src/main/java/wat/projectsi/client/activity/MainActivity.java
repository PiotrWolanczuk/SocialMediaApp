package wat.projectsi.client.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
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
import wat.projectsi.client.Misc;
import wat.projectsi.client.Picture;
import wat.projectsi.client.SharedOurPreferences;
import wat.projectsi.client.adapter.NotificationAdapter;
import wat.projectsi.client.adapter.PostAdapter;
import wat.projectsi.client.model.Comment;
import wat.projectsi.client.model.Post;
import wat.projectsi.client.model.User;
import wat.projectsi.client.model.notification.Notification;
import wat.projectsi.client.model.notification.NotificationAcquaintance;
import wat.projectsi.client.model.notification.NotificationMessage;
import wat.projectsi.client.model.notification.NotificationPost;
import wat.projectsi.client.request.GsonRequest;
import wat.projectsi.client.request.VolleyJsonRequest;

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
    private String nameText;
    private String surnameText;

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

        MenuItem adminItem = navigationView.getMenu().findItem(R.id.nav_violation_posts);
        MenuItem adminItem2 = navigationView.getMenu().findItem(R.id.nav_violation_comments);
        if(SharedOurPreferences.getDefaults(Misc.preferenceRoleStr, MainActivity.this).equals(Misc.roleAdminStr)){
            adminItem.setVisible(true);
            adminItem2.setVisible(true);
        }

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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            showCurrentProfile(findViewById(R.id.nav_view));
        } else if (id == R.id.nav_gallery) {
            Intent galleryIntent = new  Intent(MainActivity.this, GalleryActivity.class);
            galleryIntent.putExtra("userId", currentUser.getId());
            startActivity(galleryIntent);
        } else if(id == R.id.nav_people){
            writeName();
        }else if(id == R.id.nav_friends){
            Intent userIntent = new Intent(MainActivity.this, UsersActivity.class);
            userIntent.putExtra("people", "friends"); // friends
            startActivity(userIntent);
        } else if(id == R.id.nav_statute){
            showStatute();
        }else if(id == R.id.nav_violation_posts){
            Intent violationsActivity = new Intent(MainActivity.this, ViolationActivity.class);
            violationsActivity.putExtra("violations", "posts");
            startActivity(violationsActivity);
        }
        else if(id == R.id.nav_violation_comments){
            Intent violationsActivity = new Intent(MainActivity.this, ViolationActivity.class);
            violationsActivity.putExtra("violations", "comments");
            startActivity(violationsActivity);
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
        name.setHint(R.string.name);
        layout.addView(name);

        final EditText surname = new EditText(this);
        surname .setHint(R.string.surname);
        layout.addView(surname);

        dialog.setPositiveButton(R.string.search, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent userIntent = new Intent(MainActivity.this, UsersActivity.class);
                userIntent.putExtra("people", "users"); // users
                nameText = name.getText().toString();
                surnameText = surname.getText().toString();
                if(nameText.isEmpty())
                    nameText = " ";
                if(surnameText.isEmpty())
                    surnameText = " ";
                userIntent.putExtra("name", nameText);
                userIntent.putExtra("surname", surnameText);

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
                list.clear();


                outerLoop:
                for(Post oldPost:mPostList)
                {
                    for(Post post: response)
                        if(post.getPostId()==oldPost.getPostId())
                            continue outerLoop;
                        list.add(oldPost);
                }

                if(list.size()>0) {
                    mPostList.removeAll(list);
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

        request.setRetryPolicy(new DefaultRetryPolicy(
                Misc.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

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
                            list.clear();

                            outerLoop:
                            for (Comment oldComment : post.getCommentList()) {
                                for (Comment comment : response) {
                                    if (comment.getCommentId() == oldComment.getCommentId())
                                        continue outerLoop;
                                }
                                list.add(oldComment);
                            }
                            if (list.size() > 0) {
                                post.getCommentList().removeAll(list);
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

        request.setRetryPolicy(new DefaultRetryPolicy(
                Misc.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(request);
    }

    public void deleteRequest(View view){
        View parent = view.getRootView().findViewById(R.id.postContent);
        if(currentUser.getId() == (long)parent.getTag() || SharedOurPreferences.getDefaults(Misc.preferenceRoleStr, MainActivity.this).equals(Misc.roleAdminStr)){

            StringRequest MyJsonRequest = new StringRequest(Request.Method.DELETE,
                    ConnectingURL.URL_Posts + "/" + parent.getTag(), new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Toast.makeText(MainActivity.this, R.string.done, Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("APIResponse", error.toString());
                    error.printStackTrace();
                    Toast.makeText(MainActivity.this,
                            getApplicationContext().getResources().getString(R.string.message_wrong),
                            Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                public Map<String, String> getHeaders() {
                    return Misc.getSecureHeaders(MainActivity.this);
                }
            };

            requestQueue.add(MyJsonRequest);
        }
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

    public void reportRequestPost(View view){
        reportRequest(view, 1);
    }
    public void reportRequestComment(View view){
        reportRequest(view, 2);
    }
    private void reportRequest(final View view, final int commentOrPost) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText description = new EditText(this);
        description.setHint(R.string.description);
        layout.addView(description);

        dialog.setPositiveButton(R.string.send, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    report(view, description.getText().toString(), commentOrPost);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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

    public void reportRequest(JSONObject jsonRequest,  String url){
        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);

        VolleyJsonRequest MyJsonRequest = new VolleyJsonRequest(Request.Method.POST,
                url, jsonRequest, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(MainActivity.this, R.string.done, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("APIResponse", error.toString());
                error.printStackTrace();
                Toast.makeText(MainActivity.this,
                        getApplicationContext().getResources().getString(R.string.message_wrong),
                        Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                return Misc.getSecureHeaders(MainActivity.this);
            }
        };

        MyRequestQueue.add(MyJsonRequest);
    }
    private void report(View view, String description, int commentOrPost) throws JSONException {
        JSONObject jsonRequest = new JSONObject();

        if(commentOrPost == 1){
            View viewWithId = view.getRootView().findViewById(R.id.delete_button);
            jsonRequest.put("postId",  viewWithId.getTag());
            jsonRequest.put("violationDescription", description);
            reportRequest(jsonRequest, ConnectingURL.URL_Violations + "/posts");
        }
        else if(commentOrPost == 2){
            View viewWithId = view.getRootView().findViewById(R.id.commentContent);
            jsonRequest.put("commentId",  viewWithId.getTag());
            jsonRequest.put("violationDescription", description);
            reportRequest(jsonRequest, ConnectingURL.URL_Violations + "/comments");
        }
    }

    public void addCommentRequest(View view) {

        TextView newCommentView =((View)(view.getParent().getParent())).findViewById(R.id.postNewComment);
        if(!newCommentView.getText().equals("")){
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

            request.setRetryPolicy(new DefaultRetryPolicy(
                    Misc.MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            newCommentView.setText(null);
        }
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
                new Picture((ImageView)navigationView.findViewById(R.id.profilePicture)).execute(currentUser.getImage().getUrl());
                ((TextView)navigationView.findViewById(R.id.profileName)).setText(currentUser.getName()+" "+currentUser.getSurname());
            }
        }, errorListener);

        request.setRetryPolicy(new DefaultRetryPolicy(
                Misc.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

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

    public void settings(MenuItem item) {
        startActivity(new Intent(MainActivity.this, SettingsActivity.class));
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(Misc.preferenceLanguageStr))
            recreate();
        else if(key.equals(Misc.preferenceUserChangeStr)){
            new Picture((ImageView)navigationView.findViewById(R.id.profilePicture)).execute(currentUser.getImage().getUrl());
            ((TextView)navigationView.findViewById(R.id.profileName)).setText(currentUser.getName()+" "+currentUser.getSurname());

        }
    }

    public static User getCurrentUser() {
        return currentUser;
    }
}
