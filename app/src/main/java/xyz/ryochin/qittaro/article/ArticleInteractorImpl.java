/**
 * PACKAGE NAME xyz.ryochin.qittaro.article
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/21
 */
package xyz.ryochin.qittaro.article;

import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import xyz.ryochin.qittaro.api.OnFinishedListener;
import xyz.ryochin.qittaro.api.QiitaAPI;
import xyz.ryochin.qittaro.models.ArticleDetailModel;
import xyz.ryochin.qittaro.requests.APIRequest;

public class ArticleInteractorImpl implements ArticleInteractor {

    private static final String TAG = ArticleInteractorImpl.class.getSimpleName();
    private final ArticleInteractorImpl self = this;

    private boolean loading;

    public ArticleInteractorImpl() {
        this.loading = false;
    }

    @Override
    public void getArticle(APIRequest request, final Listener listener) {
        this.loading = true;
        QiitaAPI.get(request, new OnFinishedListener() {
            @Override
            public void onFinished(String jsonResponse) {
                ArticleDetailModel model = convertModel(jsonResponse);
                listener.onCompleted(model);
                self.loading = false;
            }

            @Override
            public void onError(Exception e) {
                listener.onError(e);
            }
        });
    }

    @Override
    public void putStocked(APIRequest request, final Listener listener) {
        this.loading = true;
        QiitaAPI.put(request, new OnFinishedListener() {
            @Override
            public void onFinished(String jsonResponse) {
                Log.e(TAG, jsonResponse);
                listener.onCompleted(null);
                self.loading = false;
            }

            @Override
            public void onError(Exception exception) {
                listener.onError(exception);
                self.loading = false;
            }
        });
    }

    @Override
    public void deleteStocked(APIRequest request, final Listener listener) {
        this.loading = true;
        QiitaAPI.delete(request, new OnFinishedListener() {
            @Override
            public void onFinished(String jsonResponse) {
                Log.e(TAG, jsonResponse);
                listener.onCompleted(null);
                self.loading = false;
            }

            @Override
            public void onError(Exception exception) {
                listener.onError(exception);
                self.loading = false;
            }
        });
    }

    @Override
    public boolean isLoading() {
        return loading;
    }

    @Override
    public void cancel(APIRequest request) {
        QiitaAPI.cancel(request);
    }

    private static ArticleDetailModel convertModel(String responseJson) {
        Gson gson = createGson();
        return gson.fromJson(responseJson, ArticleDetailModel.class);
    }

    private static Gson createGson() {
        return new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
    }
}
