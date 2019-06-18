package wat.projectsi.client.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import wat.projectsi.R;
import wat.projectsi.client.ConnectingURL;
import wat.projectsi.client.DateFormatter;
import wat.projectsi.client.Misc;
import wat.projectsi.client.Picture;
import wat.projectsi.client.activity.BasicActivity;
import wat.projectsi.client.activity.MainActivity;
import wat.projectsi.client.model.Image;
import wat.projectsi.client.model.User;
import wat.projectsi.client.request.VolleyJsonRequest;

public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.PictureViewHolder> {
    private List<Image> images;
    private Context context;
    private ProgressDialog progressDialog;

    public PictureAdapter(List<Image> images, Context context) {
        this.images = images;
        this.context = context;
    }

    @NonNull
    @Override
    public PictureAdapter.PictureViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new PictureAdapter.PictureViewHolder(LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.single_image,  viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PictureAdapter.PictureViewHolder pictureViewHolder, int i) {
        final Image image = images.get(i);

        new Picture(pictureViewHolder.picture).execute(image.getUrl());
        pictureViewHolder.picture.setTag(image.getId());

        progressDialog = new ProgressDialog(context);
        pictureViewHolder.picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUserDialog(context, image);
            }
        });
    }

    private void showUserDialog(final Context context, final Image user) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);

        dialog.setPositiveButton(context.getText(R.string.prompt_change_picture), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                changeUserProfile(user.getId());
                progressDialog.setMessage(context.getApplicationContext().getString(R.string.message_progress));
                progressDialog.show();
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

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class PictureViewHolder extends RecyclerView.ViewHolder {
        ImageView picture;
        public PictureViewHolder(@NonNull View itemView) {
            super(itemView);

            picture = itemView.findViewById(R.id.single_photo);
        }
    }

    private void changeUserProfile(final long pictureId) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        String date = DateFormatter.convertToLocalDate(MainActivity.getCurrentUser().getBirthday());
        JSONObject data = new JSONObject();
        try {
            data.put("userId", MainActivity.getCurrentUser().getId());
            data.put("firstName",MainActivity.getCurrentUser().getName());
            data.put("lastName", MainActivity.getCurrentUser().getSurname());
            data.put("birthday",DateFormatter.convertToApi(date));
            data.put("profilePictureId", pictureId);
            data.put("gender",MainActivity.getCurrentUser().getGender());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        VolleyJsonRequest request = new VolleyJsonRequest(Request.Method.PUT, ConnectingURL.URL_Users, data, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                Toast.makeText(context, R.string.done, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e("APIResponse", error.toString());
                error.printStackTrace();
                Toast.makeText(context, R.string.message_wrong, Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() {
                return Misc.getSecureHeaders(context);
            }
        };
        requestQueue.add(request);
    }
}
