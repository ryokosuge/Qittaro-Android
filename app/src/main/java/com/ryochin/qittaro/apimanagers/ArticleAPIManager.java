/**
 * PACKAGE NAME com.ryochin.qittaro.apimanagers
 * CREATED BY kosugeryou
 * CREATED AT 2014/07/30
 */

package com.ryochin.qittaro.apimanagers;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ryochin.qittaro.models.ArticleDetailModel;
import com.ryochin.qittaro.utils.AppController;

import org.json.JSONException;
import org.json.JSONObject;

public class ArticleAPIManager {

    public interface ArticleAPIManagerListener {
        public void onCompleted(ArticleDetailModel model);
        public void onError();
    }

    private static final String TAG = ArticleAPIManager.class.getSimpleName();
    private final ArticleAPIManager self = this;
    private static final String API_URL = "https://qiita.com/api/v1/items";

    private static ArticleAPIManager instance;
    private boolean loading;
    private ArticleDetailModel item;

    public static ArticleAPIManager getInstance() {
        if (instance == null) {
            instance = new ArticleAPIManager();
        }
        return instance;
    }

    private ArticleAPIManager() {
        this.loading = false;
    }

    public void cancel() {
        this.loading = false;
        AppController.getInstance().cancelPendingRequests(TAG);
    }

    public void getItem(String articleUUID, String token, ArticleAPIManagerListener listener) {
        if (this.loading) {
            return ;
        }

        if (this.item != null && this.item.getUuid().equals(articleUUID)) {
            listener.onCompleted(this.item);
            return ;
        }

        this.loading = true;
        StringRequest stringRequest = this.getRequest(articleUUID, token, listener);
        AppController.getInstance().addToRequestQueue(stringRequest, TAG);
    }

    public boolean isStockedItem() {
        return this.item.isStocked();
    }

    public void unStockArticle(String token, final ArticleAPIManagerListener listener) {
        if (this.loading) {
            return;
        }

        if (this.item == null || token == null) {
            listener.onError();
        }

        String url = this.makeStockURL(this.item.getUuid(), token);
        StringRequest request = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, response);
                        self.item.setStocked(false);
                        listener.onCompleted(self.item);
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "VolleyError", error);
                        listener.onError();
                    }
                }
        );

        AppController.getInstance().addToRequestQueue(request, TAG);
    }

    public void stockArticle(String token, final ArticleAPIManagerListener listener) {
        if (this.loading) {
            return;
        }

        if (this.item == null || token == null) {
            listener.onError();
        }

        String url = this.makeStockURL(this.item.getUuid(), token);
        StringRequest request = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, response);
                        self.item.setStocked(true);
                        listener.onCompleted(self.item);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "VolleyError", error);
                        listener.onError();
                    }
                }
        );

        AppController.getInstance().addToRequestQueue(request, TAG);
    }

    private String makeStockURL(String articleUUID, String token) {
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(API_URL).append("/").append(articleUUID).append("/stock?token=").append(token);
        return urlBuilder.toString();
    }

    private StringRequest getRequest(String articleUUID, String token, final ArticleAPIManagerListener listener) {
        String url = this.makeURL(articleUUID, token);
        return new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        self.loading = false;
                        self.item = self.responseToItem(response);
                        if (self.item == null) {
                            listener.onError();
                        } else {
                            listener.onCompleted(self.item);
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

    private ArticleDetailModel responseToItem(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            return new ArticleDetailModel(jsonObject);
        } catch (JSONException e) {
            Log.e(TAG, "JSONException", e);
            return null;
        }
    }

    private String makeURL(String articleUUID, String token) {
        StringBuilder urlBuilder = new StringBuilder();

        urlBuilder.append(API_URL).append("/").append(articleUUID);
        if (token != null) {
            urlBuilder.append("?token=").append(token);
        }

        return urlBuilder.toString();
    }

}
