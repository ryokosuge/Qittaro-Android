/**
 * PACKAGE NAME com.ryochin.qittaro.apimanagers
 * CREATED BY kosugeryou
 * CREATED AT 2014/07/28
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

public class StockAPIManager {

    private static final String TAG = StockAPIManager.class.getSimpleName();
    private final StockAPIManager self = this;

    private static final String API_URL = "https://qiita.com/api/v1/stocks";
    private static final int PER_PAGE = 20;

    private static StockAPIManager instance;
    private int page;
    private boolean loading;
    private boolean max;
    private List<ArticleModel> items;
    private String token;

    public static StockAPIManager getInstance() {
        if (instance == null) {
            instance = new StockAPIManager();
        }
        return instance;
    }

    private StockAPIManager() {
        this.page = 1;
        this.loading = false;
        this.token = null;
        this.items = new ArrayList<ArticleModel>();
    }

    public void cancel() {
        this.loading = false;
        AppController.getInstance().cancelPendingRequests(TAG);
    }

    public boolean isMax() {
        return this.max;
    }

    public void getItems(final String token, final APIManagerListener<ArticleModel> listener) {
        if (this.loading) {
            return ;
        }

        if (this.token != null && this.token.equals(token) && this.items.size() > 0) {
            listener.onCompleted(this.items);
            return ;
        }

        this.page = 1;
        this.token = token;
        this.loading = true;
        this.max = false;

        StringRequest request = this.getRequest(this.token, this.page, listener);
        AppController.getInstance().addToRequestQueue(request, TAG);
    }

    public void reloadItems(final APIManagerListener<ArticleModel> listener) {
        if (this.loading) {
            return;
        }

        if (this.token == null) {
            listener.onError();
            return;
        }

        this.page = 1;
        this.loading = true;
        this.max = false;

        StringRequest request = this.getRequest(this.token, this.page, listener);
        AppController.getInstance().addToRequestQueue(request, TAG);

    }

    public void addItems(final APIManagerListener<ArticleModel> listener) {
        if (this.loading) {
            return ;
        }

        if (this.token == null) {
            listener.onError();
            return ;
        }

        this.page ++;
        this.loading = true;

        StringRequest request = this.getRequest(this.token, this.page, listener);
        AppController.getInstance().addToRequestQueue(request, TAG);
    }

    private StringRequest getRequest(final String token, final int page, final APIManagerListener<ArticleModel> listener) {
        String url = API_URL + "?token=" + token + "&page=" + page + "&per_page=" + PER_PAGE;
        return new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
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
                        self.loading = false;
                        listener.onError();
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
