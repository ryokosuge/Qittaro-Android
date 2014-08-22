/**
 * PACKAGE NAME xyz.ryochin.qittaro.user
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/22
 */

package xyz.ryochin.qittaro.user;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import xyz.ryochin.qittaro.api.OnFinishedListener;
import xyz.ryochin.qittaro.api.QiitaAPI;
import xyz.ryochin.qittaro.models.UserModel;
import xyz.ryochin.qittaro.requests.APIRequest;

public class UserInteractorImpl implements UserInteractor {

    private static final String TAG = UserInteractorImpl.class.getSimpleName();
    private final UserInteractorImpl self = this;

    @Override
    public void getUserInfo(APIRequest request, final Listener listener) {
        QiitaAPI.get(request, new OnFinishedListener() {
            @Override
            public void onFinished(String jsonResponse) {
                UserModel model = convertModel(jsonResponse);
                listener.onCompleted(model);
            }

            @Override
            public void onError(Exception exception) {
                listener.onError(exception);
            }
        });
    }

    @Override
    public void cancel(APIRequest request) {
        QiitaAPI.cancel(request);
    }


    private static UserModel convertModel(String responseJson) {
        Gson gson = createGson();
        return gson.fromJson(responseJson, UserModel.class);
    }

    private static Gson createGson() {
        return new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
    }
}
