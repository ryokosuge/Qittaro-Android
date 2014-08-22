
/**
 * PACKAGE NAME xyz.ryochin.qittaro.articles.search
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/21
 */
package xyz.ryochin.qittaro.articles.search;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.List;

import xyz.ryochin.qittaro.R;
import xyz.ryochin.qittaro.activities.ArticleActivity;
import xyz.ryochin.qittaro.articles.ArticlesAdapter;
import xyz.ryochin.qittaro.fragments.AlertDialogFragment;
import xyz.ryochin.qittaro.models.ArticleModel;
import xyz.ryochin.qittaro.utils.AppController;
import xyz.ryochin.qittaro.utils.AppSharedPreference;

public class SearchFragment extends Fragment implements SearchView, AdapterView.OnItemClickListener, AbsListView.OnScrollListener, android.support.v7.widget.SearchView.OnQueryTextListener {

    public interface Listener {
        public void setTitle(String title);
        public void navigateTo(Intent intent);
    }

    public enum Type {
        All, Stocks
    }

    private static final String TAG = SearchFragment.class.getSimpleName();
    private final SearchFragment self = this;

    private static final String ARGS_SEARCH_IN_STOCKS_KEY = "searchInStocks";
    private static final String ARGS_SHOW_AD_KEY = "showAd";
    private SwipeRefreshLayout swipeRefreshLayout;
    private View fullLoadingView;
    private ArticlesAdapter adapter;
    private SearchPresenter presenter;
    private AdView adView;
    private View footerView;
    private Listener listener;
    private boolean searchingClosed = false;

