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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import wat.projectsi.R;
import wat.projectsi.client.activity.MainActivity;
import wat.projectsi.client.request.VolleyJsonRequest;

public class UniversalImageLoader extends PagerAdapter {

    private Activity activity;
    private ImageLoader imageLoader;
    private LayoutInflater inflater;
    private String[] images;

    public UniversalImageLoader(Activity activity, ImageLoader imageLoader, String[] images) {
        this.imageLoader = imageLoader;
        this.activity = activity;
        this.images = images;
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
                pickImageForProfile(images[position]);
            }
        });
        container.addView(itemView);
        return itemView;
    }

    private void pickImageForProfile(String hashCode) {
        checkIfUserWant(activity.getApplicationContext(), hashCode);
    }

    private void checkIfUserWant(Context context, final String hashCode) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);

        dialog.setPositiveButton(context.getText(R.string.prompt_change_picture), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                changeUserProfile(hashCode);
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

    private void changeUserProfile(String hashCode) {
        RequestQueue MyRequestQueue = Volley.newRequestQueue(activity);
        final JSONObject jsonRequest = new JSONObject();
        System.out.println(MainActivity.getCurrentUser().getBirthday());
        System.out.println(MainActivity.getCurrentUser().getName());
        System.out.println(MainActivity.getCurrentUser().getGender());
        System.out.println(MainActivity.getCurrentUser().getSurname());
        System.out.println(MainActivity.getCurrentUser().getId());

        try {
            jsonRequest.put("birthday",  MainActivity.getCurrentUser().getBirthday());
            jsonRequest.put("firstName",  MainActivity.getCurrentUser().getName());
            jsonRequest.put("gender",  MainActivity.getCurrentUser().getGender());
            jsonRequest.put("hashCode",  hashCode);
            jsonRequest.put("lastName",  MainActivity.getCurrentUser().getSurname());
            jsonRequest.put("userId",  MainActivity.getCurrentUser().getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(jsonRequest);

        VolleyJsonRequest MyJsonRequest = new VolleyJsonRequest(Request.Method.PUT,
                ConnectingURL.URL_Users, jsonRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response);
                System.out.println("Zmiana zaszla pomyslnie");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("APIResponse", error.toString());
                error.printStackTrace();
                Toast.makeText(activity,
                        activity.getApplicationContext().getResources().getString(R.string.message_wrong), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");

                return headers;
            }
        };
        MyRequestQueue.add(MyJsonRequest);
    }

    @Override
    public void destroyItem(@NonNull View container, int position, @NonNull Object object) {
        ((ViewPager)container).removeView((View)object);
    }
}
