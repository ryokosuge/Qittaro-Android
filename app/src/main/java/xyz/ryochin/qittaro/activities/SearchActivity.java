/**
 * PACKAGE NAME xyz.ryochin.qittaro.views
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/14
 */

package xyz.ryochin.qittaro.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import xyz.ryochin.qittaro.R;
import xyz.ryochin.qittaro.adapters.SearchSpinnerAdapter;
import xyz.ryochin.qittaro.fragments.AlertDialogFragment;
import xyz.ryochin.qittaro.fragments.SearchFragment;
import xyz.ryochin.qittaro.models.ArticleModel;
import xyz.ryochin.qittaro.utils.AppSharedPreference;

public class SearchActivity extends ActionBarActivity implements SearchFragment.Listener {

    private static final String TAG = SearchActivity.class.getSimpleName();
    private final SearchActivity self = this;

    private static final int SPINNER_FRAGMENT_SEARCH_IN_ARTICLES_POSITION = 0;
    private static final int SPINNER_FRAGMENT_SEARCH_IN_STOCKS_POSITION = 1;
    private static final String SPINNER_SELECTED_NAVIGATION_ITEM_INDEX_KEY = "selectItemIndex";
    private static final String BUNDLE_SEARCH_WORD_KEY = "searchWord";

    private SearchSpinnerAdapter adapter;
    private String searchWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            FragmentManager fragmentManager = this.getSupportFragmentManager();
            SearchFragment fragment = SearchFragment.newInstance(null, false);
            fragmentManager.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }

        if (AppSharedPreference.isLoggedIn(this)) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
            String[] spinnerStrings = this.getResources().getStringArray(R.array.search_spinner_titles);
            this.adapter = new SearchSpinnerAdapter(this, spinnerStrings);
            this.adapter.setSearchWord(this.searchWord);
            actionBar.setListNavigationCallbacks(this.adapter, this.onNavigationListener);
        } else {
            actionBar.setDisplayShowTitleEnabled(true);
        }
        this.overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
    }

    private ActionBar.OnNavigationListener onNavigationListener = new ActionBar.OnNavigationListener() {
        @Override
        public boolean onNavigationItemSelected(int position, long id) {
            Fragment fragment;
            switch (position) {
                case SPINNER_FRAGMENT_SEARCH_IN_ARTICLES_POSITION:
                    fragment = SearchFragment.newInstance(self.searchWord, false);
                    break;
                case SPINNER_FRAGMENT_SEARCH_IN_STOCKS_POSITION:
                    fragment = SearchFragment.newInstance(self.searchWord, true);
                    break;
                default:
                    fragment = null;
            }

            if (fragment != null) {
                self.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment).commit();
            }
            return false;
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        this.overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState.containsKey(BUNDLE_SEARCH_WORD_KEY)) {
            String searchWord = savedInstanceState.getString(BUNDLE_SEARCH_WORD_KEY);
            if (searchWord != null && !searchWord.equals("")) {
                this.searchWord = searchWord;
            }
        }

        if (savedInstanceState.containsKey(SPINNER_SELECTED_NAVIGATION_ITEM_INDEX_KEY)) {
            int selectedNavigationIndex = savedInstanceState.getInt(SPINNER_SELECTED_NAVIGATION_ITEM_INDEX_KEY);
            this.getSupportActionBar().setSelectedNavigationItem(selectedNavigationIndex);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        int selectedNavigationIndex = this.getSupportActionBar().getSelectedNavigationIndex();
        outState.putInt(SPINNER_SELECTED_NAVIGATION_ITEM_INDEX_KEY, selectedNavigationIndex);
        if (this.searchWord != null && !this.searchWord.equals("")) {
            outState.putString(BUNDLE_SEARCH_WORD_KEY, this.searchWord);
        }
    }

    @Override
    public void onItemClicked(ArticleModel model) {
        Intent intent = new Intent(this, ArticleDetailActivity.class);
        intent.putExtra(ArticleDetailActivity.INTENT_ARTICLE_UUID_KEY, model.getUuid());
        this.startActivity(intent);
    }

    @Override
    public void onOptionMenuClicked(MenuItem menu) {
        switch(menu.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void noSearchArticle(String searchWord) {
        String title = this.getResources().getString(R.string.search_empty_title);
        StringBuilder builder = new StringBuilder();
        String message = builder.append(this.getResources().getString(R.string.search_empty_message))
                .append("\n")
                .append(this.getResources().getString(R.string.search_empty_search_word_top))
                .append(" [")
                .append(searchWord)
                .append("]").toString();
        AlertDialogFragment fragment = AlertDialogFragment.newInstance(title, message);
        fragment.show(this.getSupportFragmentManager(), null);
    }

    @Override
    public void setSearchWord(String searchWord) {
        this.searchWord = searchWord;
        if (AppSharedPreference.isLoggedIn(this)) {
            this.adapter.setSearchWord(this.searchWord);
            this.adapter.notifyDataSetChanged();
        } else {
            ActionBar actionBar = this.getSupportActionBar();
            actionBar.setTitle(searchWord);
            actionBar.setDisplayShowTitleEnabled(true);
        }
    }
}
