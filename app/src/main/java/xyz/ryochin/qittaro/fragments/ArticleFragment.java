/**
 * PACKAGE NAME xyz.ryochin.qittaro.fragments
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/15
 */

package xyz.ryochin.qittaro.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
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

import java.util.ArrayList;
import java.util.List;

import xyz.ryochin.qittaro.R;
import xyz.ryochin.qittaro.adapters.ArticleInfoAdapter;
import xyz.ryochin.qittaro.apimanagers.ArticleAPIManager;
import xyz.ryochin.qittaro.models.ArticleCommentModel;
import xyz.ryochin.qittaro.models.ArticleDetailModel;
import xyz.ryochin.qittaro.models.ArticleInfoModel;
import xyz.ryochin.qittaro.models.ArticleInfoModelType;
import xyz.ryochin.qittaro.models.ArticleTagModel;
import xyz.ryochin.qittaro.utils.AppSharedPreference;

public class ArticleFragment extends Fragment implements AdapterView.OnItemClickListener {

    public interface Listener {
        public void onPressUser(String urlName);
        public void onPressTag(String tagName, String urlName, String iconURL);
        public void setLoadTitle();
        public void setArticleTitle(ArticleDetailModel model);
        public void onLoadError();
        public void onStockedError();
    }

    private static final String TAG = ArticleFragment.class.getSimpleName();
    private final ArticleFragment self = this;

    private static final String BUNDLE_ARGS_ARTICLE_UUID_KEY = "articleUUID";

    private boolean loading = true;
    private boolean stockedLoading = false;
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private ArticleInfoAdapter adapter;
    private WebView webView;
    private Listener listener;
    private AdView adView;

    public static ArticleFragment newInstance(String articleUUID) {
        Bundle args = new Bundle();
        args.putString(BUNDLE_ARGS_ARTICLE_UUID_KEY, articleUUID);
        ArticleFragment fragment = new ArticleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.listener = (Listener)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Please implement the ArticleFragment.Listener.");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_article, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle args = this.getArguments();
        String articleUUID = args.getString(BUNDLE_ARGS_ARTICLE_UUID_KEY);

        this.setAdView();
        this.drawerLayout = (DrawerLayout)this.getView().findViewById(R.id.article_drawer_layout);
        this.drawerList = (ListView)this.getView().findViewById(R.id.article_drawer_list);
        this.webView = (WebView)this.getView().findViewById(R.id.article_web_view);

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
        String token = AppSharedPreference.getToken(this.getActivity());
        ArticleAPIManager.getInstance().getItem(articleUUID, token, this.getItemListener);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.article, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (this.loading) {
            this.hideMenu(menu);
        } else {
            this.showMenu(menu);
        }
    }

    private void hideMenu(Menu menu) {
        menu.findItem(R.id.menu_article_stocked).setVisible(false);
        menu.findItem(R.id.menu_article_browser).setVisible(false);
        menu.findItem(R.id.menu_article_share).setVisible(false);
        menu.findItem(R.id.menu_article_info).setVisible(false);
    }

    private void showMenu(Menu menu) {
        this.setStockedMenu(menu.findItem(R.id.menu_article_stocked));
        menu.findItem(R.id.menu_article_browser).setVisible(true);
        menu.findItem(R.id.menu_article_share).setVisible(true);
        menu.findItem(R.id.menu_article_info).setVisible(true);
    }

    private void setStockedMenu(MenuItem menuItem) {
        boolean loggedIn = AppSharedPreference.isLoggedIn(this.getActivity());
        menuItem.setVisible(loggedIn);
        if (loggedIn) {
            if (this.stockedLoading) {
                this.showRefreshActionView(menuItem);
            } else {
                this.hideRefreshActionView(menuItem);
                this.changeStockedIcon(menuItem);
            }
        }
    }

    private void changeStockedIcon(MenuItem menuItem) {
        if (ArticleAPIManager.getInstance().isStockedItem()) {
            menuItem.setIcon(R.drawable.ic_menu_stocked);
        } else {
            menuItem.setIcon(R.drawable.ic_menu_stock);
        }
    }

    private void showRefreshActionView(MenuItem menuItem) {
        MenuItemCompat.setActionView(menuItem, R.layout.menu_article_loading);
    }

    private void hideRefreshActionView(MenuItem menuItem) {
        MenuItemCompat.setActionView(menuItem, null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_article_info:
                return this.pushMenuArticleInfo();
            case R.id.menu_article_browser:
                return this.pushMenuArticleBrowser();
            case R.id.menu_article_share:
                return this.pushMenuArticleShare();
            case R.id.menu_article_stocked:
                return this.pushMenuStock();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean pushMenuStock() {
        String token = AppSharedPreference.getToken(this.getActivity());
        if (ArticleAPIManager.getInstance().isStockedItem()) {
            ArticleAPIManager.getInstance().unStockArticle(token, updateStockListener);
        } else {
            ArticleAPIManager.getInstance().stockArticle(token, updateStockListener);
        }
        return true;
    }

    private boolean pushMenuArticleShare() {
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

    private boolean pushMenuArticleBrowser() {
        String articleURL = ArticleAPIManager.getInstance().getArticleURL();
        Uri uri = Uri.parse(articleURL);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        this.getActivity().startActivity(intent);
        this.getActivity().finish();
        return true;
    }

    private boolean pushMenuArticleInfo() {
        int gravity = Gravity.RIGHT;
        if (this.drawerLayout.isDrawerOpen(gravity)) {
            this.drawerLayout.closeDrawer(gravity);
        } else {
            this.drawerLayout.openDrawer(gravity);
        }
        return true;
    }

    private ArticleAPIManager.Listener getItemListener = new ArticleAPIManager.Listener() {
        @Override
        public void willStart(ArticleDetailModel model) {
            self.loading = true;
            self.getActivity().supportInvalidateOptionsMenu();
            if (self.listener != null) {
                self.listener.setLoadTitle();
            }
        }

        @Override
        public void onCompleted(ArticleDetailModel model) {
            if (self.listener != null) {
                self.listener.setArticleTitle(model);
            }
            self.setView(model);
            self.loading = false;
            self.getActivity().supportInvalidateOptionsMenu();
        }

        @Override
        public void onError() {
            self.loading = false;
            self.getActivity().supportInvalidateOptionsMenu();
            if (self.listener != null) {
                self.listener.onLoadError();
            }
        }
    };

    private void setView(ArticleDetailModel model) {
        this.setAdapter(model);
        this.webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                view.setVisibility(View.VISIBLE);
                self.setFullLoadingViewVisibility(View.GONE);
            }
        });
        this.webView.getSettings().setAppCacheEnabled(true);
        this.webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        this.webView.getSettings().setLoadWithOverviewMode(true);
        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.loadUrl(model.getUrl());
    }

