/**
 * PACKAGE NAME xyz.ryochin.qittaro.activities
 * CREATED BY kosugeryou
 * CREATED AT 2014/07/23
 */

package xyz.ryochin.qittaro.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import xyz.ryochin.qittaro.R;
import xyz.ryochin.qittaro.adapters.FragmentPagerAdapter;
import xyz.ryochin.qittaro.adapters.LoggedInFragmentPagerAdapter;
import xyz.ryochin.qittaro.fragments.AlertDialogFragment;
import xyz.ryochin.qittaro.fragments.FragmentListener;
import xyz.ryochin.qittaro.models.ArticleModel;
import xyz.ryochin.qittaro.utils.AppSharedPreference;


public class HomeActivity extends ActionBarActivity implements FragmentListener {

    private static final String TAG = HomeActivity.class.getSimpleName();
    private final HomeActivity self = this;

    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        this.viewPager = (ViewPager)this.findViewById(R.id.view_pager);
        this.viewPager.setOnPageChangeListener(this.pageChangeListener);
        if (AppSharedPreference.isLoggedIn(this)) {
            this.setLoggedInAdapter();
        } else {
            this.setNoLoggedInAdapter();
        }
        this.overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
    }

    @Override
    protected void onPause() {
        this.overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
        super.onPause();
    }

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i2) {
        }

        @Override
        public void onPageSelected(int i) {
            self.getSupportActionBar().setSelectedNavigationItem(i);
            View view = self.getCurrentFocus();
            if (view != null) {
                InputMethodManager inputMethodManager =
                        (InputMethodManager)self.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {
        }
    };

    private ActionBar.TabListener tabListener = new ActionBar.TabListener() {
        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
            self.viewPager.setCurrentItem(tab.getPosition(), true);
        }

        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        }

        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean loggedIn = AppSharedPreference.isLoggedIn(this);
        menu.findItem(R.id.home_menu_following_tags).setVisible(loggedIn);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.home_menu_search:
                intent = new Intent(this, SearchActivity.class);
                this.startActivity(intent);
                break;
            case R.id.home_menu_tags:
                intent = new Intent(this, TagActivity.class);
                this.startActivity(intent);
                break;
            case R.id.home_menu_following_tags:
                intent = new Intent(this, FollowingTagActivity.class);
                this.startActivity(intent);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCompletedLoggedin(boolean result) {
        if (result) {
            this.setLoggedInAdapter();
            this.supportInvalidateOptionsMenu();
            Toast.makeText(this, R.string.login_success_message, Toast.LENGTH_SHORT).show();
        } else {
            String title = this.getResources().getString(R.string.login_error_title);
            String message = this.getResources().getString(R.string.login_error_message);
            AlertDialogFragment alertDialogFragment = AlertDialogFragment.newInstance(title, message);
            alertDialogFragment.show(this.getSupportFragmentManager(), null);
        }
    }

    private void setNoLoggedInAdapter() {
        Log.e(TAG, "setNoLoggedInAdapter()");
        FragmentPagerAdapter pagerAdapter = new FragmentPagerAdapter(this.getSupportFragmentManager());
        this.viewPager.setAdapter(pagerAdapter);
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.removeAllTabs();
        for (int i = 0; i < pagerAdapter.getCount(); i++) {
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(pagerAdapter.getPageTitle(i))
                            .setTabListener(this.tabListener)
            );
        }
        this.viewPager.setCurrentItem(0, true);
    }

    private void setLoggedInAdapter() {
        Log.e(TAG, "setLoggedInAdapter()");
        LoggedInFragmentPagerAdapter pagerAdapter = new LoggedInFragmentPagerAdapter(this.getSupportFragmentManager());
        this.viewPager.setAdapter(pagerAdapter);
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.removeAllTabs();
        for (int i = 0; i < pagerAdapter.getCount(); i++) {
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(pagerAdapter.getPageTitle(i))
                            .setTabListener(this.tabListener)
            );
        }
        this.viewPager.setCurrentItem(0, true);
    }

    @Override
    public void onItemSelected(ArticleModel model) {
        Intent intent = new Intent(this, ArticleDetailActivity.class);
        intent.putExtra(ArticleDetailActivity.INTENT_ARTICLE_UUID_KEY, model.getUuid());
        this.startActivity(intent);
    }

    @Override
    public void showSearchEmptyMessage(String searchWord) {
        String title = "「" + searchWord + "」の検索結果";
        String message = "投稿が見つかりませんでした。";
        AlertDialogFragment alertDialogFragment = AlertDialogFragment.newInstance(title, message);
        alertDialogFragment.show(this.getSupportFragmentManager(), null);
    }
}
