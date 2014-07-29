/**
 * PACKAGE NAME com.ryochin.qittaro.fragments
 * CREATED BY kosugeryou
 * CREATED AT 2014/07/26
 */
package com.ryochin.qittaro.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ryochin.qittaro.R;
import com.ryochin.qittaro.adapters.ArticleAdapter;
import com.ryochin.qittaro.apimanagers.APIManagerListener;
import com.ryochin.qittaro.apimanagers.ArticleAPIManager;
import com.ryochin.qittaro.models.ArticleModel;

import java.util.List;

public class ArticlesFragment extends Fragment implements AbsListView.OnScrollListener, AdapterView.OnItemClickListener{

    private static final String TAG = ArticlesFragment.class.getSimpleName();
    private final ArticlesFragment self = this;

    private FragmentListener listener;
    private ListView listView;
    private ArticleAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View footerLoadingView;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if ((activity instanceof FragmentListener)) {
            this.listener = (FragmentListener) activity;
        } else {
            throw new ClassCastException("activity が ArticlesFragmentListener を実装していません.");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_article, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.listView = (ListView)this.getView().findViewById(R.id.article_list_view);
        this.swipeRefreshLayout = (SwipeRefreshLayout)this.getView().findViewById(R.id.article_swipe_refresh);
        this.adapter = new ArticleAdapter(this.getActivity());
        this.swipeRefreshLayout.setColorSchemeColors(
                R.color.app_main_green_color,
                R.color.app_main_bleu_color,
                R.color.app_main_orange_color,
                R.color.app_main_red_color
        );
        this.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ArticleAPIManager.getInstance().reloadItems(self.reloadAPIManagerListener);
            }
        });
        this.listView.addFooterView(this.getFooterLoadingView());
        this.listView.setOnItemClickListener(this);
        this.listView.setAdapter(this.adapter);
        ArticleAPIManager.getInstance().getItems(this.getAPIManagerListener);
        this.listView.setOnScrollListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ArticleAPIManager.getInstance().cancel();
    }

    private APIManagerListener<ArticleModel> getAPIManagerListener = new APIManagerListener<ArticleModel>() {
        @Override
        public void onCompleted(List<ArticleModel> items) {
            self.adapter.setItems(items);
            self.adapter.notifyDataSetChanged();
            if (ArticleAPIManager.getInstance().isMax()) {
                self.listView.removeFooterView(self.getFooterLoadingView());
            }
        }

        @Override
        public void onError() {
        }
    };

    private APIManagerListener<ArticleModel> reloadAPIManagerListener = new APIManagerListener<ArticleModel>() {
        @Override
        public void onCompleted(List<ArticleModel> items) {
            self.adapter.setItems(items);
            self.adapter.notifyDataSetChanged();
            self.swipeRefreshLayout.setRefreshing(false);
            if (ArticleAPIManager.getInstance().isMax()) {
                self.listView.removeFooterView(self.getFooterLoadingView());
            }
        }

        @Override
        public void onError() {
            self.swipeRefreshLayout.setRefreshing(false);
        }
    };

    private APIManagerListener<ArticleModel> addAPIManagerListener = new APIManagerListener<ArticleModel>() {
        @Override
        public void onCompleted(List<ArticleModel> items) {
            self.adapter.addItems(items);
            self.adapter.notifyDataSetChanged();
            if (ArticleAPIManager.getInstance().isMax()) {
                self.listView.removeFooterView(self.getFooterLoadingView());
            }
        }

        @Override
        public void onError() {
        }
    };
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (!ArticleAPIManager.getInstance().isMax()) {
            if (totalItemCount != 0 && totalItemCount == firstVisibleItem + visibleItemCount) {
                ArticleAPIManager.getInstance().addItems(this.addAPIManagerListener);
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
                    .getLayoutInflater().inflate(R.layout.fragment_article_loading, null);
        }
        return this.footerLoadingView;
    }

}
