package wat.projectsi.client.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import wat.projectsi.R;
import wat.projectsi.client.ConnectingURL;
import wat.projectsi.client.DateFormatter;
import wat.projectsi.client.Misc;
import wat.projectsi.client.Picture;
import wat.projectsi.client.adapter.PostAdapter;
import wat.projectsi.client.model.Comment;
import wat.projectsi.client.model.Post;
import wat.projectsi.client.model.Profile;
import wat.projectsi.client.model.User;
import wat.projectsi.client.request.GsonRequest;

public class ProfileActivity extends BasicActivity {

    private RecyclerView mRecyclerPostView;
    private List<User> mUserList = new ArrayList<>();

    private RequestQueue mRequestQueue;
    private PostAdapter mPostAdapter;
    private List<Post> mPostList;

    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mPostList = new ArrayList<>();

        mRecyclerPostView= findViewById(R.id.postRecyclerView);
        mRecyclerPostView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false));
        mRecyclerPostView.setAdapter(mPostAdapter = new PostAdapter(mPostList, ProfileActivity.this));

        mRequestQueue = Volley.newRequestQueue(this);

        {
            Intent intend =getIntent();
            Profile profile=(Profile)getIntent().getSerializableExtra("profile");
        }
        mUser= ((Profile)getIntent().getSerializableExtra("profile")).getUser();
        if(mUser!=null) {
            ((TextView) findViewById(R.id.profileName)).setText(mUser.getName());
            ((TextView) findViewById(R.id.profileSurname)).setText(mUser.getSurname());
            new Picture(((ImageView) findViewById(R.id.profilePicture))).execute(mUser.getImage().getUrl());
            ((TextView) findViewById(R.id.gender)).setText(mUser.getGender().equals("MAN") ? getString(R.string.prompt_gender_man) : getString(R.string.prompt_gender_woman));
            ((TextView) findViewById(R.id.birthday)).setText(DateFormatter.convertToLocalDate(mUser.getBirthday()));
            if (((Profile) getIntent().getSerializableExtra("profile")).getPosts()!=null) {
                mPostList.addAll(((Profile) getIntent().getSerializableExtra("profile")).getPosts());
                mPostAdapter.notifyDataSetChanged();
            }

            for (Post post : mPostList) {
                requestPostUser(post.getUserId(), post);
                requestComments(post.getPostId());
            }
        }

        if(mUser.getId()!=MainActivity.getCurrentUser().getId())
            findViewById(R.id.profile_edit_button).setVisibility(View.GONE);
        else findViewById(R.id.profile_edit_button).setVisibility(View.VISIBLE);

        if(mUser.getId()!=MainActivity.getCurrentUser().getId())
            findViewById(R.id.new_message_button).setVisibility(View.VISIBLE);
        else findViewById(R.id.new_message_button).setVisibility(View.GONE);
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
                                requestCommentUser(comment.getUser().getId(), comment);
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

        mRequestQueue.add(request);
    }

    public void profileEdit(View view) {
        startActivity(new Intent(ProfileActivity.this, ProfileEditActivity.class));
    }

    public void messageButton(View  view){
        Intent chatIntent = new Intent(ProfileActivity.this, ChatActivity.class);
        chatIntent.putExtra("userId",  mUser.getId());
        startActivity(chatIntent);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(Misc.preferenceLanguageStr))
            recreate();
        else if( mUser.getId()==currentUser.getId() && key.equals(Misc.preferenceUserChangeStr)){
            mUser=currentUser;
            ((TextView) findViewById(R.id.profileName)).setText(mUser.getName());
            ((TextView) findViewById(R.id.profileSurname)).setText(mUser.getSurname());
            new Picture(((ImageView) findViewById(R.id.profilePicture))).execute(mUser.getImage().getUrl());
            ((TextView) findViewById(R.id.gender)).setText(mUser.getGender().equals("MAN") ? getString(R.string.prompt_gender_man) : getString(R.string.prompt_gender_woman));
            ((TextView) findViewById(R.id.birthday)).setText(DateFormatter.convertToLocalDate(mUser.getBirthday()));

        }
    }
}
