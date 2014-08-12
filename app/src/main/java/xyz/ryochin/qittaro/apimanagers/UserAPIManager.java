/**
 * PACKAGE NAME xyz.ryochin.qittaro.apimanagers
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/12
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

public class UserAPIManager {

    public interface Listener {
        public void willStart();
        public void onCompleted(UserModel model);
        public void onError();

    }

    private static final String TAG = UserAPIManager.class.getSimpleName();
    private final UserAPIManager self = this;
    private static final String API_URL = "https://qiita.com/api/v1/user";

    private static UserAPIManager instatnce;
    private boolean loading;

    public static UserAPIManager getInstatnce() {
        if (instatnce == null) {
            instatnce = new UserAPIManager();
        }
        return instatnce;
    }

    private UserAPIManager() {
        this.loading = false;
    }

    public void cancel() {
        this.loading = false;
        AppController.getInstance().cancelPendingRequests(TAG);
    }

    public void getItem(String token, final Listener listener) {
        if (this.loading) {
            return ;
        }

        String url = API_URL + "?token=" + token;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            UserModel model = new UserModel(jsonObject);
                            listener.onCompleted(model);
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
        AppController.getInstance().addToRequestQueue(stringRequest, TAG);
    }
}
