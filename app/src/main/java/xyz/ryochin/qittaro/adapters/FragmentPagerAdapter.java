/**
 * PACKAGE NAME xyz.ryochin.qittaro.adapters
 * CREATED BY kosugeryou
 * CREATED AT 2014/07/27
 */
package xyz.ryochin.qittaro.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import xyz.ryochin.qittaro.fragments.ArticlesFragment;
import xyz.ryochin.qittaro.fragments.LoginFragment;
import xyz.ryochin.qittaro.fragments.SearchArticleFragment;
import xyz.ryochin.qittaro.fragments.TagsFragment;

public class FragmentPagerAdapter extends FragmentStatePagerAdapter {
    private static final String TAG = FragmentPagerAdapter.class.getSimpleName();
    private final FragmentPagerAdapter self = this;

    private static final int FRAGMENT_MAX_COUNT = 4;
    private static final int FRAGMENT_ARTICLE_INDEX = 0;
    private static final int FRAGMENT_TAGS_INDEX = 1;
    private static final int FRAGMENT_SEARCH_INDEX = 2;
    private static final int FRAGMENT_LOGIN_INDEX = 3;

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
            case FRAGMENT_LOGIN_INDEX:
                return new LoginFragment();
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
            case FRAGMENT_LOGIN_INDEX:
                return "LOGIN";
            default:
                return "";
        }
    }
}
