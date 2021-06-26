package com.palanim.botcontroller;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CheckTenSeconds {
    private static Context context;

    public CheckTenSeconds(Context context) {
        this.context = context;
    }
    public interface VolleyResponseListener {
        // public interface VolleyCallback {
        void onError(String message);




        void onResponse(String result);
    }


    // public void getESPData(VolleyResponseListener volleyResponseListener) {
    public void getESPDataTenS(final getWhatESPsays.VolleyResponseListener volleyResponseListener) {
        int ESPRes =0;

        RequestQueue queue = Volley.newRequestQueue(context);

        String url = "https://fast-crag-18702.herokuapp.com";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                String InitRes = " ";
                String res = " ";
                //Log.i("RESPONSE", response.toString());
                try {
                    JSONObject myResp = response.getJSONObject(1);
                    InitRes = myResp.getString("content");
                    Log.i("FromCheck", InitRes);
                    if (InitRes.charAt(0) == 'l' || InitRes.charAt(0)=='E') {
                        //    callback.onResponse(InitRes);
                        volleyResponseListener.onResponse(InitRes);
                        Log.i("FromCheck", InitRes);

                    }

                    Log.i("From Callback", InitRes);
                } catch (JSONException e) {
                    e.printStackTrace();
                    //  callback.onError("Wrong");
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("NOTHING NOTHING", "nothing");
            }
        });
        //  MySingleton.getInstance(context).addToRequestQueue(request);
        queue.add(request);

    }
}