    public static SearchFragment newInstance(boolean searchInStocks, boolean showAd) {
        Bundle args = new Bundle();
        args.putBoolean(ARGS_SEARCH_IN_STOCKS_KEY, searchInStocks);
        args.putBoolean(ARGS_SHOW_AD_KEY, showAd);
        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.listener = (Listener)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Please implement FragmentListener.");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.basic_list_view_layout, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle args = getArguments();
        boolean searchInStocks = args.getBoolean(ARGS_SEARCH_IN_STOCKS_KEY);
        boolean showAd = args.getBoolean(ARGS_SHOW_AD_KEY);
        if (AppSharedPreference.isLoggedIn(this.getActivity())) {
            String token = AppSharedPreference.getToken(this.getActivity());
            this.presenter = new SearchPresenterImpl(this, searchInStocks, token);
        } else {
            this.presenter = new SearchPresenterImpl(this);
        }
        ListView listView = (ListView) getView().findViewById(R.id.basic_list_view);
        listView.addFooterView(this.getFooterView());
        listView.setOnItemClickListener(this);
        listView.setOnScrollListener(this);
        this.fullLoadingView = getView().findViewById(R.id.basic_list_loading_layout);
        this.swipeRefreshLayout = (SwipeRefreshLayout)getView().findViewById(R.id.basic_list_swipe_refresh);
        this.swipeRefreshLayout.setColorSchemeResources(
                R.color.app_first_green_color,
                R.color.app_second_green_color,
                R.color.app_third_green_color,
                R.color.app_fourth_green_color
        );
        this.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                self.presenter.refresh();
            }
        });
        this.adapter = new ArticlesAdapter(this.getActivity());
        listView.setAdapter(adapter);
        if (showAd) {
            this.setAdView();
        }

        swipeRefreshLayout.setVisibility(View.GONE);
        fullLoadingView.setVisibility(View.GONE);
        getFooterView().findViewById(R.id.progressBar).setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
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
    public void onDestroyView() {
        super.onDestroyView();
        presenter.destroyView();
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
        menu.clear();
        inflater.inflate(R.menu.search, menu);
        MenuItem menuItem = menu.findItem(R.id.search_menu_search_view);
        android.support.v7.widget.SearchView searchView =
                (android.support.v7.widget.SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setIconifiedByDefault(true);
        searchView.setSubmitButtonEnabled(false);
        String queryHint = self.getResources().getString(R.string.search_menu_query_hint_text);
        searchView.setQueryHint(queryHint);
        searchView.onActionViewExpanded();
        searchView.setFocusable(true);
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (searchingClosed) {
            MenuItem menuItem = menu.findItem(R.id.search_menu_search_view);
            android.support.v7.widget.SearchView searchView =
                    (android.support.v7.widget.SearchView) MenuItemCompat.getActionView(menuItem);
            searchView.clearFocus();
            searchView.setIconified(false);
            searchView.onActionViewCollapsed();
            this.setAdViewVisibility(View.VISIBLE);
        } else {
            this.setAdViewVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.getActivity().finish();
                return true;
            case R.id.search_menu_search_view:
                this.searchingClosed = false;
                this.getActivity().supportInvalidateOptionsMenu();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void showFullLoadingView() {
        swipeRefreshLayout.setVisibility(View.GONE);
        fullLoadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideFullLoadingView() {
        swipeRefreshLayout.setVisibility(View.VISIBLE);
        fullLoadingView.setVisibility(View.GONE);
    }

    @Override
    public void showReloadLoadingView() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideReloadLoadingView() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showListFooterLoadingView() {
        getFooterView().findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
    }

    @Override
    public void hideListFooterLoadingView() {
        getFooterView().findViewById(R.id.progressBar).setVisibility(View.GONE);
    }

    @Override
    public void setItems(List<ArticleModel> items) {
        adapter.setItems(items);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void addItems(List<ArticleModel> items) {
        adapter.addItems(items);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setActionBarTitle(String title) {
        listener.setTitle(title);
    }

    @Override
    public void showNoArticleMessage(String searchWord) {
        String title = getString(R.string.search_error_title);
        String message = getString(R.string.search_error_message, searchWord);
        AlertDialogFragment fragment = AlertDialogFragment.newInstance(title, message);
        fragment.show(getActivity().getSupportFragmentManager(), null);
    }

    @Override
    public void showAPIErrorMessage() {
        String title = getString(R.string.api_error_title);
        String message = getString(R.string.api_error_message);
        AlertDialogFragment fragment = AlertDialogFragment.newInstance(title, message);
        fragment.show(getActivity().getSupportFragmentManager(), null);
    }

    @Override
    public void navigateToArticleActivity(String uuid) {
        Intent intent = new Intent(this.getActivity(), ArticleActivity.class);
        intent.putExtra(ArticleActivity.INTENT_ARTICLE_UUID_KEY, uuid);
        listener.navigateTo(intent);
    }

    @Override
    public void changeSearchType(Type type) {
        switch (type) {
            case All:
                presenter.changeSearchALL();
                break;
            case Stocks:
                String token = AppSharedPreference.getToken(this.getActivity());
                presenter.changeSearchStocks(token);
                break;
        }
    }

    private void setAdView() {
        adView = (AdView) getView().findViewById(R.id.basic_list_admob_view);
        adView.setVisibility(View.VISIBLE);
        AdRequest adRequest = AppController.getInstance().getAdRequest();
        adView.loadAd(adRequest);
    }

    private void setAdViewVisibility(int visibility) {
        if (adView != null) {
            adView.setVisibility(visibility);
        }
    }

    private View getFooterView() {
        if (this.footerView == null) {
            LayoutInflater inflater = LayoutInflater.from(this.getActivity());
            this.footerView = inflater.inflate(R.layout.list_item_footer_loading, null);
        }
        return this.footerView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        presenter.onItemClicked(position);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        presenter.onScroll(totalItemCount, firstVisibleItem, visibleItemCount);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        searchingClosed = true;
        this.getActivity().supportInvalidateOptionsMenu();

        View v = getActivity().getCurrentFocus();
        InputMethodManager manager = (InputMethodManager)getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(v.getWindowToken(), 0);
        presenter.queryTextSubmit(s);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

}
