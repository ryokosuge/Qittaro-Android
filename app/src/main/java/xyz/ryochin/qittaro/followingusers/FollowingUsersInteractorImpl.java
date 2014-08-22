
/**
 * PACKAGE NAME xyz.ryochin.qittaro.articles.followingusers
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/21
 */
package xyz.ryochin.qittaro.followingusers;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import xyz.ryochin.qittaro.api.OnFinishedListener;
import xyz.ryochin.qittaro.api.QiitaAPI;
import xyz.ryochin.qittaro.models.FollowingUserModel;
import xyz.ryochin.qittaro.requests.APIRequest;

public class FollowingUsersInteractorImpl implements FollowingUsersInteractor {

    private static final String TAG = FollowingUsersInteractorImpl.class.getSimpleName();
    private final FollowingUsersInteractorImpl self = this;

    private int page = 1;
    private static final int PER_PAGE = 20;
    private boolean loading;
    private boolean max;

    public FollowingUsersInteractorImpl() {
        this.page = 1;
        this.loading = false;
        this.max = false;
    }

    @Override
    public void getItems(APIRequest request, final Listener listener) {
        if (loading) {
            return;
        }

        loading = true;
        max = false;
        page = 1;

        request.setPerPage(PER_PAGE);
        request.setPage(page);

        QiitaAPI.get(request, new OnFinishedListener() {
            @Override
            public void onFinished(String jsonResponse) {
                self.loading = false;
                List<FollowingUserModel> items = convertModel(jsonResponse);
                self.max = isMax(items.size());
                listener.onCompleted(items, self.max);
            }

            @Override
            public void onError(Exception exception) {
                self.loading = false;
                listener.onError(exception);
            }
        });
    }

    @Override
    public void addItems(APIRequest request, final Listener listener) {
        if (loading) {
            return;
        }

        loading = true;
        page++;
        request.setPage(page);

        QiitaAPI.get(request, new OnFinishedListener() {
            @Override
            public void onFinished(String jsonResponse) {
                self.loading = false;
                List<FollowingUserModel> items = convertModel(jsonResponse);
                self.max = isMax(items.size());
                listener.onCompleted(items, self.max);
            }

            @Override
            public void onError(Exception exception) {
                self.loading = false;
                listener.onError(exception);
            }
        });
    }

    @Override
    public void cancel(APIRequest request) {
        QiitaAPI.cancel(request);
    }

    @Override
    public boolean isMax() {
        return max;
    }

    @Override
    public boolean isLoading() {
        return loading;
    }

    private static boolean isMax(int itemSize) {
        return (itemSize < PER_PAGE);
    }

    private static List<FollowingUserModel> convertModel(String responseJson) {
        Gson gson = createGson();
        Type listType = new TypeToken<List<FollowingUserModel>>(){}.getType();
        return gson.fromJson(responseJson, listType);
    }

    private static Gson createGson() {
        return new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
    }
}
