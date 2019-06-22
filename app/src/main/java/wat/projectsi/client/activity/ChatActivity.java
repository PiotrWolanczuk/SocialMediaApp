package wat.projectsi.client.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import wat.projectsi.R;
import wat.projectsi.client.ConnectingURL;
import wat.projectsi.client.Misc;
import wat.projectsi.client.SharedOurPreferences;
import wat.projectsi.client.request.VolleyJsonRequest;

public class ChatActivity extends BaseSettingChangeActivity {

    TextView allMessages;
    long mUserId;
    StringBuilder messages = new StringBuilder();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        allMessages = findViewById(R.id.all_message);

        Bundle bundle = getIntent().getExtras();
        mUserId = bundle.getLong("userId");

        showChat();
    }

    private void showChat() {
        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest MyJsonRequest = new JsonArrayRequest( Request.Method.GET,
                ConnectingURL.URL_Message+"/"+mUserId,null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                System.out.println(response.toString());
                for(int i =0; i <response.length(); i++){
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        messages.append(getApplicationContext().getString(R.string.sender) + ": ");
                        messages.append(jsonObject.getJSONObject("sender").getString("firstName") + ":\t");
                        messages.append(jsonObject.getString("content") + "\n");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                allMessages.setText(messages);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("APIResponse", error.toString());
                Toast.makeText(ChatActivity.this, getResources().getString(R.string.message_wrong), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() {
                return Misc.getSecureHeaders(ChatActivity.this);
            }
        };
        MyRequestQueue.add(MyJsonRequest);
    }

    public void  newMessage(final View view){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText message = new EditText(this);
        layout.addView(message);

        dialog.setPositiveButton(R.string.send, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sendNewMessage(message.getText().toString());
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

    private void sendNewMessage(String content) {
        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);

        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("content", content);
            jsonRequest.put("receiverId", mUserId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        VolleyJsonRequest MyJsonRequest = new VolleyJsonRequest( Request.Method.POST,
                ConnectingURL.URL_Message,jsonRequest, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("APIResponse", error.toString());
                Toast.makeText(ChatActivity.this, getResources().getString(R.string.message_wrong), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() {
                return Misc.getSecureHeaders(ChatActivity.this);
            }
        };
        MyRequestQueue.add(MyJsonRequest);
    }
}
