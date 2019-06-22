package wat.projectsi.client.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import wat.projectsi.R;
import wat.projectsi.client.ConnectingURL;
import wat.projectsi.client.Misc;
import wat.projectsi.client.adapter.MessageAdapter;
import wat.projectsi.client.model.Message;
import wat.projectsi.client.request.GsonRequest;
import wat.projectsi.client.request.VolleyJsonRequest;

public class ChatActivity extends BasicActivity {

    private RecyclerView mRecyclerMessageView;
    private MessageAdapter mMessageAdapter;
    private List<Message> mMessageList;
    long mUserId;

    private static volatile boolean finished;
    private Handler handler;
    private static Lock lock = new ReentrantLock();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mMessageList = new ArrayList<>();
        mMessageAdapter = new MessageAdapter(ChatActivity.this, mMessageList);

        mRecyclerMessageView = findViewById(R.id.messageRecyclerView);
        mRecyclerMessageView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL, false));
        mRecyclerMessageView.setAdapter(mMessageAdapter);

        Bundle bundle = getIntent().getExtras();
        mUserId = bundle.getLong("userId");

        handler = new Handler();

        updateChat();

        startTimerThread();
    }

    private void updateChat() {
        GsonRequest<Message[]> request= new GsonRequest<>(ConnectingURL.URL_Message+"/"+mUserId, Message[].class,Misc.getSecureHeaders(this), new Response.Listener<Message[]>(){
            @Override
            public void onResponse(Message[] response) {
                List<Message> list =new ArrayList<>();

                outerLoop:
                for(Message message: response)
                {
                    for(Message oldMessage:mMessageList)
                    {
                        if(message.getMessageId().equals(oldMessage.getMessageId()))
                            continue outerLoop;
                    }
                    list.add(message);
                }

                if(list.size()>0) {
                    mMessageList.addAll(list);
                    Collections.sort(mMessageList, new Comparator<Message>() {
                        @Override
                        public int compare(Message o1, Message o2) {
                            return o2.getDate().compareTo(o1.getDate());
                        }
                    });
                    mMessageAdapter.notifyDataSetChanged();
                }
                list.clear();


                outerLoop:
                for(Message oldMessage:mMessageList)
                {
                    for(Message message: response)
                        if(message.getMessageId().equals(oldMessage.getMessageId()))
                            continue outerLoop;
                    list.add(oldMessage);
                }

                if(list.size()>0) {
                    mMessageList.removeAll(list);
                    Collections.sort(mMessageList, new Comparator<Message>() {
                        @Override
                        public int compare(Message o1, Message o2) {
                            return o2.getDate().compareTo(o1.getDate());
                        }
                    });
                    mMessageAdapter.notifyDataSetChanged();
                }
            }
        }, errorListener);

        request.setRetryPolicy(new DefaultRetryPolicy(
                Misc.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(request);
    }

    public void newMessage(final View view){
        progressDialog.setMessage(getResources().getString(R.string.message_progress));
        progressDialog.show();

        JSONObject jsonRequest = new JSONObject();
        try {
            TextView newMessageView =((View)(view.getParent().getParent())).findViewById(R.id.newMessage);
            jsonRequest.put("content", newMessageView.getText());
            jsonRequest.put("receiverId", mUserId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        VolleyJsonRequest request = new VolleyJsonRequest( Request.Method.POST,
                ConnectingURL.URL_Message,jsonRequest, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response.toString());
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("APIResponse", error.toString());
                Toast.makeText(ChatActivity.this, getResources().getString(R.string.message_wrong), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }){
            @Override
            public Map<String, String> getHeaders() {
                return Misc.getSecureHeaders(ChatActivity.this);
            }
        };

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
                    if (!ChatActivity.finished) {
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
        updateChat();
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

        super.onDestroy();
    }
}
