/**
 * =====================================================
 * ENCODE : UTF-8
 * CREATED AT 14/08/11.
 * CREATED BY kosuge.
 * Copyright © Samurai Factory Inc. All rights reserved.
 * ===================================================== 
 */

package xyz.ryochin.qittaro.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import xyz.ryochin.qittaro.R;
import xyz.ryochin.qittaro.adapters.TagViewPagerAdapter;
import xyz.ryochin.qittaro.apimanagers.APIManagerListener;
import xyz.ryochin.qittaro.apimanagers.FollowTagsAPIManager;
import xyz.ryochin.qittaro.fragments.FragmentListener;
import xyz.ryochin.qittaro.models.ArticleModel;
import xyz.ryochin.qittaro.models.TagModel;
import xyz.ryochin.qittaro.utils.AppSharedPreference;

public class FollowingTagActivity extends ActionBarActivity implements ViewPager.OnPageChangeListener, FragmentListener{

    private static final String TAG = FollowingTagActivity.class.getSimpleName();
    private final FollowingTagActivity self = this;
    private static final int ADD_TAGS_LOADING_INDICATION = 5;
    private ViewPager viewPager;
    private TagViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_tag);

        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        this.viewPager = (ViewPager)this.findViewById(R.id.tag_view_pager);
        this.viewPager.setOnPageChangeListener(this);

        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(R.string.tag_logged_in_title);
        String urlName = AppSharedPreference.getURLName(this);
        FollowTagsAPIManager.getInstance().getItems(urlName, this.reloadTagListener);
        this.overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
    }

    private APIManagerListener<TagModel> addTagListener = new APIManagerListener<TagModel>() {
        @Override
        public void willStart() {
            Log.e(TAG, "willStart()");
        }

        @Override
        public void onCompleted(List<TagModel> items) {
            int currentTabIndex = self.getSupportActionBar().getSelectedNavigationIndex();
            self.adapter.addItems(items);
            ActionBar actionBar = self.getSupportActionBar();
            self.addActionBarTabs(actionBar, items);
            self.adapter.notifyDataSetChanged();
            self.getSupportActionBar().setSelectedNavigationItem(currentTabIndex);
        }

        @Override
        public void onError() {
        }
    };

    private APIManagerListener<TagModel> reloadTagListener = new APIManagerListener<TagModel>() {
        @Override
        public void willStart() {
            Log.e(TAG, "willStart");
            self.showFullLoadingView();
        }

        @Override
        public void onCompleted(List<TagModel> items) {
            Log.e(TAG, "onCompleted()");
            self.adapter = new TagViewPagerAdapter(self.getSupportFragmentManager(), items);
            self.viewPager.setAdapter(self.adapter);
            ActionBar actionBar = self.getSupportActionBar();
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
            actionBar.removeAllTabs();
            self.addActionBarTabs(actionBar, items);
            self.viewPager.setVisibility(View.VISIBLE);
            self.hideFullLoadingView();
            self.viewPager.setCurrentItem(0, true);
        }

        @Override
        public void onError() {
        }
    };

    private void addActionBarTabs(final ActionBar actionBar, List<TagModel> items) {
        for (TagModel tagModel : items) {
            ActionBar.Tab tab = actionBar.newTab()
                    .setText(tagModel.getName())
                    .setTabListener(self.tabListener);
            actionBar.addTab(tab);
        }
    }

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

    private void showFullLoadingView() {
        View fullLoadingView = this.findViewById(R.id.tag_loading_layout);
        fullLoadingView.setVisibility(View.VISIBLE);
    }

    private void hideFullLoadingView() {
        View fullLoadingView = this.findViewById(R.id.tag_loading_layout);
        fullLoadingView.setVisibility(View.GONE);
    }

    @Override
    public void onPageScrolled(int i, float v, int i2) {
    }

    @Override
    public void onPageSelected(int position) {
        self.getSupportActionBar().setSelectedNavigationItem(position);
        int pageCount = this.adapter.getCount();
        if (ADD_TAGS_LOADING_INDICATION > (pageCount - position)) {
            if (!FollowTagsAPIManager.getInstance().isMax()) {
                FollowTagsAPIManager.getInstance().addItems(this.addTagListener);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int position) {
    }

    @Override
    public void showSearchEmptyMessage(String searchWord) {
    }

    @Override
    public void onItemSelected(ArticleModel model) {
        Intent intent = new Intent(this, ArticleDetailActivity.class);
        intent.putExtra(ArticleDetailActivity.INTENT_ARTICLE_UUID_KEY, model.getUuid());
        this.startActivity(intent);
    }

    @Override
    public void onCompletedLoggedin(boolean result) {
    }
}
