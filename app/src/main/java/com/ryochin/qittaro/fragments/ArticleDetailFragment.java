/**
 * PACKAGE NAME com.ryochin.qittaro.fragments
 * CREATED BY kosugeryou
 * CREATED AT 2014/07/27
 */
package com.ryochin.qittaro.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ryochin.qittaro.R;

public class ArticleDetailFragment extends Fragment {

    private static final String TAG = ArticleDetailFragment.class.getSimpleName();
    private final ArticleDetailFragment self = this;

    private static final String BUNDLE_ARGS_ARTICLE_URL_KEY = "article_url";

    public ArticleDetailFragment() {}

    public static ArticleDetailFragment newInstance(String articleURL) {
        Bundle args = new Bundle();
        args.putString(BUNDLE_ARGS_ARTICLE_URL_KEY, articleURL);
        ArticleDetailFragment fragment = new ArticleDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_article_detail, container, false);
        Bundle args = this.getArguments();
        String articleURL = args.getString(BUNDLE_ARGS_ARTICLE_URL_KEY);

        WebView webView = (WebView)rootView.findViewById(R.id.article_detail_body_web_view);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.e(TAG, "URL = " + url);
                return false;
            }
        });
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.loadUrl(articleURL);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
