/**
 * PACKAGE NAME xyz.ryochin.qittaro.apimanagers
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/13
 */

package xyz.ryochin.qittaro.apimanagers;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import xyz.ryochin.qittaro.models.UserModel;
import xyz.ryochin.qittaro.utils.AppController;

public class UserInfoAPIManager {

    private static final String TAG = UserInfoAPIManager.class.getSimpleName();
    private final UserInfoAPIManager self = this;
    private static final String API_URL = "https://qiita.com/api/v1/users/";

    public interface Listener {
        public void willStart();
        public void onCompleted(UserModel model);
        public void onError();
    }

    private static UserInfoAPIManager instance;
    private boolean loading;
    private UserModel item;
    private String urlName;

    public static UserInfoAPIManager getInstance() {
        if (instance == null) {
            instance = new UserInfoAPIManager();
        }
        return instance;
    }

    private UserInfoAPIManager() {
        this.loading = false;
    }

    public void cancel() {
        AppController.getInstance().cancelPendingRequests(TAG);
    }

    public void getItem(String urlName, final Listener listener) {

        if (this.loading) {
            return;
        }

        listener.willStart();

        if (this.urlName != null && this.urlName.equals(urlName)) {
            listener.onCompleted(this.item);
            return;
        }

        this.urlName = urlName;

        String url = API_URL + urlName;
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            UserModel model = new UserModel(jsonObject);
                            if (model == null) {
                                listener.onError();
                            } else {
                                self.item = model;
                                listener.onCompleted(model);
                            }
                        } catch (JSONException e) {
                            listener.onError();
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
        AppController.getInstance().addToRequestQueue(request, TAG);
    }
}
