/**
 * PACKAGE NAME xyz.ryochin.qittaro.tags
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/22
 */

package xyz.ryochin.qittaro.tags;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import xyz.ryochin.qittaro.api.OnFinishedListener;
import xyz.ryochin.qittaro.api.QiitaAPI;
import xyz.ryochin.qittaro.requests.APIRequest;
import xyz.ryochin.qittaro.models.TagModel;

public class TagsInteractorImpl implements TagsInteractor {

    private static final String TAG = TagsInteractorImpl.class.getSimpleName();
    private final TagsInteractorImpl self = this;

    private int page;
    private static final int PER_PAGE = 20;
    private boolean loading = false;
    private boolean max = false;

    public TagsInteractorImpl() {
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

        request.setPage(page);
        request.setPerPage(PER_PAGE);

        QiitaAPI.get(request, new OnFinishedListener() {
            @Override
            public void onFinished(String jsonResponse) {
                self.loading = false;
                List<TagModel> items = convertModel(jsonResponse);
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
        page ++;

        QiitaAPI.get(request, new OnFinishedListener() {
            @Override
            public void onFinished(String jsonResponse) {
                self.loading = false;
                List<TagModel> items = convertModel(jsonResponse);
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

    private static List<TagModel> convertModel(String responseJson) {
        Gson gson = createGson();
        Type listType = new TypeToken<List<TagModel>>(){}.getType();
        return gson.fromJson(responseJson, listType);
    }

    private static Gson createGson() {
        return new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
    }
}
