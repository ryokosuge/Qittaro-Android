/**
 * =====================================
 * ENCODE : UTF-8
 * CREATED AT 2014/08/19
 * CREATED BY kosugeryou
 * =====================================
 */
package xyz.ryochin.qittaro.login;

import java.util.HashMap;
import java.util.Map;

import xyz.ryochin.qittaro.api.OnFinishedListener;
import xyz.ryochin.qittaro.api.QiitaAPI;
import xyz.ryochin.qittaro.api.QiitaAPIImpl;
import xyz.ryochin.qittaro.requests.Request;

public class LoginInteractorImpl implements LoginInteractor {

    private static final String TAG = LoginInteractorImpl.class.getSimpleName();
    private final LoginInteractorImpl self = this;

    private static final int REQUEST_PARAM_COUNT = 2;
    private static final String REQUEST_PARAMS_URL_NAME_KEY = "url_name";
    private static final String REQUEST_PARAMS_PASSWORD_KEY = "password";
    private QiitaAPI qiitaAPI;

    public LoginInteractorImpl() {
        this.qiitaAPI = new QiitaAPIImpl();
    }

    @Override
    public void login(Request request, String urlName, String password, final OnLoginListener listener) {
        if (urlName == null || urlName.equals("")) {
            listener.onUserNameError();
            return;
        }

        if (password == null || password.equals("")) {
            listener.onPasswordError();
            return;
        }

        Map<String, String> params = new HashMap<String, String>(REQUEST_PARAM_COUNT);
        params.put(REQUEST_PARAMS_URL_NAME_KEY, urlName);
        params.put(REQUEST_PARAMS_PASSWORD_KEY, password);

        qiitaAPI.post(request, params, new OnFinishedListener() {
            @Override
            public void onFinished(String jsonResponse) {
                listener.onFinished(jsonResponse);
            }

            @Override
            public void onError(Exception exception) {
                listener.onAPIError(exception);
            }
        });
    }

    @Override
    public void getUserInfo(Request request, OnFinishedListener listener) {
        qiitaAPI.getItems(request, listener);
    }

    @Override
    public void cancel(Request request) {
        qiitaAPI.cancel(request);
    }
}
