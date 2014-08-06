/**
 * PACKAGE NAME xyz.ryochin.qittaro.fragments
 * CREATED BY kosugeryou
 * CREATED AT 2014/07/27
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
import android.widget.Spinner;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.List;

import xyz.ryochin.qittaro.R;
import xyz.ryochin.qittaro.adapters.ArticleAdapter;
import xyz.ryochin.qittaro.adapters.TagAdapter;
import xyz.ryochin.qittaro.apimanagers.APIManagerListener;
import xyz.ryochin.qittaro.apimanagers.TagArticlesAPIManager;
import xyz.ryochin.qittaro.apimanagers.TagsAPIManager;
import xyz.ryochin.qittaro.models.ArticleModel;
import xyz.ryochin.qittaro.models.TagModel;
import xyz.ryochin.qittaro.utils.AppController;

public class TagsFragment extends Fragment implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener, AbsListView.OnScrollListener {

    private static final String TAG = TagsFragment.class.getSimpleName();
    private final TagsFragment self = this;

    private static final String SAVED_SELECTED_TAG_INDEX_KEY = "selectedTagIndex";

    private Spinner spinner;
    private TagAdapter spinnerAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArticleAdapter adapter;
    private FragmentListener listener;
    private View footerLoadingView;
    private int selectedTagIndex = 0;
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
        return inflater.inflate(R.layout.fragment_tags, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.setAdView();

        if (savedInstanceState != null) {
            this.selectedTagIndex = savedInstanceState.getInt(SAVED_SELECTED_TAG_INDEX_KEY, 0);
        }

        this.spinner = (Spinner) this.getView().findViewById(R.id.tags_spinner);
        ListView listView = (ListView) this.getView().findViewById(R.id.tags_article_list_view);
        this.swipeRefreshLayout = (SwipeRefreshLayout)this.getView().findViewById(R.id.tags_article_swipe_refresh);
        this.swipeRefreshLayout.setColorSchemeResources(
                R.color.app_first_green_color,
                R.color.app_second_green_color,
                R.color.app_third_green_color,
                R.color.app_fourth_green_color
        );
        this.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                TagArticlesAPIManager.getInstance().reloadItems(self.articleAPIManagerListener);
            }
        });
        this.spinnerAdapter = new TagAdapter(this.getActivity());
        this.adapter = new ArticleAdapter(this.getActivity());
        listView.addFooterView(this.getFooterLoadingView());
        listView.setOnItemClickListener(this);
        listView.setOnScrollListener(this);
        listView.setAdapter(this.adapter);
        this.spinner.setAdapter(this.spinnerAdapter);
        this.spinner.setOnItemSelectedListener(this);
        TagsAPIManager.getInstance().getItems(this.tagAPIManagerListener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        TagsAPIManager.getInstance().cancel();
        TagArticlesAPIManager.getInstance().cancel();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_SELECTED_TAG_INDEX_KEY, this.selectedTagIndex);
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

    private APIManagerListener<TagModel> tagAPIManagerListener = new APIManagerListener<TagModel>() {
        @Override
        public void willStart() {
            self.showFullLoadingView();
            self.spinner.setVisibility(View.GONE);
            self.swipeRefreshLayout.setVisibility(View.GONE);
        }

        @Override
        public void onCompleted(List<TagModel> items) {
            self.spinnerAdapter.setItems(items);
            self.spinnerAdapter.notifyDataSetChanged();
            self.hideFullLoadingView();
            self.spinner.setVisibility(View.VISIBLE);
            self.swipeRefreshLayout.setVisibility(View.VISIBLE);
            TagModel tagModel = items.get(self.selectedTagIndex);
            TagArticlesAPIManager.getInstance().getItems(tagModel.getUrlName(), self.articleAPIManagerListener);
        }

        @Override
        public void onError() {
        }
    };

    private APIManagerListener<ArticleModel> addArticleAPIManagerListener = new APIManagerListener<ArticleModel>() {
        @Override
        public void willStart() {
        }

        @Override
        public void onCompleted(List<ArticleModel> items) {
            self.adapter.addItems(items);
            self.adapter.notifyDataSetChanged();
            if (TagArticlesAPIManager.getInstance().isMax()) {
                self.hideFooterLoadingView();
            }
        }

        @Override
        public void onError() {
        }
    };

    private APIManagerListener<ArticleModel> articleAPIManagerListener = new APIManagerListener<ArticleModel>() {
        @Override
        public void willStart() {
        }

        @Override
        public void onCompleted(List<ArticleModel> items) {
            self.adapter.setItems(items);
            self.adapter.notifyDataSetChanged();
            self.swipeRefreshLayout.setRefreshing(false);
            if (TagArticlesAPIManager.getInstance().isMax()) {
                self.hideFooterLoadingView();
            }
        }

        @Override
        public void onError() {
            self.swipeRefreshLayout.setRefreshing(false);
        }
    };

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        this.adapter.clear();
        this.adapter.notifyDataSetChanged();
        this.selectedTagIndex = position;
        this.showFooterLoadingView();
        TagModel tagModel = (TagModel)this.spinnerAdapter.getItem(position);
        TagArticlesAPIManager.getInstance()
                .getItems(tagModel.getUrlName(), this.articleAPIManagerListener);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Log.e(TAG, "onNothingSelected()");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ArticleModel articleModel = (ArticleModel)this.adapter.getItem(position);
        this.listener.onItemSelected(articleModel);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (!TagArticlesAPIManager.getInstance().isMax() && this.adapter.getCount() > 0) {
            if (totalItemCount != 0 && totalItemCount == firstVisibleItem + visibleItemCount) {
                TagArticlesAPIManager.getInstance().addItems(this.addArticleAPIManagerListener);
            }
        }
    }

    private View getFooterLoadingView() {
        if (this.footerLoadingView == null) {
            this.footerLoadingView = this.getActivity()
                    .getLayoutInflater().inflate(R.layout.fragment_article_loading, null);
        }
        return this.footerLoadingView;
    }

    private void showFullLoadingView() {
        View fullLoadingView = this.getView().findViewById(R.id.tags_loading_layout);
        fullLoadingView.setVisibility(View.VISIBLE);
    }

    private void hideFullLoadingView() {
        View fullLoadingView = this.getView().findViewById(R.id.tags_loading_layout);
        fullLoadingView.setVisibility(View.GONE);
    }

    private void hideFooterLoadingView() {
        View footerLoadingView = this.getFooterLoadingView();
        footerLoadingView.findViewById(R.id.progressBar).setVisibility(View.GONE);
    }

    private void showFooterLoadingView() {
        View footerLoadingView = this.getFooterLoadingView();
        footerLoadingView.findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
    }

    private void setAdView() {
        this.adView = (AdView)this.getView().findViewById(R.id.tags_admob_view);
        AdRequest adRequest = new AdRequest.Builder().build();
        this.adView.loadAd(adRequest);
    }
}