    private void setAdapter(ArticleDetailModel model) {
        List<ArticleInfoModel> items = new ArrayList<ArticleInfoModel>();

        String userTitle = this.getResources().getString(R.string.article_info_user_title);
        ArticleInfoModel userTitleModel = new ArticleInfoModel(ArticleInfoModelType.Title, userTitle);
        items.add(userTitleModel);

        ArticleInfoModel userModel = new ArticleInfoModel(
                ArticleInfoModelType.User,
                model.getUser().getUrlName(),
                model.getUser().getProfileImageURL()
        );
        items.add(userModel);

        String tagTitle = this.getResources().getString(R.string.article_info_tag_title, model.getTags().size());
        ArticleInfoModel tagTitleModel = new ArticleInfoModel(ArticleInfoModelType.Title, tagTitle);
        items.add(tagTitleModel);

        for(ArticleTagModel tagModel : model.getTags()) {
            ArticleInfoModel tagInfoModel = new ArticleInfoModel(
                    ArticleInfoModelType.Tag,
                    tagModel.getName(),
                    tagModel.getIconURL(),
                    tagModel.getUrlName()
            );
            items.add(tagInfoModel);
        }

        String stockUserTitle = this.getResources().getString(R.string.article_info_stock_user_title, model.getStockCount());
        ArticleInfoModel stockUserTitleModel = new ArticleInfoModel(ArticleInfoModelType.Title, stockUserTitle);
        items.add(stockUserTitleModel);

        for (String stockUserName : model.getStockUsers()) {
            ArticleInfoModel stockUserModel = new ArticleInfoModel(
                    ArticleInfoModelType.StockUser,
                    stockUserName
            );
            items.add(stockUserModel);
        }

        String commentTitle = this.getResources().getString(R.string.article_info_comment_title, model.getCommentCount());
        ArticleInfoModel commentTitleModel = new ArticleInfoModel(
                ArticleInfoModelType.Title,
                commentTitle
        );
        items.add(commentTitleModel);

        for (ArticleCommentModel commentModel : model.getComments()) {
            ArticleInfoModel commentInfoModel = new ArticleInfoModel(
                    ArticleInfoModelType.Comment,
                    commentModel.getUser().getUrlName(),
                    commentModel.getUser().getProfileImageURL(),
                    commentModel.getBody()
            );
            items.add(commentInfoModel);
        }

        this.adapter = new ArticleInfoAdapter(this.getActivity(), items);
        this.drawerList.setAdapter(this.adapter);
    }

    private ArticleAPIManager.Listener updateStockListener = new ArticleAPIManager.Listener() {
        @Override
        public void willStart(ArticleDetailModel model) {
            self.stockedLoading = true;
            self.getActivity().supportInvalidateOptionsMenu();
        }

        @Override
        public void onCompleted(ArticleDetailModel model) {
            self.stockedLoading = false;
            if (model.isStocked()) {
                Toast.makeText(self.getActivity(), R.string.article_detail_stock_message, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(self.getActivity(), R.string.article_detail_un_stock_message, Toast.LENGTH_SHORT).show();
            }
            self.getActivity().supportInvalidateOptionsMenu();
        }

        @Override
        public void onError() {
            self.stockedLoading = false;
            self.getActivity().supportInvalidateOptionsMenu();
            if (self.listener != null) {
                self.listener.onStockedError();
            }
        }
    };

    private void setFullLoadingViewVisibility(int visibility) {
        if (this.getView() != null) {
            this.getView().findViewById(R.id.article_loading_layout).setVisibility(visibility);
        }
    }

    private void setAdView() {
        this.adView = (AdView)this.getView().findViewById(R.id.article_admob_view);
        AdRequest adRequest = new AdRequest.Builder().build();
        this.adView.loadAd(adRequest);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        this.drawerLayout.closeDrawer(Gravity.RIGHT);
        if (this.listener == null) {
            return;
        }
        ArticleInfoModel model = (ArticleInfoModel)this.adapter.getItem(position);
        if (model.getType() == ArticleInfoModelType.User) {
            String urlName = model.getTitle();
            this.listener.onPressUser(urlName);
        } else if (model.getType() == ArticleInfoModelType.Tag) {
            String urlName = model.getBody();
            String name = model.getBody();
            String iconURL = model.getIconURL();
            this.listener.onPressTag(name, urlName, iconURL);
        } else if (model.getType() == ArticleInfoModelType.StockUser) {
            String urlName = model.getTitle();
            this.listener.onPressUser(urlName);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ArticleAPIManager.getInstance().cancel();
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
}
