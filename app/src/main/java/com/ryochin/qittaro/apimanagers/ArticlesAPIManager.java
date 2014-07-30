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
import com.ryochin.qittaro.models.ArticleModel;
import com.ryochin.qittaro.utils.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ArticlesAPIManager {

    private static final String TAG = ArticlesAPIManager.class.getSimpleName();
    private final ArticlesAPIManager self = this;
    private static final String API_URL = "https://qiita.com/api/v1/items";
    private static final int PER_PAGE = 20;

    private static ArticlesAPIManager instance;
    private int page;
    private boolean loading;
    private boolean max;
    private List<ArticleModel> items;

    public static ArticlesAPIManager getInstance() {
        if (instance == null) {
            instance = new ArticlesAPIManager();
        }
        return instance;
    }

    private ArticlesAPIManager() {
        this.page = 1;
        this.loading = false;
        this.max = false;
        this.items = new ArrayList<ArticleModel>();
    }

    public boolean isMax() {
        return this.max;
    }

    public void cancel() {
        this.loading = false;
        AppController.getInstance().cancelPendingRequests(TAG);
    }


    public void getItems(final APIManagerListener<ArticleModel> listener) {
        if (this.loading) {
            return;
        }

        if (!this.items.isEmpty()) {
            listener.onCompleted(items);
            return ;
        }

        this.page = 1;
        this.loading = true;
        this.max = false;
        StringRequest stringRequest = this.getRequest(this.page, listener);
        AppController.getInstance().addToRequestQueue(stringRequest, TAG);
    }

    public void reloadItems(final APIManagerListener<ArticleModel> listener) {
        if (this.loading) {
            return;
        }
        this.page = 1;
        this.loading = true;
        this.max = false;
        StringRequest stringRequest = this.getRequest(this.page, listener);
        AppController.getInstance().addToRequestQueue(stringRequest, TAG);
    }

    public void addItems(final APIManagerListener<ArticleModel> listener) {
        if (this.loading) {
            return;
        }
        this.page++;
        this.loading = true;
        StringRequest stringRequest = this.getRequest(this.page, listener);
        AppController.getInstance().addToRequestQueue(stringRequest, TAG);
    }

    private StringRequest getRequest(final int page, final APIManagerListener<ArticleModel> listener) {
        String url = API_URL + "?page=" + page + "&per_page=" + PER_PAGE;
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "onResponse()");
                        self.loading = false;
                        List<ArticleModel> items = self.responseToItems(response);
                        if (items == null) {
                            listener.onError();
                        } else {
                            if (items.size() < PER_PAGE) {
                                self.max = true;
                            }
                            self.items.addAll(items);
                            listener.onCompleted(items);
                        }
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


    private List<ArticleModel> responseToItems(String response) {
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(response);
            int responseArrayCount = jsonArray.length();
            List<ArticleModel> items = new ArrayList<ArticleModel>(responseArrayCount);
            for (int i = 0; i < responseArrayCount; i ++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                ArticleModel articleModel = new ArticleModel(jsonObject);
                items.add(articleModel);
            }
            return items;
        } catch (JSONException e) {
            Log.e(TAG, "JSONException ::", e);
            return null;
        }
    }

}
