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

import java.util.Date;
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
                changeUserProfile( MainActivity.getCurrentUser().getName(), MainActivity.getCurrentUser().getSurname(),
                        MainActivity.getCurrentUser().getBirthday().toString(), hashCode, MainActivity.getCurrentUser().getGender());
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

    private void changeUserProfile(final String name, final String surname, final String birthDate, final String hashCode, final String gender) {
        RequestQueue requestQueue = Volley.newRequestQueue(activity.getApplicationContext());

        JSONObject data = new JSONObject();
        try {
            data.put("userId", MainActivity.getCurrentUser().getId());
            data.put("firstName",name);
            data.put("lastName", surname);
            data.put("birthday",birthDate);
            data.put("hashCode", hashCode);
            data.put("gender",gender);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, ConnectingURL.URL_Users, data, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(activity.getApplicationContext(), R.string.prompt_register_success, Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("APIResponse", error.toString());
                error.printStackTrace();
                Toast.makeText(activity.getApplicationContext(), R.string.message_wrong, Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() {
                return Misc.getSecureHeaders(activity.getApplicationContext());
            }
        };
        requestQueue.add(request);
    }

    @Override
    public void destroyItem(@NonNull View container, int position, @NonNull Object object) {
        ((ViewPager)container).removeView((View)object);
    }
}
