
/**
 * PACKAGE NAME xyz.ryochin.qittaro.articles.search
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/21
 */
package xyz.ryochin.qittaro.articles.search;

import java.util.ArrayList;
import java.util.List;

import xyz.ryochin.qittaro.articles.ArticlesInteractor;
import xyz.ryochin.qittaro.articles.ArticlesInteractorImpl;
import xyz.ryochin.qittaro.models.ArticleModel;
import xyz.ryochin.qittaro.requests.SearchRequest;

public class SearchPresenterImpl implements SearchPresenter {
    private static final String TAG = SearchPresenterImpl.class.getSimpleName();
    private final SearchPresenterImpl self = this;

    private SearchView view;
    private SearchRequest request;
    private List<ArticleModel> items;
    private ArticlesInteractor interactor;
    private String searchWord;

    public SearchPresenterImpl(SearchView view, boolean searchInStocks, String token) {
        this.view = view;
        this.request = new SearchRequest(searchInStocks, token);
        this.interactor = new ArticlesInteractorImpl();
        this.items = new ArrayList<ArticleModel>();
    }

    public SearchPresenterImpl(SearchView view) {
        this.view = view;
        this.request = new SearchRequest();
        this.interactor = new ArticlesInteractorImpl();
        this.items = new ArrayList<ArticleModel>();
    }

    @Override
    public void onItemClicked(int position) {
        ArticleModel model = this.items.get(position);
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
            }
        });
    }

    @Override
    public void changeSearchALL() {
        request.setSearchInStocks(false);
        if (searchWord == null) {
            return;
        }
        view.showFullLoadingView();
        interactor.getItems(request, new ArticlesInteractor.Listener() {
            @Override
            public void onCompleted(List<ArticleModel> items, boolean isMax) {
                if (items.size() < 1) {
                    view.showNoArticleMessage(searchWord);
                } else {
                    if (isMax) {
                        view.hideListFooterLoadingView();
                    }
                    view.setItems(items);
                    self.items = items;
                }
                view.hideFullLoadingView();
            }

            @Override
            public void onError(Exception e) {
                view.hideFullLoadingView();
            }
        });
    }

    @Override
    public void changeSearchStocks(String token) {
        request.setSearchInStocks(true);
        if (searchWord == null) {
            return;
        }
        view.showFullLoadingView();
        interactor.getItems(request, new ArticlesInteractor.Listener() {
            @Override
            public void onCompleted(List<ArticleModel> items, boolean isMax) {
                if (items.size() < 1) {
                    view.showNoArticleMessage(searchWord);
                } else {
                    if (isMax) {
                        view.hideListFooterLoadingView();
                    }
                    view.setItems(items);
                    self.items = items;
                }
                view.hideFullLoadingView();
            }

            @Override
            public void onError(Exception e) {
                view.hideFullLoadingView();
            }
        });
    }

    @Override
    public void queryTextSubmit(final String searchWord) {
        this.searchWord = searchWord;
        request.setSearchWord(searchWord);
        view.setActionBarTitle(searchWord);
        view.showFullLoadingView();
        interactor.getItems(request, new ArticlesInteractor.Listener() {
            @Override
            public void onCompleted(List<ArticleModel> items, boolean isMax) {
                view.hideFullLoadingView();
                if (items.size() < 1) {
                    view.showNoArticleMessage(searchWord);
                    self.items.clear();
                } else {
                    if (isMax) {
                        view.hideListFooterLoadingView();
                    }
                    view.setItems(items);
                    self.items = items;
                }
            }

            @Override
            public void onError(Exception e) {
                view.hideFullLoadingView();
            }
        });
    }

    @Override
    public void destroyView() {
        interactor.cancel(request);
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
            }
        });
    }
}
