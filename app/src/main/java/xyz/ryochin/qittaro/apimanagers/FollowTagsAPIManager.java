/**
 * PACKAGE NAME xyz.ryochin.qittaro.adapters
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/11
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

public class FollowTagsAPIManager {

    private static final String TAG = FollowTagsAPIManager.class.getSimpleName();
    private final FollowTagsAPIManager self = this;
    private static final String API_URL = "https://qiita.com/api/v1/users";
    private static final int PER_PAGE = 20;

    private static FollowTagsAPIManager instance;
    private List<TagModel> items;
    private int page;
    private boolean loading;
    private boolean max;
    private String urlName;

    public static FollowTagsAPIManager getInstance() {
        if (instance == null) {
            instance = new FollowTagsAPIManager();
        }
        return instance;
    }

    private FollowTagsAPIManager() {
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

    public int getItemSize() {
        return this.items.size();
    }

    public void getItems(String urlName, final APIManagerListener<TagModel> listener) {
        if (this.loading) {
            return ;
        }

        if (this.urlName != null && this.urlName.equals(urlName) && this.items.size() > 0) {
            listener.onCompleted(this.items);
        }

        listener.willStart();

        this.loading = true;
        this.page = 1;
        this.max = false;
        this.urlName = urlName;

        StringRequest stringRequest = this.getRequest(this.urlName, this.page, listener);
        AppController.getInstance().addToRequestQueue(stringRequest, TAG);
    }

    public void reloadItems(String urlName, final APIManagerListener<TagModel> listener) {
        if (this.loading) {
            return ;
        }

        listener.willStart();

        this.loading = true;
        this.page = 1;
        this.urlName = urlName;
        this.items.clear();

        StringRequest stringRequest = this.getRequest(this.urlName, this.page, listener);
        AppController.getInstance().addToRequestQueue(stringRequest, TAG);
    }

    public void addItems(final APIManagerListener<TagModel> listener) {
        if (this.loading) {
            return ;
        }

        if (this.urlName == null || this.urlName.equals("")) {
            listener.onError();
            return ;
        }

        listener.willStart();

        this.loading = true;
        this.page ++;

        StringRequest stringRequest = this.getRequest(this.urlName, this.page, listener);
        AppController.getInstance().addToRequestQueue(stringRequest, TAG);
    }

    private StringRequest getRequest(String urlName, int page, final APIManagerListener<TagModel> listener) {
        String url = API_URL + "/" + urlName + "/following_tags?page=" + page + "&per_page=" + PER_PAGE;
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
            int jsonArrayCount = jsonArray.length();
            List<TagModel> items = new ArrayList<TagModel>(jsonArrayCount);
            for (int i = 0; i < jsonArrayCount; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                TagModel tagModel = new TagModel(jsonObject);
                items.add(tagModel);
            }
            return items;
        } catch (JSONException e) {
            Log.e(TAG, "JSONException :: ", e);
            return null;
        }
    }
}
