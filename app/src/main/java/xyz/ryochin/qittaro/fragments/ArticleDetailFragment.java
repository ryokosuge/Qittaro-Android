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
import android.support.v4.app.ShareCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import xyz.ryochin.qittaro.R;
import xyz.ryochin.qittaro.apimanagers.ArticleAPIManager;
import xyz.ryochin.qittaro.models.ArticleDetailModel;
import xyz.ryochin.qittaro.utils.AppController;
import xyz.ryochin.qittaro.utils.AppSharedPreference;

@SuppressLint("SetJavaScriptEnabled")
public class ArticleDetailFragment extends Fragment {

    private static final String TAG = ArticleDetailFragment.class.getSimpleName();
    private final ArticleDetailFragment self = this;

    private static final String BUNDLE_ARGS_ARTICLE_UUID_KEY = "article_uuid";

    private ArticleDetailFragmentListener listener;
    private AdView adView;
    private WebView webView;
    private View loadingView;
    private Menu menu;

    public interface ArticleDetailFragmentListener {
        public void onCompleted(ArticleDetailModel model);
        public void onLoadError();
        public void onStockedError();
        public void openBrowser(String articleURL);
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_article_detail, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((ActionBarActivity)this.getActivity()).getSupportActionBar().setTitle(R.string.article_detail_loading_title);
        this.setAdView();

        Bundle args = this.getArguments();
        String articleUUID = args.getString(BUNDLE_ARGS_ARTICLE_UUID_KEY);

        this.webView = (WebView)this.getView().findViewById(R.id.article_detail_body_web_view);
        this.loadingView = this.getView().findViewById(R.id.article_detail_loading_layout);
        this.webView.setVisibility(View.GONE);
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.article_detail, menu);
        this.menu = menu;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_article_detail_stocked:
                return this.pushStockedMenu();
            case R.id.menu_article_detail_browser:
                return this.pushBrowserMenu();
            case R.id.menu_article_detail_share:
                return this.pushShareMenu();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        AppController.getInstance().sendView(TAG);
    }

    private void setAdView() {
        this.adView = (AdView)this.getView().findViewById(R.id.article_detail_admob_view);
        AdRequest adRequest = new AdRequest.Builder().build();
        this.adView.loadAd(adRequest);
    }

    private boolean pushStockedMenu() {
        String token = AppSharedPreference.getToken(this.getActivity());
        if (ArticleAPIManager.getInstance().isStockedItem()) {
            ArticleAPIManager.getInstance().unStockArticle(token, stockListener);
        } else {
            ArticleAPIManager.getInstance().stockArticle(token, stockListener);
        }
        return true;
    }

    private boolean pushBrowserMenu() {
        String articleURL = ArticleAPIManager.getInstance().getArticleURL();
        this.listener.openBrowser(articleURL);
        return true;
    }

    private boolean pushShareMenu() {
        String articleURL = ArticleAPIManager.getInstance().getArticleURL();
        String articleTitle = ArticleAPIManager.getInstance().getArticleTitle();
        String sharedText = articleTitle + " " + articleURL;

        ShareCompat.IntentBuilder builder = ShareCompat.IntentBuilder.from(this.getActivity());
        builder.setChooserTitle(R.string.menu_article_detail_share_chooser_title);
        builder.setSubject(articleTitle);
        builder.setText(sharedText);
        builder.setType("text/plain");
        builder.startChooser();

        return true;
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
                self.listener.onCompleted(model);
            }
        });
        this.webView.getSettings().setAppCacheEnabled(true);
        this.webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        this.webView.getSettings().setLoadWithOverviewMode(true);
        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.loadUrl(model.getUrl());

        this.changeMenuStocked(AppSharedPreference.isLoggedIn(this.getActivity()), model.isStocked());
    }

    private void changeMenuStocked(boolean isLoggedIn, boolean stocked) {
        if (isLoggedIn) {
            MenuItem stockedMenu = menu.findItem(R.id.menu_article_detail_stocked);
            stockedMenu.setVisible(true);
            MenuItemCompat.setActionView(stockedMenu, null);
            if (stocked) {
                stockedMenu.setIcon(R.drawable.ic_menu_stocked);
            } else {
                stockedMenu.setIcon(R.drawable.ic_menu_stock);
            }
        }
    }

    private void setRefreshActionView() {
        MenuItem menuItem = this.menu.findItem(R.id.menu_article_detail_stocked);
        MenuItemCompat.setActionView(menuItem, R.layout.menu_article_detail_loading);
    }

    private ArticleAPIManager.ArticleAPIManagerListener stockListener = new ArticleAPIManager.ArticleAPIManagerListener() {
        @Override
        public void willStart(ArticleDetailModel model) {
            self.setRefreshActionView();
        }

        @Override
        public void onCompleted(ArticleDetailModel model) {
            self.changeMenuStocked(AppSharedPreference.isLoggedIn(self.getActivity()), model.isStocked());
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
