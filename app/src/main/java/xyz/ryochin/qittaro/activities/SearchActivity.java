/**
 * PACKAGE NAME xyz.ryochin.qittaro.activities
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/14
 */

package xyz.ryochin.qittaro.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

import xyz.ryochin.qittaro.R;
import xyz.ryochin.qittaro.adapters.SearchSpinnerAdapter;
import xyz.ryochin.qittaro.articles.search.SearchFragment;
import xyz.ryochin.qittaro.articles.search.SearchView;
import xyz.ryochin.qittaro.utils.AppSharedPreference;

public class SearchActivity extends ActionBarActivity implements ActionBar.OnNavigationListener, SearchFragment.Listener {

    private static final String TAG = SearchActivity.class.getSimpleName();
    private final SearchActivity self = this;

    private static final int SEARCH_SPINNER_ALL_INDEX = 0;
    private static final int SEARCH_SPINNER_STOCKS_INDEX = 1;

    private SearchSpinnerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basic_fragment_container_layout);
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            Fragment fragment = SearchFragment.newInstance(false, true);
            this.getSupportFragmentManager().beginTransaction()
                    .add(R.id.basic_fragment_container, fragment)
                    .commit();
        }

        if (AppSharedPreference.isLoggedIn(this)) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
            String[] spinnerStrings = this.getResources().getStringArray(R.array.search_spinner_titles);
            this.adapter = new SearchSpinnerAdapter(this, spinnerStrings);
            actionBar.setListNavigationCallbacks(this.adapter, this);
        } else {
            actionBar.setDisplayShowTitleEnabled(true);
        }
        this.overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
    }

    @Override
    public boolean onNavigationItemSelected(int i, long l) {
        SearchView searchView = (SearchView)this.getSupportFragmentManager()
                .findFragmentById(R.id.basic_fragment_container);
        switch (i) {
            case SEARCH_SPINNER_ALL_INDEX:
                searchView.changeSearchType(SearchFragment.Type.All);
                break;
            case SEARCH_SPINNER_STOCKS_INDEX:
                searchView.changeSearchType(SearchFragment.Type.Stocks);
                break;
        }
        return false;
    }

    @Override
    public void setTitle(String title) {
        if (AppSharedPreference.isLoggedIn(this)) {
            this.adapter.setSearchWord(title);
            this.adapter.notifyDataSetChanged();
        } else {
            ActionBar actionBar = this.getSupportActionBar();
            actionBar.setTitle(title);
            actionBar.setDisplayShowTitleEnabled(true);
        }
    }

    @Override
    public void navigateTo(Intent intent) {
        startActivity(intent);
    }

}
