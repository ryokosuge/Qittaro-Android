/**
 * =====================================================
 * ENCODE : UTF-8
 * CREATED AT 14/08/13.
 * CREATED BY kosuge.
 * Copyright © Samurai Factory Inc. All rights reserved.
 * ===================================================== 
 */

package xyz.ryochin.qittaro.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import xyz.ryochin.qittaro.R;
import xyz.ryochin.qittaro.adapters.ArticleAdapter;
import xyz.ryochin.qittaro.apimanagers.APIManagerListener;
import xyz.ryochin.qittaro.apimanagers.UserStocksAPIManager;
import xyz.ryochin.qittaro.models.ArticleModel;
import xyz.ryochin.qittaro.utils.AppController;

public class UserStocksFragment extends Fragment implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener {

    private static final String TAG = UserStocksFragment.class.getSimpleName();
    private final UserStocksFragment self = this;

    private static final String ARGS_URL_NAME_KEY = "urlName";

    private FragmentListener listener;
    private ArticleAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View footerLoadingView;
    private String urlName;

    public static UserStocksFragment newInstance(String urlName) {
        Bundle args = new Bundle();
        args.putString(ARGS_URL_NAME_KEY, urlName);
        UserStocksFragment fragment = new UserStocksFragment();
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
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle args = this.getArguments();
        this.urlName = args.getString(ARGS_URL_NAME_KEY);

        ListView listView = (ListView) this.getView().findViewById(R.id.user_article_list_view);
        this.swipeRefreshLayout = (SwipeRefreshLayout)this.getView().findViewById(R.id.user_article_swipe_refresh);
        this.adapter = new ArticleAdapter(this.getActivity());
        this.swipeRefreshLayout.setColorSchemeResources(
                R.color.app_first_green_color,
                R.color.app_second_green_color,
                R.color.app_third_green_color,
                R.color.app_fourth_green_color
        );
        this.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                UserStocksAPIManager.getInstance().reloadItems(self.urlName, self.reloadAPIManagerListener);
            }
        });
        listView.addFooterView(this.getFooterLoadingView());
        listView.setOnItemClickListener(this);
        listView.setAdapter(this.adapter);
        listView.setOnScrollListener(this);
        UserStocksAPIManager.getInstance().getItems(this.urlName, this.getAPIManagerListener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        UserStocksAPIManager.getInstance().cancel();
    }

    @Override
    public void onStart() {
        super.onStart();
        AppController.getInstance().sendView(TAG);
    }

    private APIManagerListener<ArticleModel> getAPIManagerListener = new APIManagerListener<ArticleModel>() {

        @Override
        public void willStart() {
            self.swipeRefreshLayout.setVisibility(View.GONE);
            self.hideFooterLoadingView();
            self.showFullLoadingView();
        }

        @Override
        public void onCompleted(List<ArticleModel> items) {
            self.adapter.setItems(items);
            self.adapter.notifyDataSetChanged();
            if (UserStocksAPIManager.getInstance().isMax()) {
                self.hideFooterLoadingView();
            } else {
                self.showFooterLoadingView();
            }
            self.swipeRefreshLayout.setVisibility(View.VISIBLE);
            self.hideFullLoadingView();
        }

        @Override
        public void onError() {
            self.hideFullLoadingView();
        }
    };

    private APIManagerListener<ArticleModel> reloadAPIManagerListener = new APIManagerListener<ArticleModel>() {

        @Override
        public void willStart() {
            self.hideFooterLoadingView();
        }

        @Override
        public void onCompleted(List<ArticleModel> items) {
            self.adapter.setItems(items);
            self.adapter.notifyDataSetChanged();
            self.swipeRefreshLayout.setRefreshing(false);
            if (UserStocksAPIManager.getInstance().isMax()) {
                self.hideFooterLoadingView();
            } else {
                self.showFooterLoadingView();
            }
        }

        @Override
        public void onError() {
            self.swipeRefreshLayout.setRefreshing(false);
            self.hideFullLoadingView();
        }
    };

    private APIManagerListener<ArticleModel> addAPIManagerListener = new APIManagerListener<ArticleModel>() {
        @Override
        public void willStart() {
        }

        @Override
        public void onCompleted(List<ArticleModel> items) {
            self.adapter.addItems(items);
            self.adapter.notifyDataSetChanged();
            if (UserStocksAPIManager.getInstance().isMax()) {
                self.hideFooterLoadingView();
            } else {
                self.showFooterLoadingView();
            }
            self.hideFullLoadingView();
        }

        @Override
        public void onError() {
            self.hideFullLoadingView();
        }
    };
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (!UserStocksAPIManager.getInstance().isMax()) {
            if (totalItemCount != 0 && totalItemCount == firstVisibleItem + visibleItemCount) {
                Log.e(TAG, "HOGE");
                UserStocksAPIManager.getInstance().addItems(this.addAPIManagerListener);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ArticleModel articleModel = (ArticleModel)this.adapter.getItem(position);
        this.listener.onItemSelected(articleModel);
    }

    private View getFooterLoadingView() {
        if (this.footerLoadingView == null) {
            this.footerLoadingView = this.getActivity()
                    .getLayoutInflater().inflate(R.layout.list_item_footer_loading, null);
        }
        return this.footerLoadingView;
    }

    private void showFooterLoadingView() {
        this.getFooterLoadingView().findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
    }

    private void hideFooterLoadingView() {
        this.getFooterLoadingView().findViewById(R.id.progressBar).setVisibility(View.GONE);
    }

    private void showFullLoadingView() {
        View fullLoadingView = this.getView().findViewById(R.id.user_loading_layout);
        fullLoadingView.setVisibility(View.VISIBLE);
    }

    private void hideFullLoadingView() {
        View fullLoadingView = this.getView().findViewById(R.id.user_loading_layout);
        fullLoadingView.setVisibility(View.GONE);
    }
}
