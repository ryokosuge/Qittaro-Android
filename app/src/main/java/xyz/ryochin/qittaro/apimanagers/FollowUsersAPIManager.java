/**
 * PACKAGE NAME xyz.ryochin.qittaro.adapters
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/13
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

import xyz.ryochin.qittaro.models.FollowUserModel;
import xyz.ryochin.qittaro.utils.AppController;

public class FollowUsersAPIManager {

    private static final String TAG = FollowUsersAPIManager.class.getSimpleName();
    private final FollowUsersAPIManager self = this;
    private static final String API_URL = "https://qiita.com/api/v1/users/%s/following_users?page=%d&per_page=%d";
    private static final int PER_PAGE = 20;

    private static FollowUsersAPIManager instance;
    private List<FollowUserModel> items;
    private int page;
    private boolean loading;
    private boolean max;
    private String urlName;

    public static FollowUsersAPIManager getInstance() {
        if (instance == null) {
            instance = new FollowUsersAPIManager();
        }
        return instance;
    }

    private FollowUsersAPIManager() {
        this.page = 1;
        this.loading = false;
        this.items = new ArrayList<FollowUserModel>();
    }

    public void cancel() {
        this.loading = false;
        AppController.getInstance().cancelPendingRequests(TAG);
    }

    public boolean isMax() {
        return this.max;
    }

    public void getItems(String urlName, final APIManagerListener<FollowUserModel> listener) {
        if (this.loading) {
            return ;
        }

        listener.willStart();

        if (this.urlName != null && this.urlName.equals(urlName) && this.items.size() > 0) {
            listener.onCompleted(this.items);
        }

        this.loading = true;
        this.page = 1;
        this.max = false;
        this.urlName = urlName;

        StringRequest request = this.getRequest(this.urlName, this.page, listener);
        AppController.getInstance().addToRequestQueue(request, TAG);
    }

    public void reloadItems(String urlName, final APIManagerListener<FollowUserModel> listener) {
        if (this.loading) {
            return;
        }

        listener.willStart();

        this.loading = true;
        this.page = 1;
        this.max = false;
        this.urlName = urlName;
        this.items.clear();

        StringRequest request = this.getRequest(this.urlName, this.page, listener);
        AppController.getInstance().addToRequestQueue(request, TAG);
    }

    public void addItems(final APIManagerListener<FollowUserModel> listener) {
        if (this.loading) {
            return;
        }

        listener.willStart();

        if (this.urlName == null || this.urlName.equals("")) {
            listener.onError();
        }

        this.loading = true;
        this.page ++;

        StringRequest request = this.getRequest(this.urlName, this.page, listener);
        AppController.getInstance().addToRequestQueue(request, TAG);
    }

    private StringRequest getRequest(String urlName, int page, final APIManagerListener<FollowUserModel> listener) {
        String url = String.format(API_URL, urlName, page, PER_PAGE);
        Log.e(TAG, "URL = " + url);
        return new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        List<FollowUserModel> items = self.responseToItems(response);
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
                        listener.onError();
                    }
                }
        );
    }

    private List<FollowUserModel> responseToItems(String response) {
        try {
            JSONArray jsonArray = new JSONArray(response);
            int count = jsonArray.length();
            List<FollowUserModel> items = new ArrayList<FollowUserModel>(count);
            for (int i = 0; i < count; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                FollowUserModel model = new FollowUserModel(jsonObject);
                items.add(model);
            }
            return items;
        } catch (JSONException e) {
            Log.e(TAG, "JSONException", e);
            return null;
        }
    }
}
