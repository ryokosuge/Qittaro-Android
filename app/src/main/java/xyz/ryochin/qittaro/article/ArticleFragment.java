/**
 * PACKAGE NAME xyz.ryochin.qittaro.article
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/21
 */

package xyz.ryochin.qittaro.article;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.List;

import xyz.ryochin.qittaro.R;
import xyz.ryochin.qittaro.activities.TagActivity;
import xyz.ryochin.qittaro.activities.UserActivity;
import xyz.ryochin.qittaro.adapters.ArticleInfoAdapter;
import xyz.ryochin.qittaro.models.ArticleInfoModel;
import xyz.ryochin.qittaro.utils.AppController;
import xyz.ryochin.qittaro.utils.AppSharedPreference;

public class ArticleFragment extends Fragment implements ArticleView, AdapterView.OnItemClickListener {

    private static final String TAG = ArticleFragment.class.getSimpleName();
    private final ArticleFragment self = this;

    private static final String ARGS_ARTICLE_UUID_KEY = "articleUUID";

    private View fullLoadingView;
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private ArticleInfoAdapter adapter;
    private WebView webView;
    private ArticlePresenter presenter;
    private AdView adView;

    private boolean isShowActionBar = false;
    private boolean isShowActionBarLoading = false;
    private boolean isStockedArticle = false;

    public static ArticleFragment newInstance(String articleUUID) {
        Bundle args = new Bundle();
        args.putString(ARGS_ARTICLE_UUID_KEY, articleUUID);
        ArticleFragment fragment = new ArticleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_article, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle args = getArguments();
        String articleUUID = args.getString(ARGS_ARTICLE_UUID_KEY);
        String token = AppSharedPreference.getToken(this.getActivity());

        this.presenter = new ArticlePresenterImpl(this, this.getActivity(), articleUUID, token);
        this.webView = (WebView)getView().findViewById(R.id.article_web_view);
        this.drawerLayout = (DrawerLayout)getView().findViewById(R.id.article_drawer_layout);
        this.drawerList = (ListView)getView().findViewById(R.id.article_drawer_list);
        this.drawerList.setOnItemClickListener(this);
        DrawerLayout.DrawerListener drawerListener = new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View view, float v) {
            }

            @Override
            public void onDrawerOpened(View view) {
                self.getActivity().supportInvalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View view) {
                self.getActivity().supportInvalidateOptionsMenu();
            }

