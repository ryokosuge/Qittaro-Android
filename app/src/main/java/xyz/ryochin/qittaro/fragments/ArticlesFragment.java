/**
 * PACKAGE NAME xyz.ryochin.qittaro.fragments
 * CREATED BY kosugeryou
 * CREATED AT 2014/07/26
 */
package xyz.ryochin.qittaro.fragments;

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

import java.util.List;

import xyz.ryochin.qittaro.R;
import xyz.ryochin.qittaro.adapters.ArticleAdapter;
import xyz.ryochin.qittaro.apimanagers.APIManagerListener;
import xyz.ryochin.qittaro.apimanagers.ArticlesAPIManager;
import xyz.ryochin.qittaro.models.ArticleModel;

public class ArticlesFragment extends Fragment implements AbsListView.OnScrollListener, AdapterView.OnItemClickListener{

    private static final String TAG = ArticlesFragment.class.getSimpleName();
    private final ArticlesFragment self = this;

    private FragmentListener listener;
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
        ListView listView = (ListView) this.getView().findViewById(R.id.article_list_view);
        this.swipeRefreshLayout = (SwipeRefreshLayout)this.getView().findViewById(R.id.article_swipe_refresh);
        this.adapter = new ArticleAdapter(this.getActivity());
        this.swipeRefreshLayout.setColorScheme(
                R.color.app_main_green_color,
                R.color.app_main_bleu_color,
                R.color.app_main_orange_color,
                R.color.app_main_red_color
        );
        this.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ArticlesAPIManager.getInstance().reloadItems(self.reloadAPIManagerListener);
            }
        });
        listView.addFooterView(this.getFooterLoadingView());
        listView.setOnItemClickListener(this);
        listView.setAdapter(this.adapter);
        ArticlesAPIManager.getInstance().getItems(this.getAPIManagerListener);
        listView.setOnScrollListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ArticlesAPIManager.getInstance().cancel();
    }

    private APIManagerListener<ArticleModel> getAPIManagerListener = new APIManagerListener<ArticleModel>() {

        @Override
        public void willStart() {
            self.swipeRefreshLayout.setVisibility(View.GONE);
            self.showFullLoadingView();
        }

        @Override
        public void onCompleted(List<ArticleModel> items) {
            self.adapter.setItems(items);
            self.adapter.notifyDataSetChanged();
            if (ArticlesAPIManager.getInstance().isMax()) {
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
        }

        @Override
        public void onCompleted(List<ArticleModel> items) {
            self.adapter.setItems(items);
            self.adapter.notifyDataSetChanged();
            self.swipeRefreshLayout.setRefreshing(false);
            if (ArticlesAPIManager.getInstance().isMax()) {
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
            if (ArticlesAPIManager.getInstance().isMax()) {
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
        if (!ArticlesAPIManager.getInstance().isMax()) {
            if (totalItemCount != 0 && totalItemCount == firstVisibleItem + visibleItemCount) {
                ArticlesAPIManager.getInstance().addItems(this.addAPIManagerListener);
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

    private void showFooterLoadingView() {
        this.getFooterLoadingView().findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
    }

    private void hideFooterLoadingView() {
        this.getFooterLoadingView().findViewById(R.id.progressBar).setVisibility(View.GONE);
    }

    private void showFullLoadingView() {
        View fullLoadingView = this.getView().findViewById(R.id.article_loading_layout);
        fullLoadingView.setVisibility(View.VISIBLE);
    }

    private void hideFullLoadingView() {
        View fullLoadingView = this.getView().findViewById(R.id.article_loading_layout);
        fullLoadingView.setVisibility(View.GONE);
    }

}
