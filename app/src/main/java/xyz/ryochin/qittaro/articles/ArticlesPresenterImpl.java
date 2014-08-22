/**
 * PACKAGE NAME xyz.ryochin.qittaro.articles
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/20
 */
package xyz.ryochin.qittaro.articles;

import java.util.ArrayList;
import java.util.List;

import xyz.ryochin.qittaro.models.ArticleModel;
import xyz.ryochin.qittaro.requests.APIRequest;

public class ArticlesPresenterImpl implements ArticlesPresenter {

    private static final String TAG = ArticlesPresenterImpl.class.getSimpleName();
    private final ArticlesPresenterImpl self = this;

    private ArticlesView view;
    private APIRequest request;
    private List<ArticleModel> items;
    private ArticlesInteractor interactor;

    public ArticlesPresenterImpl(ArticlesView view, APIRequest request) {
        this.view = view;
        this.request = request;
        this.items = new ArrayList<ArticleModel>();
        this.interactor = new ArticlesInteractorImpl();
    }

    @Override
    public void start() {
        view.showFullLoadingView();
        interactor.getItems(request, new ArticlesInteractor.Listener() {
            @Override
            public void onCompleted(List<ArticleModel> items, boolean isMax) {
                if (isMax) {
                    view.hideListFooterLoadingView();
                }
                view.setItems(items);
                self.items = items;
                view.hideFullLoadingView();
            }

            @Override
            public void onError(Exception e) {
                self.view.hideFullLoadingView();
                self.view.showMessage("エラー", e.getLocalizedMessage());
            }
        });
    }

    @Override
    public void refresh() {
        view.showReloadLoadingView();
        interactor.getItems(request, new ArticlesInteractor.Listener() {
            @Override
            public void onCompleted(List<ArticleModel> items, boolean isMax) {
                if (isMax) {
                    view.hideListFooterLoadingView();
                }
                view.setItems(items);
                self.items = items;
                view.hideReloadLoadingView();
            }

            @Override
            public void onError(Exception e) {
                view.hideReloadLoadingView();
                view.showMessage("エラー", e.getLocalizedMessage());
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
            if (items.size() > 0 && !interactor.isLoading() && !interactor.isMax()) {
                addRequest();
            }
        }
    }

    @Override
    public String getRequestTag() {
        return request.getTag();
    }

    private void addRequest() {
        view.showListFooterLoadingView();
        interactor.addItems(request, new ArticlesInteractor.Listener() {
            @Override
            public void onCompleted(List<ArticleModel> items, boolean isMax) {
                if (isMax) {
                    view.hideListFooterLoadingView();
                }
                self.items.addAll(items);
                view.addItems(items);
            }

            @Override
            public void onError(Exception e) {
                view.hideListFooterLoadingView();
                view.showMessage("エラー", e.getLocalizedMessage());
            }
        });
    }

    @Override
    public void destroyView() {
        interactor.cancel(request);
    }
}
