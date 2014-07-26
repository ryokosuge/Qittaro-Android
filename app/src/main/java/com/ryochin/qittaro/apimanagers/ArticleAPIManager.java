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

public class ArticleAPIManager {

    private static final String TAG = ArticleAPIManager.class.getSimpleName();
    private final ArticleAPIManager self = this;

    private static final String API_URL = "https://qiita.com/api/v1/items";

    private static ArticleAPIManager instance;

    private List<ArticleModel> articles;
    private int page;

    public static ArticleAPIManager getInstance() {
        if (instance == null) {
            instance = new ArticleAPIManager();
        }

        return instance;
    }

    private ArticleAPIManager() {
        this.articles = new ArrayList<ArticleModel>();
        this.page = 1;
    }

    public int getCount() {
        return this.articles.size();
    }

    public ArticleModel getItem(int index) {
        return this.articles.get(index);
    }

    public void reloadItems(final APIManagerListener listener) {
        this.page = 1;
        listener.willStart();
        StringRequest stringRequest = this.getRequest(this.page, listener);
        AppController.getInstance().addToRequestQueue(stringRequest, TAG);
    }

    public void addItems(final APIManagerListener listener) {
        this.page++;
        listener.willStart();
        StringRequest stringRequest = this.getRequest(this.page, listener);
        AppController.getInstance().addToRequestQueue(stringRequest, TAG);
    }

    public void cancel() {
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
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            int responseArrayCount = jsonArray.length();
                            for (int i = 0; i < responseArrayCount; i ++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                ArticleModel articleModel = new ArticleModel(jsonObject);
                                self.articles.add(articleModel);
                            }
                            listener.onCompleted();
                        } catch (JSONException e) {
                            Log.e(TAG, "JSONException()", e);
                            listener.onError();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "onErrorResponse()");
                        listener.onError();
                    }
                }
        );
        return stringRequest;
    }

}
