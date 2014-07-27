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

public class TagsFragment extends Fragment implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {

    private static final String TAG = TagsFragment.class.getSimpleName();
    private final TagsFragment self = this;

    private Spinner spinner;
    private TagAdapter spinnerAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;
    private ArticleAdapter adapter;
    private ArticlesFragmentListener listener;
    private View footerLoadingView;

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
        View rootView = inflater.inflate(R.layout.fragment_tags, container, false);
        this.spinner = (Spinner)rootView.findViewById(R.id.tags_spinner);
        this.swipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.tags_article_swipe_refresh);
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
        this.listView = (ListView)rootView.findViewById(R.id.tags_article_list_view);
        this.listView.addFooterView(this.getFooterLoadingView());
        this.listView.setOnItemClickListener(this);
        this.spinnerAdapter = new TagAdapter(this.getActivity());
        this.adapter = new ArticleAdapter(this.getActivity());
        this.listView.setAdapter(this.adapter);
        this.spinner.setAdapter(this.spinnerAdapter);
        this.spinner.setOnItemSelectedListener(this);
        TagAPIManager.getInstance().reloadItems(this.tagAPIManagerListener);
        return rootView;
    }

    private APIManagerListener<TagModel> tagAPIManagerListener = new APIManagerListener<TagModel>() {
        @Override
        public void onCompleted(List<TagModel> items) {
            self.spinnerAdapter.setItems(items);
            self.spinnerAdapter.notifyDataSetChanged();
            // とりあえず最初
            TagModel tagModel = items.get(0);
            TagArticleAPIManager.getInstance().reloadItems(tagModel.getUrlName(), self.articleAPIManagerListener);
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
        Log.e(TAG, "onItemSelected()");
        this.adapter.clear();
        this.adapter.notifyDataSetChanged();
        TagModel tagModel = (TagModel)this.spinnerAdapter.getItem(position);
        TagArticleAPIManager.getInstance().reloadItems(tagModel.getUrlName(), this.articleAPIManagerListener);
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
}
