/**
 * PACKAGE NAME com.ryochin.qittaro.apimanagers
 * CREATED BY kosugeryou
 * CREATED AT 2014/07/27
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

public class TagArticleAPIManager {

    private static final String TAG = TagArticleAPIManager.class.getSimpleName();
    private final TagArticleAPIManager self = this;

    private static final String API_URL = "https://qiita.com/api/v1/tags";
    private static TagArticleAPIManager instance;
    private int page;
    private boolean loading;
    private String tagURL;
    private List<ArticleModel> items;


    public static TagArticleAPIManager getInstance() {
        if (instance == null) {
            instance = new TagArticleAPIManager();
        }
        return instance;
    }

    private TagArticleAPIManager() {
        this.page = 1;
        this.loading = false;
        this.items = new ArrayList<ArticleModel>();
    }

    public List<ArticleModel> getItems() {
        return this.items;
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

    public void reloadItems(final String tagURL, final APIManagerListener<ArticleModel> listener) {
        if (this.loading) {
            return;
        }
        this.page = 1;
        this.loading = true;
        this.tagURL = tagURL;
        this.items.clear();
        StringRequest stringRequest = this.getRequest(this.tagURL, this.page, listener);
        AppController.getInstance().addToRequestQueue(stringRequest, TAG);
    }

    public void reloadItems(final APIManagerListener<ArticleModel> listener) {
        if (this.loading || this.tagURL == null) {
            return;
        }
        this.page = 1;
        this.loading = true;
        this.items.clear();
        StringRequest stringRequest = this.getRequest(this.tagURL, this.page, listener);
        AppController.getInstance().addToRequestQueue(stringRequest, TAG);
    }

    public void addItems(final APIManagerListener<ArticleModel> listener) {
        if (this.loading || this.tagURL == null) {
            return ;
        }
        this.page ++;
        this.loading = true;
        StringRequest stringRequest = this.getRequest(this.tagURL, this.page, listener);
        AppController.getInstance().addToRequestQueue(stringRequest, TAG);
    }

    private StringRequest getRequest(final String tagURL, final int page, final APIManagerListener<ArticleModel> listener) {
        String url = API_URL + "/" + tagURL + "/items?page=" + page;
        return new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        self.loading = false;
                        List<ArticleModel> items = self.responseToItems(response);
                        if (items == null) {
                            listener.onError();
                        } else {
                            self.items.addAll(items);
                            listener.onCompleted(items);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        self.loading = false;
                    }
                }
        );
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
