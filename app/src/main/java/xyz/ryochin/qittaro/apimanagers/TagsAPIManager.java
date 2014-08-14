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

import xyz.ryochin.qittaro.models.TagModel;
import xyz.ryochin.qittaro.utils.AppController;

public class TagsAPIManager {
    private static final String TAG = TagsAPIManager.class.getSimpleName();
    private final TagsAPIManager self = this;
    private static final String API_URL = "https://qiita.com/api/v1/tags?page=%d&per_page=%d";
    private static final int PER_PAGE = 20;

    private static TagsAPIManager instance;
    private List<TagModel> items;
    private int page;
    private boolean loading;
    private boolean max;

    public static TagsAPIManager getInstance() {
        if (instance == null) {
            instance = new TagsAPIManager();
        }
        return instance;
    }

    private TagsAPIManager() {
        this.page = 1;
        this.loading = false;
        this.items = new ArrayList<TagModel>();
    }

    public void cancel() {
        this.loading = false;
        AppController.getInstance().cancelPendingRequests(TAG);
    }

    public boolean isMax() {
        return this.max;
    }

    public void getItems(final APIManagerListener<TagModel> listener) {
        if (this.loading) {
            return ;
        }

        listener.willStart();

        if (this.items != null && this.items.size() > 0) {
            listener.onCompleted(this.items);
        }

        this.loading = true;
        this.max = false;
        this.page = 1;

        StringRequest stringRequest = this.getRequest(this.page, listener);
        AppController.getInstance().addToRequestQueue(stringRequest, TAG);
    }

    public void reloadItems(final APIManagerListener<TagModel> listener) {
        if (this.loading) {
            return;
        }

        listener.willStart();

        this.loading = true;
        this.max = false;
        this.page = 1;

        StringRequest stringRequest = this.getRequest(this.page, listener);
        AppController.getInstance().addToRequestQueue(stringRequest, TAG);
    }

    public void addItems(final APIManagerListener<TagModel> listener) {
        if (this.loading) {
            return;
        }

        listener.willStart();

        this.loading = true;
        this.page ++;
        StringRequest stringRequest = this.getRequest(this.page, listener);
        AppController.getInstance().addToRequestQueue(stringRequest, TAG);
    }

    private StringRequest getRequest(int page, final APIManagerListener<TagModel> listener) {
        String url = String.format(API_URL, page, PER_PAGE);
        return new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        List<TagModel> items = self.responseToItems(response);
                        self.loading = false;
                        if (items == null) {
                            listener.onError();
                        } else {
                            if (items.size() < PER_PAGE) {
                                self.max = true;
                            }
                            listener.onCompleted(items);
                            self.items.addAll(items);
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

    private List<TagModel> responseToItems(String response) {
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(response);
            int responseArrayCount = jsonArray.length();
            List<TagModel> items = new ArrayList<TagModel>(responseArrayCount);
            for (int i = 0; i < responseArrayCount; i ++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                TagModel tagModel = new TagModel(jsonObject);
                items.add(tagModel);
            }
            return items;
        } catch (JSONException e) {
            Log.e(TAG, "JSONException ::", e);
            return null;
        }
    }
}
