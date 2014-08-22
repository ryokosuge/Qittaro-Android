/**
 * PACKAGE NAME xyz.ryochin.qittaro.api
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/20
 */

package xyz.ryochin.qittaro.api;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.Map;

import xyz.ryochin.qittaro.requests.Request;
import xyz.ryochin.qittaro.utils.AppController;

public class QiitaAPI {

    private static final String TAG = QiitaAPI.class.getSimpleName();
    private final QiitaAPI self = this;

    public static void get(Request request, final OnFinishedListener listener) {
        String urlStr = request.toURLString();
        Log.e(TAG, "get()");
        Log.e(TAG, "URL = " + urlStr);
        StringRequest req = new StringRequest(com.android.volley.Request.Method.GET,
                urlStr,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        listener.onFinished(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onError(error);
                    }
                }
        );
        AppController.getInstance().addToRequestQueue(req, request.getTag());
    }

    public static void post(Request request, final Map<String, String> params, final OnFinishedListener listener) {
        String urlStr = request.toURLString();
        StringRequest req = new StringRequest(com.android.volley.Request.Method.POST, urlStr,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        listener.onFinished(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onError(error);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(req, request.getTag());
    }

    public static void put(Request request, final OnFinishedListener listener) {
        String urlStr = request.toURLString();
        StringRequest req = new StringRequest(com.android.volley.Request.Method.PUT, urlStr,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        listener.onFinished(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onError(error);
                    }
                }
        );
        AppController.getInstance().addToRequestQueue(req, request.getTag());
    }

    public static void delete(Request request, final OnFinishedListener listener) {
        String urlStr = request.toURLString();
        StringRequest req = new StringRequest(com.android.volley.Request.Method.DELETE, urlStr,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        listener.onFinished(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onError(error);
                    }
                }
        );
        AppController.getInstance().addToRequestQueue(req, request.getTag());
    }

    public static void cancel(Request request) {
        AppController.getInstance().cancelPendingRequests(request.getTag());
    }
}
