/**
 * PACKAGE NAME xyz.ryochin.qittaro.fragments
 * CREATED BY kosugeryou
 * CREATED AT 2014/07/29
 */

package xyz.ryochin.qittaro.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
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
import xyz.ryochin.qittaro.adapters.ArticleAdapter;
import xyz.ryochin.qittaro.apimanagers.APIManagerListener;
import xyz.ryochin.qittaro.apimanagers.SearchArticlesAPIManager;
import xyz.ryochin.qittaro.models.ArticleModel;
import xyz.ryochin.qittaro.utils.AppController;
import xyz.ryochin.qittaro.utils.AppSharedPreference;

public class SearchFragment extends Fragment implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener {

    private static final String TAG = SearchFragment.class.getSimpleName();
    private final SearchFragment self = this;
    private static final String SAVED_SEARCH_WORD_KEY = "searchWord";
    private static final String SAVED_SEARCH_IN_STOCKED_KEY = "searchInStocked";
    private static final String ARGS_SEARCH_WORD_KEY = "searchWord";
    private static final String ARGS_SEARCH_IN_STOCKED_KEY = "searchInStocked";

    public interface Listener {
        public void noSearchArticle(String searchWord);
        public void onItemClicked(ArticleModel model);
        public void setSearchWord(String searchWord);
        public void onOptionMenuClicked(MenuItem menu);
    }

    private SwipeRefreshLayout swipeRefreshLayout;
    private ArticleAdapter adapter;
    private View footerLoadingView;
    private String searchWord;
    private boolean searchInStocked;
    private SearchView searchView;
    private Listener listener;
    private AdView adView;

    public SearchFragment() {}

    public static SearchFragment newInstance(String searchWord, boolean searchInStocked) {
        Bundle args = new Bundle();
        if (searchWord != null && !searchWord.equals("")) {
            args.putString(ARGS_SEARCH_WORD_KEY, searchWord);
        }
        args.putBoolean(ARGS_SEARCH_IN_STOCKED_KEY, searchInStocked);
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
            throw new ClassCastException("Please implement the SearchFragment.Listener.");
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
        this.setAdView();
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(SAVED_SEARCH_WORD_KEY)) {
                this.searchWord = savedInstanceState.getString(SAVED_SEARCH_WORD_KEY);
                ActionBar actionBar = ((ActionBarActivity) this.getActivity()).getSupportActionBar();
                actionBar.setDisplayShowTitleEnabled(true);
                actionBar.setTitle(this.searchWord);
            }

