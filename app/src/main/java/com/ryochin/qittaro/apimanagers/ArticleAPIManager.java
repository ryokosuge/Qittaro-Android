/**
 * PACKAGE NAME com.ryochin.qittaro.apimanagers
 * CREATED BY kosugeryou
 * CREATED AT 2014/07/26
 */
package com.ryochin.qittaro.apimanagers;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ryochin.qittaro.utils.AppController;

public class ArticleAPIManager {

    private static final String TAG = ArticleAPIManager.class.getSimpleName();
    private final ArticleAPIManager self = this;
    private static final String API_URL = "https://qiita.com/api/v1/items";
    private static ArticleAPIManager instance;
    private int page;
    private boolean loading;

    public static ArticleAPIManager getInstance() {
        if (instance == null) {
            instance = new ArticleAPIManager();
        }
        return instance;
    }

    private ArticleAPIManager() {
        this.page = 1;
        this.loading = false;
    }

    public void reloadItems(final APIManagerListener listener) {
        if (this.loading) {
            return;
        }
        this.page = 1;
        listener.willStart();
        this.loading = true;
        StringRequest stringRequest = this.getRequest(this.page, listener);
        AppController.getInstance().addToRequestQueue(stringRequest, TAG);
    }

    public void addItems(final APIManagerListener listener) {
        if (this.loading) {
            return;
        }
        this.page++;
        listener.willStart();
        this.loading = true;
        StringRequest stringRequest = this.getRequest(this.page, listener);
        AppController.getInstance().addToRequestQueue(stringRequest, TAG);
    }

    public int getPage() {
        return this.page;
    }

    public boolean isLoading() {
        return this.loading;
    }

    public void cancel() {
        this.loading = false;
        AppController.getInstance().cancelPendingRequests(TAG);
    }

    private StringRequest getRequest(final int page, final APIManagerListener listener) {
        String url = API_URL + "?page=" + String.valueOf(page);
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "onResponse()");
                        self.loading = false;
                        listener.onCompleted(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "onErrorResponse()");
                        self.loading = false;
                        listener.onError();
                    }
                }
        );
        return stringRequest;
    }

}
