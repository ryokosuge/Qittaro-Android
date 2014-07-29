package com.ryochin.qittaro.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.ryochin.qittaro.R;
import com.ryochin.qittaro.adapters.FragmentPagerAdapter;
import com.ryochin.qittaro.adapters.LoggedInFragmentPagerAdapter;
import com.ryochin.qittaro.fragments.ArticlesFragmentListener;
import com.ryochin.qittaro.models.ArticleModel;
import com.ryochin.qittaro.utils.AppSharedPreference;


public class HomeActivity extends ActionBarActivity implements ArticlesFragmentListener {

    private static final int RESULT_CODE_FOR_LOGIN = 1;
    private static final String TAG = HomeActivity.class.getSimpleName();
    private final HomeActivity self = this;

    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        this.viewPager = (ViewPager)this.findViewById(R.id.view_pager);
        this.viewPager.setOnPageChangeListener(this.pageChangeListener);
        if (AppSharedPreference.isLoggedIn(this)) {
            this.setLoggedInAdapter();
        } else {
            this.setNoLoggedInAdapter();
        }
    }

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i2) {
        }

        @Override
        public void onPageSelected(int i) {
            self.getSupportActionBar().setSelectedNavigationItem(i);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_login:
                Intent intent = new Intent(self, LoginActivity.class);
                self.startActivityForResult(intent, RESULT_CODE_FOR_LOGIN);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "requestCode = " + requestCode);
        Log.e(TAG, "resultCode = " + resultCode);
        switch (resultCode) {
            case LoginActivity.LOGIN_RESULT_OK:
                this.setLoggedInAdapter();
                break;
            case LoginActivity.LOGIN_RESULT_NG:
                this.setNoLoggedInAdapter();
                break;
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
    }

    @Override
    public void onItemSelected(ArticleModel model) {
        Intent intent = new Intent(this, ArticleDetailActivity.class);
        intent.putExtra(ArticleDetailActivity.INTENT_ARTICLE_URL_KEY, model.getUrl());
        this.startActivity(intent);
    }
}
