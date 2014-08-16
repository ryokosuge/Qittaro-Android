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

import java.util.List;

import xyz.ryochin.qittaro.R;
import xyz.ryochin.qittaro.apimanagers.APIManagerListener;
import xyz.ryochin.qittaro.apimanagers.SearchArticlesAPIManager;
import xyz.ryochin.qittaro.models.ArticleModel;
import xyz.ryochin.qittaro.utils.AppController;
import xyz.ryochin.qittaro.utils.AppSharedPreference;
import xyz.ryochin.qittaro.views.ArticleListView;

public class SearchFragment extends Fragment implements ArticleListView.Listener {

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

    private ArticleListView view;
    private SearchView searchView;
    private Listener listener;
    private String searchWord;
    private boolean searchInStocked;

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
        return inflater.inflate(R.layout.basic_list_view_layout, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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

        this.view = new ArticleListView(this.getActivity(), this.getView(), true, this);

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
        this.view.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.view.resume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.view.destroy();
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
    public void onRefresh() {
        this.getSearchArticle();
    }

    @Override
    public void onItemClicked(ArticleModel model) {
        this.listener.onItemClicked(model);
    }

    @Override
    public void onScrollEnd() {
        if (!SearchArticlesAPIManager.getInstance().isMax()) {
            SearchArticlesAPIManager.getInstance().addItems(this.addManagerListener);
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

    private void getSearchArticle() {
        SearchArticlesAPIManager.getInstance().cancel();
        String token = AppSharedPreference.getToken(this.getActivity());
        SearchArticlesAPIManager.getInstance()
                .getItems(this.searchWord, this.searchInStocked, token, this.managerListener);
    }

    private APIManagerListener<ArticleModel> managerListener = new APIManagerListener<ArticleModel>() {
        @Override
        public void willStart() {
            self.view.setFooterLoadingViewVisibility(View.GONE);
            self.view.setSwipeRefreshVisibility(View.GONE);
            self.view.setFullLoadingViewVisibility(View.VISIBLE);
        }

        @Override
        public void onCompleted(List<ArticleModel> items) {
            if (items.size() > 0) {
                self.view.setItems(items);
                if (SearchArticlesAPIManager.getInstance().isMax()) {
                    self.view.setFooterLoadingViewVisibility(View.GONE);
                } else {
                    self.view.setFooterLoadingViewVisibility(View.VISIBLE);
                }
            } else {
                self.view.setFooterLoadingViewVisibility(View.GONE);
                self.listener.noSearchArticle(self.searchWord);
            }
            self.view.setSwipeRefreshVisibility(View.VISIBLE);
            self.view.setFullLoadingViewVisibility(View.GONE);
        }

        @Override
        public void onError() {
            self.view.setFooterLoadingViewVisibility(View.GONE);
            self.view.setFullLoadingViewVisibility(View.GONE);
            self.view.setSwipeRefreshVisibility(View.VISIBLE);
        }
    };

    private APIManagerListener<ArticleModel> addManagerListener = new APIManagerListener<ArticleModel>() {
        @Override
        public void willStart() {
        }

        @Override
        public void onCompleted(List<ArticleModel> items) {
            self.view.addItems(items);
            if (SearchArticlesAPIManager.getInstance().isMax()) {
                self.view.setFooterLoadingViewVisibility(View.GONE);
            } else {
                self.view.setFooterLoadingViewVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onError() {
        }
    };
}
