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
import xyz.ryochin.qittaro.models.ArticleModel;
import xyz.ryochin.qittaro.utils.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TagArticlesAPIManager {

    private static final String TAG = TagArticlesAPIManager.class.getSimpleName();
    private final TagArticlesAPIManager self = this;

    private static final String API_URL = "https://qiita.com/api/v1/tags";
    private static final int PER_PAGE = 20;

    private static TagArticlesAPIManager instance;
    private int page;
    private boolean loading;
    private boolean max;
    private String tagURL;
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

    public void cancel() {
        this.loading = false;
        AppController.getInstance().cancelPendingRequests(TAG);
    }

    public boolean isMax() {
        return this.max;
    }

    public void getItems(final String tagURL, final APIManagerListener<ArticleModel> listener) {
        if (this.loading) {
            return;
        }

        if (this.tagURL != null && this.tagURL.equals(tagURL)) {
            if (this.items.size() > 0) {
                listener.onCompleted(this.items);
                return;
            }
        }

        listener.willStart();

        this.page = 1;
        this.loading = true;
        this.max = false;
        this.tagURL = tagURL;
        this.items.clear();
        StringRequest stringRequest = this.getRequest(this.tagURL, this.page, listener);
        AppController.getInstance().addToRequestQueue(stringRequest, TAG);
    }

    public void reloadItems(final APIManagerListener<ArticleModel> listener) {
        if (this.loading || this.tagURL == null) {
            return;
        }

        listener.willStart();
        this.page = 1;
        this.loading = true;
        this.max = false;
        this.items.clear();
        StringRequest stringRequest = this.getRequest(this.tagURL, this.page, listener);
        AppController.getInstance().addToRequestQueue(stringRequest, TAG);
    }

    public void addItems(final APIManagerListener<ArticleModel> listener) {
        if (this.loading || this.tagURL == null) {
            return ;
        }
        listener.willStart();
        this.page ++;
        this.loading = true;
        StringRequest stringRequest = this.getRequest(this.tagURL, this.page, listener);
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
