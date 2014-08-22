/**
 * PACKAGE NAME xyz.ryochin.qittaro.login
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/21
 */
package xyz.ryochin.qittaro.login;

import android.content.Context;
import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import xyz.ryochin.qittaro.R;
import xyz.ryochin.qittaro.api.OnFinishedListener;
import xyz.ryochin.qittaro.models.LoginResultModel;
import xyz.ryochin.qittaro.requests.APIRequest;
import xyz.ryochin.qittaro.requests.LoginRequest;
import xyz.ryochin.qittaro.requests.UserRequest;
import xyz.ryochin.qittaro.models.UserModel;
import xyz.ryochin.qittaro.utils.AppSharedPreference;

public class LoginPresenterImpl implements LoginPresenter {

    private static final String TAG = LoginPresenterImpl.class.getSimpleName();
    private final LoginPresenterImpl self = this;

    private LoginView view;
    private Context context;
    private LoginInteractor interactor;

    public LoginPresenterImpl(LoginView view, Context context) {
        this.view = view;
        this.context = context;
        this.interactor = new LoginInteractorImpl();
    }

    @Override
    public void validateCredentials(String urlName, String password) {
        APIRequest request = new LoginRequest();
        this.view.showProgress();
        this.interactor.login(request, urlName, password, loginListener);
    }

    private OnLoginListener loginListener = new OnLoginListener() {
        @Override
        public void onUserNameError() {
            Log.e(TAG, "onUserNameError()");
            self.view.hideProgress();
            String title = self.context.getString(R.string.login_error_title);
            String message = self.context.getString(R.string.login_error_user_name_text);
            self.view.showMessage(title, message);
        }

        @Override
        public void onPasswordError() {
            self.view.hideProgress();
            String title = self.context.getString(R.string.login_error_title);
            String message = self.context.getString(R.string.login_error_password_text);
            self.view.showMessage(title, message);
            Log.e(TAG, "onPasswordError()");
        }

        @Override
        public void onAPIError(Exception e) {
            Log.e(TAG, "onAPIError()", e);
            self.view.hideProgress();
            String title = self.context.getString(R.string.login_error_title);
            String message = self.context.getString(R.string.login_error_message);
            self.view.showMessage(title, message);
        }

        @Override
        public void onFinished(String jsonResponse) {
            Gson gson = createGson();
            LoginResultModel model = gson.fromJson(jsonResponse, LoginResultModel.class);
            getUserInfo(model.getToken());
        }
    };

    private void getUserInfo(String token) {
        storeToken(self.context, token);
        APIRequest request = new UserRequest(token);
        interactor.getUserInfo(request, new OnFinishedListener() {
            @Override
            public void onFinished(String jsonResponse) {
                Gson gson = createGson();
                UserModel userModel = gson.fromJson(jsonResponse, UserModel.class);
                storeUserInfo(self.context, userModel);
                view.hideProgress();
                view.navigateToHome();
            }

            @Override
            public void onError(Exception exception) {
                Log.e(TAG, "onError", exception);
            }
        });
    }

    private static Gson createGson() {
        return new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
    }

    private static void storeToken(Context context, String token) {
        AppSharedPreference.setToken(context, token);
    }

    private static void storeUserInfo(Context context, UserModel model) {
        AppSharedPreference.setProfileImageURL(context, model.getProfileImageUrl());
        AppSharedPreference.setURLName(context, model.getUrlName());
    }
}
