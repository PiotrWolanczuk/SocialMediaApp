package wat.projectsi.client;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.ImageLoader;

import wat.projectsi.R;
import wat.projectsi.client.model.Profile;
import wat.projectsi.client.model.User;
import wat.projectsi.client.request.GsonRequest;

public class UniversalImageLoader extends PagerAdapter {

    private Activity activity;
    private ImageLoader imageLoader;
    private LayoutInflater inflater;
    private String[] images;
    private long userId;
    private long[] imagesId;
    private User currentUser;
    RequestQueue requestQueue;

    public UniversalImageLoader(Activity activity, ImageLoader imageLoader, String[] images, long userId, long[] imagesId) {
        this.imageLoader = imageLoader;
        this.activity = activity;
        this.images = images;
        this.userId = userId;
        this.imagesId = imagesId;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        inflater = (LayoutInflater) activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.viewpager_item,  container, false);

        ImageView imageView = itemView.findViewById(R.id.imageViewForPage);
        imageLoader.displayImage(images[position], imageView);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImageForProfile(position, imagesId);
            }
        });
        container.addView(itemView);
        return itemView;
    }

    private void pickImageForProfile(int position, long[] imagesId) {
        checkIfUserWant(activity.getApplicationContext());
    }

    private void checkIfUserWant(Context context) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);

        dialog.setPositiveButton(context.getText(R.string.prompt_change_picture), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                changeUserProfile();
                dialog.dismiss();
            }
        });

        dialog.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void changeUserProfile() {
        requestCurrentUser();

//        requestQueue = Volley.newRequestQueue(activity.getApplicationContext());
//        GsonRequest<User> request = new GsonRequest<>(Request.Method.PUT, ConnectingURL.URL_Users_Profile, Profile.class,
//                Misc.getSecureHeaders(activity.getApplicationContext()), new Response.Listener<User>() {
//            @Override
//            public void onResponse(User response) {
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e("API", error.getMessage());
//            }
//        });

//        requestQueue.add(request);
    }

    @Override
    public void destroyItem(@NonNull View container, int position, @NonNull Object object) {
        ((ViewPager)container).removeView((View)object);
    }

    private void requestCurrentUser(){
        requestQueue = Volley.newRequestQueue(activity.getApplicationContext());
        GsonRequest<User> request = new GsonRequest<>(ConnectingURL.URL_Users_Profile, User.class,
                Misc.getSecureHeaders(activity.getApplicationContext()), new Response.Listener<User>() {
            @Override
            public void onResponse(User response) {
                currentUser = response;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("API", error.getMessage());
            }
        });

        requestQueue.add(request);
    }

}
