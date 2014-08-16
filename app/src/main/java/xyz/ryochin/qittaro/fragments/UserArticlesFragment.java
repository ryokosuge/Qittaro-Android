/**
 * PACKAGE NAME xyz.ryochin.qittaro.fragments
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/13
 */

package xyz.ryochin.qittaro.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import xyz.ryochin.qittaro.R;
import xyz.ryochin.qittaro.apimanagers.APIManagerListener;
import xyz.ryochin.qittaro.apimanagers.UserArticlesAPIManager;
import xyz.ryochin.qittaro.models.ArticleModel;
import xyz.ryochin.qittaro.utils.AppController;
import xyz.ryochin.qittaro.views.ArticleListView;

public class UserArticlesFragment extends Fragment implements ArticleListView.Listener {

    private static final String TAG = UserArticlesFragment.class.getSimpleName();
    private final UserArticlesFragment self = this;
    private static final String ARGS_URL_NAME_KEY = "urlName";

    private FragmentListener listener;
    private ArticleListView view;
    private String urlName;

    public static UserArticlesFragment newInstance(String urlName) {
        Bundle args = new Bundle();
        args.putString(ARGS_URL_NAME_KEY, urlName);
        UserArticlesFragment fragment = new UserArticlesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if ((activity instanceof FragmentListener)) {
            this.listener = (FragmentListener) activity;
        } else {
            throw new ClassCastException("Please implement the FragmentListener.");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.basic_list_view_layout, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle args = this.getArguments();
        this.urlName = args.getString(ARGS_URL_NAME_KEY);
        this.view = new ArticleListView(this.getActivity(), this.getView(), false, this);
        UserArticlesAPIManager.getInstance().getItems(this.urlName, this.getAPIManagerListener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        UserArticlesAPIManager.getInstance().cancel();
    }

    @Override
    public void onStart() {
        super.onStart();
        AppController.getInstance().sendView(TAG);
    }

    @Override
    public void onRefresh() {
        UserArticlesAPIManager.getInstance().reloadItems(this.urlName, this.reloadAPIManagerListener);
    }

    @Override
    public void onItemClicked(ArticleModel model) {
        this.listener.onItemSelected(model);
    }

    @Override
    public void onScrollEnd() {
        if (!UserArticlesAPIManager.getInstance().isMax()) {
            UserArticlesAPIManager.getInstance().addItems(this.addAPIManagerListener);
        }
    }

    private APIManagerListener<ArticleModel> getAPIManagerListener = new APIManagerListener<ArticleModel>() {

        @Override
        public void willStart() {
            self.view.setFullLoadingViewVisibility(View.VISIBLE);
            self.view.setSwipeRefreshVisibility(View.GONE);
        }

        @Override
        public void onCompleted(List<ArticleModel> items) {
            self.view.setItems(items);
            if (UserArticlesAPIManager.getInstance().isMax()) {
                self.view.setFooterLoadingViewVisibility(View.GONE);
            } else {
                self.view.setFooterLoadingViewVisibility(View.VISIBLE);
            }
            self.view.setSwipeRefreshVisibility(View.VISIBLE);
            self.view.setFullLoadingViewVisibility(View.GONE);
        }

        @Override
        public void onError() {
            self.view.setSwipeRefreshVisibility(View.VISIBLE);
            self.view.setFullLoadingViewVisibility(View.GONE);
        }
    };

    private APIManagerListener<ArticleModel> reloadAPIManagerListener = new APIManagerListener<ArticleModel>() {

        @Override
        public void willStart() {
            self.view.setRefresh(true);
        }

        @Override
        public void onCompleted(List<ArticleModel> items) {
            self.view.setItems(items);
            if (UserArticlesAPIManager.getInstance().isMax()) {
                self.view.setFooterLoadingViewVisibility(View.GONE);
            } else {
                self.view.setFooterLoadingViewVisibility(View.VISIBLE);
            }
            self.view.setRefresh(false);
        }

        @Override
        public void onError() {
            self.view.setRefresh(false);
        }
    };

    private APIManagerListener<ArticleModel> addAPIManagerListener = new APIManagerListener<ArticleModel>() {
        @Override
        public void willStart() {
            self.view.setFooterLoadingViewVisibility(View.VISIBLE);
        }

        @Override
        public void onCompleted(List<ArticleModel> items) {
            self.view.addItems(items);
            if (UserArticlesAPIManager.getInstance().isMax()) {
                self.view.setFooterLoadingViewVisibility(View.GONE);
            } else {
                self.view.setFooterLoadingViewVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onError() {
            self.view.setFooterLoadingViewVisibility(View.GONE);
        }
    };

}