            @Override
            public void onDrawerStateChanged(int i) {
            }
        };
        this.drawerLayout.setDrawerListener(drawerListener);
        this.fullLoadingView = getView().findViewById(R.id.article_loading_layout);

        this.adView = (AdView) this.getView().findViewById(R.id.article_admob_view);
        AdRequest adRequest = new AdRequest.Builder().build();
        this.adView.loadAd(adRequest);
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.start();
        AppController.getInstance().sendView(TAG);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (adView != null) {
            adView.pause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (adView != null) {
            adView.destroy();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.article, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (isShowActionBar) {
            showMenu(menu);
        } else {
            hideMenu(menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_article_info:
                pushMenuInfo();
                break;
            case R.id.menu_article_browser:
                presenter.openBrowser();
                break;
            case R.id.menu_article_stocked:
                String token = AppSharedPreference.getToken(this.getActivity());
                presenter.stock(token);
                break;
            case R.id.menu_article_share:
                presenter.share(this.getActivity());
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void pushMenuInfo() {
        int gravity = Gravity.RIGHT;
        if (this.drawerLayout.isDrawerOpen(gravity)) {
            this.drawerLayout.closeDrawer(gravity);
        } else {
            this.drawerLayout.openDrawer(gravity);
        }
    }

    private void showMenu(Menu menu) {
        showMenuStockItem(menu.findItem(R.id.menu_article_stocked));
        menu.findItem(R.id.menu_article_browser).setVisible(true);
        menu.findItem(R.id.menu_article_share).setVisible(true);
        menu.findItem(R.id.menu_article_info).setVisible(true);
    }

    private void showMenuStockItem(MenuItem menuItem) {
        boolean loggedIn = AppSharedPreference.isLoggedIn(this.getActivity());
        menuItem.setVisible(loggedIn);
        if (loggedIn) {
            if (this.isShowActionBarLoading) {
                this.showActionBarLoading(menuItem);
            } else {
                this.changeStateStockIcon(menuItem);
            }
        }
    }

    private void changeStateStockIcon(MenuItem menuItem) {
        hideActionBarLoading(menuItem);
        if (isStockedArticle) {
            menuItem.setIcon(R.drawable.ic_menu_stocked);
        } else {
            menuItem.setIcon(R.drawable.ic_menu_stock);
        }
    }

    private void showActionBarLoading(MenuItem menuItem) {
        MenuItemCompat.setActionView(menuItem, R.layout.menu_article_loading);
    }

    private void hideActionBarLoading(MenuItem menuItem) {
        MenuItemCompat.setActionView(menuItem, null);
    }

    private void hideMenu(Menu menu) {
        menu.findItem(R.id.menu_article_stocked).setVisible(false);
        menu.findItem(R.id.menu_article_browser).setVisible(false);
        menu.findItem(R.id.menu_article_share).setVisible(false);
        menu.findItem(R.id.menu_article_info).setVisible(false);
    }

    @Override
    public void showFullLoadingView() {
        webView.setVisibility(View.GONE);
        fullLoadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideFullLoadingView() {
        webView.setVisibility(View.VISIBLE);
        fullLoadingView.setVisibility(View.GONE);
    }

    @Override
    public void showActionBarItem(boolean stocked) {
        this.isShowActionBar = true;
        this.isStockedArticle = stocked;
        this.getActivity().supportInvalidateOptionsMenu();
    }

    @Override
    public void hideActionBarItem() {
        this.isShowActionBar = false;
        this.getActivity().supportInvalidateOptionsMenu();
    }

    @Override
    public void showActionBarProgress() {
        this.isShowActionBarLoading = true;
        this.getActivity().supportInvalidateOptionsMenu();
    }

    @Override
    public void hideActionBarProgress(boolean stocked) {
        this.isShowActionBarLoading = false;
        this.isStockedArticle = stocked;
        this.getActivity().supportInvalidateOptionsMenu();
    }

    @Override
    public void setWebView(WebViewClient webViewClient, String url) {
        this.webView.setWebViewClient(webViewClient);

        this.webView.getSettings().setAppCacheEnabled(true);
        this.webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        this.webView.getSettings().setLoadWithOverviewMode(true);
        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.loadUrl(url);
    }

    @Override
    public void setSideInfoItems(List<ArticleInfoModel> items) {
        this.adapter = new ArticleInfoAdapter(this.getActivity(), items);
        this.drawerList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void navigateToUserActivity(String urlName) {
        Intent intent = new Intent(this.getActivity(), UserActivity.class);
        intent.putExtra(UserActivity.INTENT_USER_URL_NAME_KEY, urlName);
        this.getActivity().startActivity(intent);
    }

    @Override
    public void navigateToTagActivity(String urlName, String tagName, String tagIconUrl) {
        Intent intent = new Intent(this.getActivity(), TagActivity.class);
        intent.putExtra(TagActivity.INTENT_TAG_URL_NAME_KEY, urlName);
        intent.putExtra(TagActivity.INTENT_TAG_NAME_KEY, tagName);
        intent.putExtra(TagActivity.INTENT_TAG_ICON_URL_KEY, tagIconUrl);
        this.getActivity().startActivity(intent);
    }

    @Override
    public void navigateToBrowser(String articleUrl) {
        Uri uri = Uri.parse(articleUrl);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        this.getActivity().startActivity(intent);
        this.getActivity().finish();
    }

    @Override
    public void showErrorMessage() {
    }

    @Override
    public void showLoadingTitle() {
        ((ActionBarActivity)getActivity()).getSupportActionBar()
                .setTitle(R.string.article_detail_loading_title);
    }

    @Override
    public void showArticleTitle(String title) {
        ((ActionBarActivity)getActivity()).getSupportActionBar()
                .setTitle(title);
    }

    @Override
    public void showSuccessStockMessage(boolean stocked) {
        if (stocked) {
            Toast.makeText(self.getActivity(), R.string.article_detail_stock_message, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(self.getActivity(), R.string.article_detail_un_stock_message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        presenter.onItemClicked(position);
    }
}
