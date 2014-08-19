
package xyz.ryochin.qittaro.interactors;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import xyz.ryochin.qittaro.requests.Request;
import xyz.ryochin.qittaro.utils.AppController;

public class InteractorImpl implements Interactor {

    private static final String TAG = InteractorImpl.class.getSimpleName();
    private final InteractorImpl self = this;

    @Override
    public void getItems(Request request, final OnFinishedListener listener) {
        String urlStr = request.toURLString();
        Log.e(TAG, "getItems()");
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

    @Override
    public void cancel(Request request) {
        AppController.getInstance().cancelPendingRequests(request.getTag());
    }
}
