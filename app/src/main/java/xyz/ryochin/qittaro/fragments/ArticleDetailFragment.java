/**
 * PACKAGE NAME xyz.ryochin.qittaro.fragments
 * CREATED BY kosugeryou
 * CREATED AT 2014/07/27
 */
package xyz.ryochin.qittaro.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import xyz.ryochin.qittaro.R;
import xyz.ryochin.qittaro.apimanagers.ArticleAPIManager;
import xyz.ryochin.qittaro.models.ArticleDetailModel;
import xyz.ryochin.qittaro.utils.AppController;
import xyz.ryochin.qittaro.utils.AppSharedPreference;

@SuppressLint("SetJavaScriptEnabled")
public class ArticleDetailFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = ArticleDetailFragment.class.getSimpleName();
    private final ArticleDetailFragment self = this;
    private static final int APP_CACHE_MAX_SIZE = 8 * 1024 * 1024;
    private static final String AD_UNIT_ID = "ca-app-pub-3010029359415397/5967347269";

    private static final String BUNDLE_ARGS_ARTICLE_UUID_KEY = "article_uuid";

    private ArticleDetailFragmentListener listener;
    private WebView webView;
    private View loadingView;
    private View bottomBtnView;
    private InterstitialAd interstitialAd;

    public interface ArticleDetailFragmentListener {
        public void onCompleted(ArticleDetailModel model);
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

        this.setInterstitialView();
        ((ActionBarActivity)this.getActivity()).getSupportActionBar().setTitle(R.string.article_detail_loading_title);

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
            public void willStart(ArticleDetailModel model) {
            }

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.webView.stopLoading();
        ArticleAPIManager.getInstance().cancel();
    }

    @Override
    public void onStart() {
        super.onStart();
        AppController.getInstance().sendView(TAG);
    }

    private void setWebView(final ArticleDetailModel model) {
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
                if (self.getActivity() != null) {
                    if (AppSharedPreference.isLoggedIn(self.getActivity())) {
                        self.bottomBtnView.setVisibility(View.VISIBLE);
                    }
                }
                self.listener.onCompleted(model);
            }
        });
        this.webView.getSettings().setAppCacheEnabled(true);
        // this.webView.getSettings().setAppCacheMaxSize(APP_CACHE_MAX_SIZE);
        this.webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        this.webView.getSettings().setLoadWithOverviewMode(true);
        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.loadUrl(model.getUrl());

        if (AppSharedPreference.isLoggedIn(this.getActivity())) {
            this.changeBtnText(model.isStocked());
        }
    }

    private void changeBtnProcessText(boolean stocked) {
        Button stockedBtn = (Button)this.getView().findViewById(R.id.article_detail_stocked_btn);
        if (stocked) {
            stockedBtn.setText(R.string.article_detail_un_stock_process_text);
        } else {
            stockedBtn.setText(R.string.article_detail_stock_process_text);
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
        public void willStart(ArticleDetailModel model) {
            self.changeBtnProcessText(model.isStocked());
        }

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

    private void setInterstitialView() {
        this.interstitialAd = new InterstitialAd(this.getActivity());
        this.interstitialAd.setAdUnitId(AD_UNIT_ID);
        this.interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                Log.d(TAG, "onAdLoaded");
                if (self.interstitialAd.isLoaded()) {
                    self.interstitialAd.show();
                }
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Log.d(TAG, "onAdFailedToLoad()");
                String errorReason = self.getErrorReason(errorCode);
                String message = String.format("onAdFailedToLoad (%s)", errorReason);
                Toast.makeText(self.getActivity(), message, Toast.LENGTH_SHORT).show();
            }
        });

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("E7BC9CB9CFE61F02CF1CB17ED85FA7B6")
                .addTestDevice("47B76C38515FC5B269076AAB6D6DB5EE")
                .build();
        this.interstitialAd.loadAd(adRequest);
    }

    private String getErrorReason(int errorCode) {
        String errorReason = "";
        switch (errorCode) {
            case AdRequest.ERROR_CODE_INTERNAL_ERROR:
                errorReason = "Internal error.";
                break;
            case AdRequest.ERROR_CODE_INVALID_REQUEST:
                errorReason = "Invalid request.";
                break;
            case AdRequest.ERROR_CODE_NETWORK_ERROR:
                errorReason = "Network error.";
                break;
            case AdRequest.ERROR_CODE_NO_FILL:
                errorReason = "No fill";
                break;
        }
        return errorReason;
    }
}
