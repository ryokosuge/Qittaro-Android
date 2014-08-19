
package xyz.ryochin.qittaro.articles;

import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import xyz.ryochin.qittaro.articles.models.ArticleModel;
import xyz.ryochin.qittaro.interactors.Interactor;
import xyz.ryochin.qittaro.interactors.InteractorImpl;
import xyz.ryochin.qittaro.interactors.OnFinishedListener;
import xyz.ryochin.qittaro.requests.APIRequest;

public class ArticlesPresenterImpl implements ArticlesPresenter {

    private static final String TAG = ArticlesPresenterImpl.class.getSimpleName();
    private final ArticlesPresenterImpl self = this;

    private ArticlesView view;
    private APIRequest request;
    private Interactor interactor;
    private List<ArticleModel> items;

    private int page;
    private static final int PER_PAGE = 20;
    private boolean loading;
    private boolean max;

    public ArticlesPresenterImpl(ArticlesView view, APIRequest request) {
        this.view = view;
        this.request = request;
        this.interactor = new InteractorImpl();
        this.items = new ArrayList<ArticleModel>();
    }

    @Override
    public void start() {
        this.loading = true;
        page = 1;
        request.setPage(page);
        request.setPerPage(PER_PAGE);
        this.view.showFullLoadingView();
        interactor.getItems(request, new OnFinishedListener() {
            @Override
            public void onFinished(String jsonResponse) {
                List<ArticleModel> items = convertModel(jsonResponse);
                self.items = items;
                if (self.max = isLimit(items.size())) {
                    self.view.hideListFooterLoadingView();
                }
                self.view.setItems(items);
                self.view.hideFullLoadingView();
                self.loading = false;
            }

            @Override
            public void onError(Exception exception) {
                self.view.hideFullLoadingView();
                self.view.showMessage("エラー", exception.getLocalizedMessage());
                self.loading = false;
            }
        });
    }

    @Override
    public void refresh() {
        self.loading = true;
        page = 1;
        request.setPage(page);
        request.setPerPage(PER_PAGE);
        view.showReloadLoadingView();
        interactor.getItems(request, new OnFinishedListener() {
            @Override
            public void onFinished(String jsonResponse) {
                List<ArticleModel> items = convertModel(jsonResponse);
                self.items = items;
                if (self.max = isLimit(items.size())) {
                    self.view.hideListFooterLoadingView();
                }
                self.view.setItems(items);
                self.view.hideReloadLoadingView();
                self.loading = false;
            }

            @Override
            public void onError(Exception exception) {
                self.view.hideFullLoadingView();
                self.view.showMessage("エラー", exception.getLocalizedMessage());
                self.loading = false;
            }
        });
    }

    @Override
    public void onItemClicked(int position) {
        ArticleModel model = items.get(position);
        view.navigateToArticleActivity(model.getUuid());
    }

    @Override
    public void onScroll(int totalItemCount, int firstVisibleItem, int visibleItemCount) {
        if (totalItemCount != 0 && totalItemCount == firstVisibleItem + visibleItemCount) {
            Log.e(TAG, "onScroll");
            Log.e(TAG, "items.size() = " + items.size());
            if (this.items.size() > 0 && !this.loading && !this.max) {
                addRequest();
            }
        }
    }

    private void addRequest() {
        this.loading = true;
        page++;
        request.setPage(page);
        view.showListFooterLoadingView();
        interactor.getItems(request, new OnFinishedListener() {
            @Override
            public void onFinished(String jsonResponse) {
                List<ArticleModel> items = convertModel(jsonResponse);
                if (self.max = isLimit(items.size())) {
                    self.view.hideListFooterLoadingView();
                }
                self.items.addAll(items);
                self.loading = false;
                self.view.addItems(items);
            }

            @Override
            public void onError(Exception exception) {
                self.view.hideListFooterLoadingView();
                self.view.showMessage("エラー", exception.getLocalizedMessage());
            }
        });
    }

    @Override
    public void destroyView() {
        interactor.cancel(request);
    }

    private static boolean isLimit(int itemSize) {
        return (itemSize < PER_PAGE);
    }

    private static List<ArticleModel> convertModel(String responseJson) {
        Gson gson = createGson();
        Type listTyp = new TypeToken<List<ArticleModel>>(){}.getType();
        return gson.fromJson(responseJson, listTyp);
    }

    private static Gson createGson() {
        return new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
    }
}