            if (savedInstanceState.containsKey(SAVED_SEARCH_IN_STOCKED_KEY)) {
                this.searchInStocked = savedInstanceState.getBoolean(SAVED_SEARCH_IN_STOCKED_KEY);
            }
        } else {
            Bundle args = this.getArguments();
            this.searchInStocked = (args.containsKey(ARGS_SEARCH_IN_STOCKED_KEY)) && args.getBoolean(ARGS_SEARCH_IN_STOCKED_KEY);
            if (args.containsKey(ARGS_SEARCH_WORD_KEY)) {
                String searchWord = args.getString(ARGS_SEARCH_WORD_KEY);
                if (searchWord != null && !searchWord.equals("")) {
                    this.searchWord = searchWord;
                }
            }
        }

        ListView listView = (ListView) this.getView().findViewById(R.id.article_list_view);
        listView.addFooterView(this.getFooterLoadingView());
        this.hideFooterLoadingView();
        this.swipeRefreshLayout = (SwipeRefreshLayout)this.getView().findViewById(R.id.article_swipe_refresh);
        this.adapter = new ArticleAdapter(this.getActivity());
        listView.setAdapter(this.adapter);
        this.swipeRefreshLayout.setColorSchemeResources(
                R.color.app_first_green_color,
                R.color.app_second_green_color,
                R.color.app_third_green_color,
                R.color.app_fourth_green_color
        );
        this.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                SearchArticlesAPIManager.getInstance().reloadItems(self.managerListener);
            }
        });
        listView.setOnItemClickListener(this);
        listView.setOnScrollListener(this);

        if (this.searchWord != null && !this.searchWord.equals("")) {
            this.getSearchArticle();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.search, menu);
        MenuItem menuItem = menu.findItem(R.id.search_menu_search_view);
        this.searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        this.searchView.setIconifiedByDefault(true);
        this.searchView.setSubmitButtonEnabled(false);
        if (this.searchWord != null && !this.searchWord.equals("")) {
            this.searchView.setQuery(this.searchWord, false);
        } else {
            String queryHint = self.getResources().getString(R.string.search_menu_query_hint_text);
            this.searchView.setQueryHint(queryHint);
            this.searchView.onActionViewExpanded();
        }
        this.searchView.setFocusable(true);
        this.searchView.setOnQueryTextListener(self.onQueryTextListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.listener.onOptionMenuClicked(item);
        return super.onOptionsItemSelected(item);
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

    private SearchView.OnQueryTextListener onQueryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String searchWord) {
            return self.setSearchWord(searchWord);
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }
    };

    private boolean setSearchWord(String searchWord) {
        if (searchWord != null && !searchWord.equals("")) {
            this.searchWord = searchWord;
            this.listener.setSearchWord(searchWord);
            this.getSearchArticle();
        }

        this.searchView.setIconified(false);
        this.searchView.onActionViewCollapsed();
        this.searchView.clearFocus();
        InputMethodManager inputMethodManager =
                (InputMethodManager)this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(this.searchView.getWindowToken(), 0);
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        SearchArticlesAPIManager.getInstance().cancel();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SEARCH_IN_STOCKED_KEY, this.searchInStocked);
        if (this.searchWord != null && this.searchWord.equals("")) {
            outState.putString(SAVED_SEARCH_WORD_KEY, this.searchWord);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        AppController.getInstance().sendView(TAG);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (!SearchArticlesAPIManager.getInstance().isMax()) {
            if (this.adapter.getCount() > 0 && !this.searchWord.equals("") && totalItemCount != 0 && totalItemCount == firstVisibleItem + visibleItemCount) {
                SearchArticlesAPIManager.getInstance().addItems(this.addManagerListener);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ArticleModel articleModel = (ArticleModel)this.adapter.getItem(position);
        this.listener.onItemClicked(articleModel);
    }

    private void getSearchArticle() {
        SearchArticlesAPIManager.getInstance().cancel();
        this.adapter.clear();
        this.adapter.notifyDataSetChanged();
        this.showFooterLoadingView();
        String token = AppSharedPreference.getToken(this.getActivity());
        SearchArticlesAPIManager.getInstance()
                .getItems(this.searchWord, this.searchInStocked, token, this.managerListener);
    }

    private APIManagerListener<ArticleModel> managerListener = new APIManagerListener<ArticleModel>() {
        @Override
        public void willStart() {
            self.showFooterLoadingView();
        }

        @Override
        public void onCompleted(List<ArticleModel> items) {
            if (items.size() > 0) {
                self.adapter.setItems(items);
                self.adapter.notifyDataSetChanged();
                self.swipeRefreshLayout.setRefreshing(false);
                if (SearchArticlesAPIManager.getInstance().isMax()) {
                    self.hideFooterLoadingView();
                } else {
                    self.showFooterLoadingView();
                }
            } else {
                self.hideFooterLoadingView();
                self.listener.noSearchArticle(self.searchWord);
            }
        }

        @Override
        public void onError() {
            self.swipeRefreshLayout.setRefreshing(false);
        }
    };

    private APIManagerListener<ArticleModel> addManagerListener = new APIManagerListener<ArticleModel>() {
        @Override
        public void willStart() {
        }

        @Override
        public void onCompleted(List<ArticleModel> items) {
            self.adapter.addItems(items);
            self.adapter.notifyDataSetChanged();
            if (SearchArticlesAPIManager.getInstance().isMax()) {
                self.hideFooterLoadingView();
            } else {
                self.showFooterLoadingView();
            }
        }

        @Override
        public void onError() {
        }
    };

    private View getFooterLoadingView() {
        if (this.footerLoadingView == null) {
            this.footerLoadingView = this.getActivity()
                    .getLayoutInflater().inflate(R.layout.list_item_footer_loading, null);
        }
        return this.footerLoadingView;
    }

    private void hideFooterLoadingView() {
        View footerLoadingView = this.getFooterLoadingView();
        footerLoadingView.findViewById(R.id.progressBar).setVisibility(View.GONE);
    }

    private void showFooterLoadingView() {
        View footerLoadingView = this.getFooterLoadingView();
        footerLoadingView.findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
    }

    private void setAdView() {
        this.adView = (AdView)this.getView().findViewById(R.id.article_admob_view);
        AdRequest adRequest = new AdRequest.Builder().build();
        this.adView.loadAd(adRequest);
    }
}
