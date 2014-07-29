/**
 * PACKAGE NAME com.ryochin.qittaro.adapters
 * CREATED BY kosugeryou
 * CREATED AT 2014/07/27
 */
package com.ryochin.qittaro.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.ryochin.qittaro.fragments.ArticlesFragment;
import com.ryochin.qittaro.fragments.SearchArticleFragment;
import com.ryochin.qittaro.fragments.TagsFragment;

public class FragmentPagerAdapter extends FragmentStatePagerAdapter {
    private static final String TAG = FragmentPagerAdapter.class.getSimpleName();
    private final FragmentPagerAdapter self = this;

    private static final int FRAGMENT_MAX_COUNT = 3;
    private static final int FRAGMENT_ARTICLE_INDEX = 0;
    private static final int FRAGMENT_TAGS_INDEX = 1;
    private static final int FRAGMENT_SEARCH_INDEX = 2;

    public FragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {
        switch (index) {
            case FRAGMENT_ARTICLE_INDEX:
                return new ArticlesFragment();
            case FRAGMENT_TAGS_INDEX:
                return new TagsFragment();
            case FRAGMENT_SEARCH_INDEX:
                return new SearchArticleFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return FRAGMENT_MAX_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case FRAGMENT_ARTICLE_INDEX:
                return "PUBLIC";
            case FRAGMENT_TAGS_INDEX:
                return "TAGS";
            case FRAGMENT_SEARCH_INDEX:
                return "SEARCH";
            default:
                return "";
        }
    }
}
