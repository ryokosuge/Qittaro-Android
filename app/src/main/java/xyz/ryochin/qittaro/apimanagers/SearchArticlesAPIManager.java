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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class SearchArticlesAPIManager {

    private static final String TAG = SearchArticlesAPIManager.class.getSimpleName();
    private final SearchArticlesAPIManager self = this;
    private static final String API_URL = "https://qiita.com/api/v1/search";
    private static final int PER_PAGE = 20;

    private static SearchArticlesAPIManager instance;
    private int page;
    private boolean loading;
    private boolean max;
    private String searchWord;
    private boolean searchInStocked;
    private String token;
    private List<ArticleModel> items;

    public static SearchArticlesAPIManager getInstance() {
        if (instance == null) {
            instance = new SearchArticlesAPIManager();
        }
        return instance;
    }

    private SearchArticlesAPIManager() {
        this.page = 1;
        this.loading = false;
        this.max = false;
        this.searchWord = "";
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

    public void getItems(String searchWord, boolean searchInStocked, String token, APIManagerListener<ArticleModel> listener) {
        Log.e(TAG, "getItems()");
        if (this.loading) {
            return ;
        }

        if (!this.items.isEmpty() && this.searchWord.equals(searchWord) && this.searchInStocked == searchInStocked) {
            listener.onCompleted(this.items);
            return ;
        }

        listener.willStart();

        this.page = 1;
        this.loading = true;
        this.max = false;
        this.searchWord = searchWord;
        this.searchInStocked = searchInStocked;
        this.token = token;

        if (this.searchWord == null || this.searchWord.equals("")) {
            listener.onError();
            return ;
        }

        try {
            StringRequest stringRequest = this.getRequest(this.searchWord, this.page, this.searchInStocked, this.token, listener);
            AppController.getInstance().addToRequestQueue(stringRequest, TAG);
        } catch (UnsupportedEncodingException e) {
            listener.onError();
        }
    }

    public void reloadItems(final APIManagerListener<ArticleModel> listener) {
        Log.e(TAG, "reloadItems()");
        if (this.loading) {
            return;
        }

        if (this.searchWord.equals("")) {
            listener.onError();
            return;
        }

        listener.willStart();

        this.page = 1;
        this.loading = true;
        this.max = false;
        try {
            StringRequest stringRequest = this.getRequest(this.searchWord, this.page, this.searchInStocked, token, listener);
            AppController.getInstance().addToRequestQueue(stringRequest, TAG);
        } catch (UnsupportedEncodingException e) {
            listener.onError();
        }
    }

    public void addItems(final APIManagerListener<ArticleModel> listener) {
        Log.e(TAG, "addItems()");
        if (this.loading) {
            return;
        }

        listener.willStart();

        this.page++;
        this.loading = true;
        StringRequest stringRequest = null;
        try {
            stringRequest = this.getRequest(this.searchWord, this.page, this.searchInStocked, token, listener);
            AppController.getInstance().addToRequestQueue(stringRequest, TAG);
        } catch (UnsupportedEncodingException e) {
            listener.onError();
        }
    }

    private StringRequest getRequest(String searchWord, int page, boolean searchInStocked, String token, final APIManagerListener<ArticleModel> listener) throws UnsupportedEncodingException {
        Log.e(TAG, "searchWord [" + searchWord + "]");
        String encodeSearchWord = URLEncoder.encode(searchWord, "UTF-8");
        StringBuilder urlStrBuilder = new StringBuilder();
        urlStrBuilder.append(API_URL);
        urlStrBuilder.append("?q=");
        urlStrBuilder.append(encodeSearchWord);
        urlStrBuilder.append("&page=");
        urlStrBuilder.append(page);
        urlStrBuilder.append("&per_page=");
        urlStrBuilder.append(PER_PAGE);
        if (searchInStocked && token != null) {
            urlStrBuilder.append("&stocked=");
            urlStrBuilder.append(searchInStocked);
            urlStrBuilder.append("&token=");
            urlStrBuilder.append(token);
        }

        String url = urlStrBuilder.toString();
        Log.e(TAG, "URL = " + url);
        return new StringRequest(Request.Method.GET,
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
