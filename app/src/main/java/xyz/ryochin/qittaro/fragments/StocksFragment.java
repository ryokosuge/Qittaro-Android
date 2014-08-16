/**
 * PACKAGE NAME xyz.ryochin.qittaro.fragments
 * CREATED BY kosugeryou
 * CREATED AT 2014/07/29
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
import xyz.ryochin.qittaro.apimanagers.StocksAPIManager;
import xyz.ryochin.qittaro.models.ArticleModel;
import xyz.ryochin.qittaro.utils.AppController;
import xyz.ryochin.qittaro.utils.AppSharedPreference;
import xyz.ryochin.qittaro.views.ArticleListView;

public class StocksFragment extends Fragment implements ArticleListView.Listener {

    private static final String TAG = StocksFragment.class.getSimpleName();
    private final StocksFragment self = this;

    private FragmentListener listener;
    private ArticleListView view;

    public static StocksFragment newInstance() {
        return new StocksFragment();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if ((activity instanceof FragmentListener)) {
            this.listener = (FragmentListener)activity;
        } else {
            throw new ClassCastException("activity が ArticlesFragmentListener を実装していません.");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.basic_list_view_layout, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.view = new ArticleListView(this.getActivity(), this.getView(), true, this);
        String token = AppSharedPreference.getToken(this.getActivity());
        StocksAPIManager.getInstance().getItems(token, this.getAPIManagerListener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        StocksAPIManager.getInstance().cancel();
    }

    @Override
    public void onStart() {
        super.onStart();
        AppController.getInstance().sendView(TAG);
    }

    @Override
    public void onPause() {
        super.onPause();
        this.view.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.view.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.view.destroy();
    }

    @Override
    public void onRefresh() {
        StocksAPIManager.getInstance().reloadItems(this.reloadAPIManagerListener);
    }

    @Override
    public void onItemClicked(ArticleModel model) {
        this.listener.onItemSelected(model);
    }

    @Override
    public void onScrollEnd() {
        if (!StocksAPIManager.getInstance().isMax()) {
            StocksAPIManager.getInstance().addItems(this.addAPIManagerListener);
        }
    }

    private APIManagerListener<ArticleModel> getAPIManagerListener = new APIManagerListener<ArticleModel>() {

        @Override
        public void willStart() {
            self.view.setSwipeRefreshVisibility(View.GONE);
            self.view.setFullLoadingViewVisibility(View.VISIBLE);
        }

        @Override
        public void onCompleted(List<ArticleModel> items) {
            self.view.setItems(items);
            if (StocksAPIManager.getInstance().isMax()) {
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
            self.view.setFooterLoadingViewVisibility(View.GONE);
        }
    };

    private APIManagerListener<ArticleModel> reloadAPIManagerListener = new APIManagerListener<ArticleModel>() {

        @Override
        public void willStart() {
            self.view.setRefresh(false);
        }

        @Override
        public void onCompleted(List<ArticleModel> items) {
            self.view.setItems(items);
            if (StocksAPIManager.getInstance().isMax()) {
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
            if (StocksAPIManager.getInstance().isMax()) {
                self.view.setFooterLoadingViewVisibility(View.GONE);
            } else {
                self.view.setFooterLoadingViewVisibility(View.VISIBLE);
            }
            self.view.setRefresh(false);
        }

        @Override
        public void onError() {
            self.view.setFooterLoadingViewVisibility(View.GONE);
        }
    };
}
