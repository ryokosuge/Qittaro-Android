/**
 * PACKAGE NAME xyz.ryochin.qittaro.articles.followingusers
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/20
 */
package xyz.ryochin.qittaro.followingusers;

import java.util.ArrayList;
import java.util.List;

import xyz.ryochin.qittaro.models.FollowingUserModel;
import xyz.ryochin.qittaro.requests.APIRequest;

public class FollowingUsersPresenterImpl implements FollowingUsersPresenter {

    private static final String TAG = FollowingUsersPresenterImpl.class.getSimpleName();
    private final FollowingUsersPresenterImpl self = this;

    private FollowingUsersView view;
    private APIRequest request;
    private FollowingUsersInteractor interactor;
    private List<FollowingUserModel> items;

    public FollowingUsersPresenterImpl(FollowingUsersView view, APIRequest request) {
        this.view = view;
        this.request = request;
        this.interactor = new FollowingUsersInteractorImpl();
        this.items = new ArrayList<FollowingUserModel>();
    }

    @Override
    public void start() {
        view.showFullLoadingView();
        interactor.getItems(request, new FollowingUsersInteractor.Listener() {
            @Override
            public void onCompleted(List<FollowingUserModel> items, boolean isMax) {
                self.items = items;
                if (isMax) {
                    self.view.hideListFooterLoadingView();
                }
                view.setItems(items);
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
        interactor.getItems(request, new FollowingUsersInteractor.Listener() {
            @Override
            public void onCompleted(List<FollowingUserModel> items, boolean isMax) {
                self.items = items;
                if (isMax) {
                    self.view.hideListFooterLoadingView();
                }
                self.view.setItems(items);
                self.view.hideReloadLoadingView();
            }

            @Override
            public void onError(Exception e) {
                self.view.hideFullLoadingView();
                self.view.showMessage("エラー", e.getLocalizedMessage());
            }
        });
    }

    @Override
    public void onItemClicked(int position) {
        FollowingUserModel model = items.get(position);
        view.navigateToUserActivity(model.getUrlName());
    }

    @Override
    public void onScroll(int totalItemCount, int firstVisibleItem, int visibleItemCount) {
        if (totalItemCount != 0 && totalItemCount == firstVisibleItem + visibleItemCount) {
            if (this.items.size() > 0 && !interactor.isMax() && !interactor.isLoading()) {
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
        interactor.addItems(request, new FollowingUsersInteractor.Listener() {
            @Override
            public void onCompleted(List<FollowingUserModel> items, boolean isMax) {
                self.items.addAll(items);
                self.view.addItems(items);
                if (isMax) {
                    self.view.hideListFooterLoadingView();
                }
            }

            @Override
            public void onError(Exception e) {
                self.view.hideListFooterLoadingView();
                self.view.showMessage("エラー", e.getLocalizedMessage());
            }
        });
    }

}
