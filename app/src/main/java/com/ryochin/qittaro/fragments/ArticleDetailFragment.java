/**
 * PACKAGE NAME com.ryochin.qittaro.fragments
 * CREATED BY kosugeryou
 * CREATED AT 2014/07/27
 */
package com.ryochin.qittaro.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.ryochin.qittaro.R;
import com.ryochin.qittaro.apimanagers.ArticleAPIManager;
import com.ryochin.qittaro.models.ArticleDetailModel;
import com.ryochin.qittaro.utils.AppSharedPreference;

@SuppressLint("SetJavaScriptEnabled")
public class ArticleDetailFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = ArticleDetailFragment.class.getSimpleName();
    private final ArticleDetailFragment self = this;

    private static final String BUNDLE_ARGS_ARTICLE_UUID_KEY = "article_uuid";

    private ArticleDetailFragmentListener listener;
    private WebView webView;
    private View loadingView;
    private View bottomBtnView;

    public interface ArticleDetailFragmentListener {
        public void onLoadError();
        public void onStockedError();
    }

    public ArticleDetailFragment() {}

    public static ArticleDetailFragment newInstance(String articleUUID) {
        Bundle args = new Bundle();
        args.putString(BUNDLE_ARGS_ARTICLE_UUID_KEY, articleUUID);
        ArticleDetailFragment fragment = new ArticleDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ArticleDetailFragmentListener) {
            this.listener = (ArticleDetailFragmentListener)activity;
        } else {
            throw new ClassCastException("activity が ArticlesFragmentListener を実装していません.");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_article_detail, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle args = this.getArguments();
        String articleUUID = args.getString(BUNDLE_ARGS_ARTICLE_UUID_KEY);

        this.webView = (WebView)this.getView().findViewById(R.id.article_detail_body_web_view);
        this.loadingView = this.getView().findViewById(R.id.article_detail_loading_layout);
        this.bottomBtnView = this.getView().findViewById(R.id.article_detail_btn_layout);
        this.webView.setVisibility(View.GONE);
        this.bottomBtnView.setVisibility(View.GONE);
        this.loadingView.setVisibility(View.VISIBLE);
        String token = AppSharedPreference.getToken(this.getActivity());
        ArticleAPIManager.getInstance().getItem(articleUUID, token, new ArticleAPIManager.ArticleAPIManagerListener() {
            @Override
            public void onCompleted(ArticleDetailModel model) {
                self.setWebView(model);
            }

            @Override
            public void onError() {
                listener.onLoadError();
            }
        });
    }

    private void setWebView(ArticleDetailModel model) {
        this.webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                view.setVisibility(View.VISIBLE);
                self.loadingView.setVisibility(View.GONE);
                if (AppSharedPreference.isLoggedIn(self.getActivity())) {
                    self.bottomBtnView.setVisibility(View.VISIBLE);
                }
            }
        });
        this.webView.getSettings().setLoadWithOverviewMode(true);
        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.loadUrl(model.getUrl());

        if (AppSharedPreference.isLoggedIn(this.getActivity())) {
            this.changeBtnText(model.isStocked());
        }
    }

    private void changeBtnText(boolean stocked) {
        Button stockedBtn = (Button)this.getView().findViewById(R.id.article_detail_stocked_btn);
        stockedBtn.setOnClickListener(this);
        if (stocked) {
            stockedBtn.setText(R.string.article_detail_un_stock_text);
        } else {
            stockedBtn.setText(R.string.article_detail_stock_text);
        }
    }

    @Override
    public void onClick(View v) {
        String token = AppSharedPreference.getToken(this.getActivity());
        if (ArticleAPIManager.getInstance().isStockedItem()) {
            ArticleAPIManager.getInstance().unStockArticle(token, stockListener);
        } else {
            ArticleAPIManager.getInstance().stockArticle(token, stockListener);
        }
    }

    private ArticleAPIManager.ArticleAPIManagerListener stockListener = new ArticleAPIManager.ArticleAPIManagerListener() {
        @Override
        public void onCompleted(ArticleDetailModel model) {
            self.changeBtnText(model.isStocked());
            if (model.isStocked()) {
                Toast.makeText(self.getActivity(), R.string.article_detail_stock_message, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(self.getActivity(), R.string.article_detail_un_stock_message, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError() {
            self.listener.onStockedError();
        }
    };
}
