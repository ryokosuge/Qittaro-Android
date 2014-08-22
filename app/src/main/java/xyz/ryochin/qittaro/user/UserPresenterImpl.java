/**
 * =====================================================
 * ENCODE : UTF-8
 * CREATED AT 14/08/22.
 * CREATED BY kosuge.
 * Copyright © Samurai Factory Inc. All rights reserved.
 * ===================================================== 
 */

package xyz.ryochin.qittaro.user;

import android.util.Log;

import xyz.ryochin.qittaro.models.UserModel;
import xyz.ryochin.qittaro.requests.APIRequest;
import xyz.ryochin.qittaro.requests.FollowUserRequest;

public class UserPresenterImpl implements UserPresenter {

    private static final String TAG = UserPresenterImpl.class.getSimpleName();
    private final UserPresenterImpl self = this;

    private APIRequest request;
    private UserInteractor interactor;
    private UserView view;

    public UserPresenterImpl(UserView view, String urlName) {
        this.view = view;
        this.request = new FollowUserRequest(urlName);
        this.interactor = new UserInteractorImpl();
    }

    @Override
    public void start() {
        view.setActionBarLoadingTitle();
        view.showFullLoadingView();
        interactor.getUserInfo(request, new UserInteractor.Listener() {
            @Override
            public void onCompleted(UserModel model) {
                view.setActionBarUserInfo(model.getUrlName(), model.getProfileImageUrl());
                view.setFragmentAdapter(model);
                view.hideFullLoadingView();
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "onError", e);
            }
        });
    }

    @Override
    public void destroyView() {
        interactor.cancel(request);
    }
}
