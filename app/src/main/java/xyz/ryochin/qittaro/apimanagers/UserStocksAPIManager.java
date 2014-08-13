/**
 * =====================================================
 * ENCODE : UTF-8
 * CREATED AT 14/08/13.
 * CREATED BY kosuge.
 * Copyright © Samurai Factory Inc. All rights reserved.
 * ===================================================== 
 */

package xyz.ryochin.qittaro.apimanagers;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import xyz.ryochin.qittaro.models.ArticleModel;
import xyz.ryochin.qittaro.utils.AppController;

public class UserStocksAPIManager {
    private static final String TAG = UserStocksAPIManager.class.getSimpleName();
    private final UserStocksAPIManager self = this;
    private static final String API_URL = "https://qiita.com/api/v1/users/%s/stocks?page=%d&per_page=%d";
    private static final int PER_PAGE = 20;

    private static UserStocksAPIManager instance;
    private int page;
    private String urlName;
    private boolean loading;
    private boolean max;
    private List<ArticleModel> items;

    public static UserStocksAPIManager getInstance() {
        if (instance == null) {
            instance = new UserStocksAPIManager();
        }
        return instance;
    }

    private UserStocksAPIManager() {
        this.page = 1;
        this.urlName = "";
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

    public void getItems(String urlName, APIManagerListener<ArticleModel> listener) {
        if (this.loading) {
            return ;
        }

        listener.willStart();

        if (this.urlName != null && this.urlName.equals(urlName) && this.items.size() > 0) {
            listener.onCompleted(this.items);
            return;
        }

        this.page = 1;
        this.loading = true;
        this.max = false;
        this.urlName = urlName;
        this.items.clear();

        StringRequest request = this.getRequest(this.urlName, this.page, listener);
        AppController.getInstance().addToRequestQueue(request, TAG);
    }

    public void reloadItems(String urlName, APIManagerListener<ArticleModel> listener) {
        if (this.loading) {
            return ;
        }

        listener.willStart();

        this.page = 1;
        this.loading = true;
        this.max = false;
        this.urlName = urlName;
        this.items.clear();

        StringRequest request = this.getRequest(this.urlName, this.page, listener);
        AppController.getInstance().addToRequestQueue(request, TAG);
    }

    public void addItems(APIManagerListener<ArticleModel> listener) {
        if (this.loading || this.max) {
            return ;
        }

        listener.willStart();

        this.page ++;
        this.loading = true;

        StringRequest request = this.getRequest(this.urlName, this.page, listener);
        AppController.getInstance().addToRequestQueue(request, TAG);
    }

    private StringRequest getRequest(String urlName, int page, final APIManagerListener<ArticleModel> listener) {
        String url = String.format(API_URL, urlName, page, PER_PAGE);
        return new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
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
