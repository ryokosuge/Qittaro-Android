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

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.List;

import xyz.ryochin.qittaro.R;
import xyz.ryochin.qittaro.adapters.ArticleAdapter;
import xyz.ryochin.qittaro.apimanagers.APIManagerListener;
import xyz.ryochin.qittaro.apimanagers.ArticlesAPIManager;
import xyz.ryochin.qittaro.models.ArticleModel;
import xyz.ryochin.qittaro.utils.AppController;

public class ArticlesFragment extends Fragment implements AbsListView.OnScrollListener, AdapterView.OnItemClickListener{

    private static final String TAG = ArticlesFragment.class.getSimpleName();
    private final ArticlesFragment self = this;

    private FragmentListener listener;
    private ArticleAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View footerLoadingView;

    private AdView adView;

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
        this.setAdView();
        ListView listView = (ListView) this.getView().findViewById(R.id.article_list_view);
        this.swipeRefreshLayout = (SwipeRefreshLayout)this.getView().findViewById(R.id.article_swipe_refresh);
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

    @Override
    public void onStart() {
        super.onStart();
        AppController.getInstance().sendView(TAG);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (this.adView != null) {
            this.adView.pause();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (this.adView != null) {
            this.adView.resume();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (this.adView != null) {
            this.adView.destroy();
        }
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

    private void setAdView() {
        this.adView = (AdView)this.getView().findViewById(R.id.article_admob_view);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("E7BC9CB9CFE61F02CF1CB17ED85FA7B6")
                .addTestDevice("47B76C38515FC5B269076AAB6D6DB5EE")
                .addTestDevice("C3578474900D7CA6D88C34E3634A24AD")
                .build();
        this.adView.loadAd(adRequest);
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
