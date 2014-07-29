/**
 * PACKAGE NAME com.ryochin.qittaro.fragments
 * CREATED BY kosugeryou
 * CREATED AT 2014/07/27
 */
package com.ryochin.qittaro.fragments;

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

import com.ryochin.qittaro.R;
import com.ryochin.qittaro.adapters.ArticleAdapter;
import com.ryochin.qittaro.adapters.TagAdapter;
import com.ryochin.qittaro.apimanagers.APIManagerListener;
import com.ryochin.qittaro.apimanagers.TagAPIManager;
import com.ryochin.qittaro.apimanagers.TagArticleAPIManager;
import com.ryochin.qittaro.models.ArticleModel;
import com.ryochin.qittaro.models.TagModel;

import java.util.List;

public class TagsFragment extends Fragment implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener, AbsListView.OnScrollListener {

    private static final String TAG = TagsFragment.class.getSimpleName();
    private final TagsFragment self = this;

    private static final String SAVED_SELECTED_TAG_INDEX_KEY = "selectedTagIndex";

    private Spinner spinner;
    private TagAdapter spinnerAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;
    private ArticleAdapter adapter;
    private ArticlesFragmentListener listener;
    private View footerLoadingView;
    private int selectedTagIndex = 0;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if ((activity instanceof ArticlesFragmentListener)) {
            this.listener = (ArticlesFragmentListener) activity;
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

        if (savedInstanceState != null) {
            this.selectedTagIndex = savedInstanceState.getInt(SAVED_SELECTED_TAG_INDEX_KEY, 0);
        }

        this.spinner = (Spinner)this.getView().findViewById(R.id.tags_spinner);
        this.swipeRefreshLayout = (SwipeRefreshLayout)this.getView().findViewById(R.id.tags_article_swipe_refresh);
        this.swipeRefreshLayout.setColorSchemeColors(
                R.color.app_main_green_color,
                R.color.app_main_bleu_color,
                R.color.app_main_orange_color,
                R.color.app_main_red_color
        );
        this.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                TagArticleAPIManager.getInstance().reloadItems(self.articleAPIManagerListener);
            }
        });
        this.listView = (ListView)this.getView().findViewById(R.id.tags_article_list_view);
        this.spinnerAdapter = new TagAdapter(this.getActivity());
        this.adapter = new ArticleAdapter(this.getActivity());
        this.listView.addFooterView(this.getFooterLoadingView());
        this.listView.setOnItemClickListener(this);
        this.listView.setOnScrollListener(this);
        this.listView.setAdapter(this.adapter);
        this.spinner.setAdapter(this.spinnerAdapter);
        this.spinner.setOnItemSelectedListener(this);
        TagAPIManager.getInstance().getItems(this.tagAPIManagerListener);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_SELECTED_TAG_INDEX_KEY, this.selectedTagIndex);
    }

    private APIManagerListener<TagModel> tagAPIManagerListener = new APIManagerListener<TagModel>() {
        @Override
        public void onCompleted(List<TagModel> items) {
            self.spinnerAdapter.setItems(items);
            self.spinnerAdapter.notifyDataSetChanged();
            TagModel tagModel = items.get(self.selectedTagIndex);
            TagArticleAPIManager.getInstance().getItems(tagModel.getUrlName(), self.articleAPIManagerListener);
        }

        @Override
        public void onError() {
        }
    };

    private APIManagerListener<ArticleModel> addArticleAPIManagerListener = new APIManagerListener<ArticleModel>() {
        @Override
        public void onCompleted(List<ArticleModel> items) {
            self.adapter.addItems(items);
            self.adapter.notifyDataSetChanged();
        }

        @Override
        public void onError() {
        }
    };

    private APIManagerListener<ArticleModel> articleAPIManagerListener = new APIManagerListener<ArticleModel>() {
        @Override
        public void onCompleted(List<ArticleModel> items) {
            self.adapter.setItems(items);
            self.adapter.notifyDataSetChanged();
            self.swipeRefreshLayout.setRefreshing(false);
        }

        @Override
        public void onError() {
        }
    };

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        this.adapter.clear();
        this.adapter.notifyDataSetChanged();
        this.selectedTagIndex = position;
        TagModel tagModel = (TagModel)this.spinnerAdapter.getItem(position);
        TagArticleAPIManager.getInstance()
                .getItems(tagModel.getUrlName(), this.articleAPIManagerListener);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Log.e(TAG, "onNothingSelected()");
    }

    private View getFooterLoadingView() {
        if (this.footerLoadingView == null) {
            this.footerLoadingView = this.getActivity()
                    .getLayoutInflater().inflate(R.layout.fragment_article_loading, null);
        }
        return this.footerLoadingView;
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
        if (totalItemCount != 0 && totalItemCount == firstVisibleItem + visibleItemCount) {
            TagArticleAPIManager.getInstance().addItems(this.addArticleAPIManagerListener);
        }
    }
}
