/**
 * PACKAGE NAME xyz.ryochin.qittaro.tags
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/22
 */

package xyz.ryochin.qittaro.tags;

import java.util.ArrayList;
import java.util.List;

import xyz.ryochin.qittaro.requests.APIRequest;
import xyz.ryochin.qittaro.models.TagModel;

public class TagsPresenterImpl implements TagsPresenter {

    private static final String TAG = TagsPresenterImpl.class.getSimpleName();
    private final TagsPresenterImpl self = this;

    private TagsView view;
    private APIRequest request;
    private TagsInteractor interactor;
    private List<TagModel> items;

    public TagsPresenterImpl(TagsView view, APIRequest request) {
        this.view = view;
        this.request = request;
        this.interactor = new TagsInteractorImpl();
        this.items = new ArrayList<TagModel>();
    }


    @Override
    public void start() {
        this.view.showFullLoadingView();
        interactor.getItems(request, new TagsInteractor.Listener() {
            @Override
            public void onCompleted(List<TagModel> items, boolean isMax) {
                self.items = items;
                if (isMax) {
                    self.view.hideListFooterLoadingView();
                }
                self.view.setItems(items);
                self.view.hideFullLoadingView();
            }

            @Override
            public void onError(Exception e) {
            }
        });
    }

    @Override
    public void refresh() {
        view.showReloadLoadingView();
        interactor.getItems(request, new TagsInteractor.Listener() {
            @Override
            public void onCompleted(List<TagModel> items, boolean isMax) {
                if (isMax) {
                    self.view.hideListFooterLoadingView();
                }
                self.items = items;
                self.view.setItems(items);
                self.view.hideReloadLoadingView();
            }

            @Override
            public void onError(Exception e) {
            }
        });
    }

    @Override
    public void onItemClicked(int position) {
        TagModel model = items.get(position);
        view.navigateToTagActivity(model.getUrlName(), model.getName(), model.getIconUrl());
    }

    @Override
    public void onScroll(int totalItemCount, int firstVisibleItem, int visibleItemCount) {
        if (totalItemCount != 0 && totalItemCount == firstVisibleItem + visibleItemCount) {
            if (this.items.size() > 0 && !interactor.isLoading() && !interactor.isMax()) {
                addRequest();
            }
        }
    }

    @Override
    public void destroyView() {
        interactor.cancel(request);
    }

    private void addRequest() {
        view.showListFooterLoadingView();
        interactor.addItems(request, new TagsInteractor.Listener() {
            @Override
            public void onCompleted(List<TagModel> items, boolean isMax) {
                if (isMax) {
                    self.view.hideListFooterLoadingView();
                }
                self.items.addAll(items);
                self.view.addItems(items);
            }

            @Override
            public void onError(Exception e) {
            }
        });
    }

}
