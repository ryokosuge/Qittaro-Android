/**
 * PACKAGE NAME xyz.ryochin.qittaro.apimanagers
 * CREATED BY kosugeryou
 * CREATED AT 2014/07/27
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

public class TagArticlesAPIManager {

    private static final String TAG = TagArticlesAPIManager.class.getSimpleName();
    private final TagArticlesAPIManager self = this;

    private static final String API_URL = "https://qiita.com/api/v1/tags";
    private static final int PER_PAGE = 20;

    private static TagArticlesAPIManager instance;
    private int page;
    private boolean loading;
    private boolean max;
    private String tagURLName;
    private List<ArticleModel> items;

    public static TagArticlesAPIManager getInstance() {
        if (instance == null) {
            instance = new TagArticlesAPIManager();
        }
        return instance;
    }

    private TagArticlesAPIManager() {
        this.page = 1;
        this.loading = false;
        this.max = false;
        this.items = new ArrayList<ArticleModel>();
    }

    public void cancel(String tagURLName) {
        this.loading = false;
        AppController.getInstance().cancelPendingRequests(TAG + ":" + this.tagURLName);
    }

    public boolean isMax() {
        return this.max;
    }

    public void reloadItems(String tagURLName, final APIManagerListener<ArticleModel> listener) {
        if (this.loading) {
            return ;
        }

        listener.willStart();
        this.page = 1;
        this.loading = true;
        this.max = false;
        this.tagURLName = tagURLName;
        StringRequest stringRequest = this.getRequest(this.tagURLName, this.page, listener);
        AppController.getInstance().addToRequestQueue(stringRequest, TAG + ":" + this.tagURLName);
    }

    public void addItems(final APIManagerListener<ArticleModel> listener) {
        if (this.loading || this.tagURLName == null) {
            return ;
        }
        listener.willStart();
        this.page ++;
        this.loading = true;
        StringRequest stringRequest = this.getRequest(this.tagURLName, this.page, listener);
        AppController.getInstance().addToRequestQueue(stringRequest, TAG);
    }

    private StringRequest getRequest(final String tagURL, final int page, final APIManagerListener<ArticleModel> listener) {
        String url = API_URL + "/" + tagURL + "/items?page=" + page + "&per_page=" + PER_PAGE;
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
