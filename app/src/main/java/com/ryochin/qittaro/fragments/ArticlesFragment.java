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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.ryochin.qittaro.R;
import com.ryochin.qittaro.adapters.ArticleAdapter;
import com.ryochin.qittaro.apimanagers.APIManagerListener;
import com.ryochin.qittaro.apimanagers.ArticleAPIManager;
import com.ryochin.qittaro.models.ArticleModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ArticlesFragment extends Fragment implements AbsListView.OnScrollListener {
    private static final String TAG = ArticlesFragment.class.getSimpleName();
    private final ArticlesFragment self = this;

    private ListView listView;
    private ArticleAdapter adapter;
    private View footerLoadingView;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_article, container, false);
        this.listView = (ListView)rootView.findViewById(R.id.article_list_view);
        this.listView.addFooterView(this.getFooterLoadingView());
        this.adapter = new ArticleAdapter(this.getActivity());
        this.listView.setAdapter(this.adapter);
        ArticleAPIManager.getInstance().reloadItems(new APIManagerListener() {
            @Override
            public void willStart() {
            }
            @Override
            public void onCompleted(String response) {
                self.setResponseToAdapter(response);
            }
            @Override
            public void onError() {
            }
        });

        this.listView.setOnScrollListener(this);
        return rootView;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (totalItemCount != 0 && totalItemCount == firstVisibleItem + visibleItemCount) {
            ArticleAPIManager.getInstance().addItems(new APIManagerListener() {
                @Override
                public void willStart() {
                }

                @Override
                public void onCompleted(String response) {
                    self.setResponseToAdapter(response);
                }

                @Override
                public void onError() {
                }
            });
        }
    }

    private View getFooterLoadingView() {
        if (this.footerLoadingView == null) {
            this.footerLoadingView = this.getActivity()
                    .getLayoutInflater().inflate(R.layout.fragment_article_loading, null);
        }
        return this.footerLoadingView;
    }

    private void setResponseToAdapter(String response) {
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(response);
            int responseArrayCount = jsonArray.length();
            List<ArticleModel> items = new ArrayList<ArticleModel>(responseArrayCount);
            for (int i = 0; i < responseArrayCount; i ++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                ArticleModel articleModel = new ArticleModel(jsonObject);
                items.add(articleModel);
            }
            self.adapter.addItems(items);
            self.adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            Log.e(TAG, "JSONException ::", e);
        }
    }
}
