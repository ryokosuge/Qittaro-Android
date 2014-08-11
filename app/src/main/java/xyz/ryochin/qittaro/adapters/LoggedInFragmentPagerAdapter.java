/**
 * PACKAGE NAME xyz.ryochin.qittaro.adapters
 * CREATED BY kosugeryou
 * CREATED AT 2014/07/28
 */

package xyz.ryochin.qittaro.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import xyz.ryochin.qittaro.fragments.ArticlesFragment;
import xyz.ryochin.qittaro.fragments.MyArticleFragment;
import xyz.ryochin.qittaro.fragments.StocksFragment;

public class LoggedInFragmentPagerAdapter extends FragmentStatePagerAdapter {

    private static final String TAG = LoggedInFragmentPagerAdapter.class.getSimpleName();
    private final LoggedInFragmentPagerAdapter self = this;

    private static final int FRAGMENT_MAX_COUNT = 3;
    private static final int FRAGMENT_ARTICLE_INDEX = 0;
    private static final int FRAGMENT_MY_ARTICLE_INDEX = 2;
    private static final int FRAGMENT_STOCKS_INDEX = 1;

    public LoggedInFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {
        switch (index) {
            case FRAGMENT_ARTICLE_INDEX:
                return new ArticlesFragment();
            case FRAGMENT_STOCKS_INDEX:
                return new StocksFragment();
            case FRAGMENT_MY_ARTICLE_INDEX:
                return new MyArticleFragment();
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
            case FRAGMENT_STOCKS_INDEX:
                return "STOCK";
            case FRAGMENT_MY_ARTICLE_INDEX:
                return "MINE";
            default:
                return "";
        }
    }
}
