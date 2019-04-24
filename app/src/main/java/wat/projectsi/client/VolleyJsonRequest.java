package wat.projectsi.client;

import android.support.annotation.Nullable;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

public class VolleyJsonRequest extends JsonObjectRequest {
    protected VolleyJsonRequest(int method, String url, @Nullable JSONObject jsonRequest, Response.Listener<JSONObject> listener, @Nullable Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        if(response.data.length == 0){
            byte[] responseData;
            responseData = "{}".getBytes(StandardCharsets.UTF_8);
            response = new NetworkResponse(response.statusCode, responseData, response.headers, response.notModified);
        }
        return super.parseNetworkResponse(response);
    }
}
