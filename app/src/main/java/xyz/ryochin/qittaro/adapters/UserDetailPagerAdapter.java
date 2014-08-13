/**
 * PACKAGE NAME xyz.ryochin.qittaro.adapters
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/13
 */

package xyz.ryochin.qittaro.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import xyz.ryochin.qittaro.R;
import xyz.ryochin.qittaro.fragments.UserArticlesFragment;
import xyz.ryochin.qittaro.fragments.UserInfoFragment;
import xyz.ryochin.qittaro.fragments.UserStocksFragment;

public class UserDetailPagerAdapter extends FragmentStatePagerAdapter {
    private static final String TAG = UserDetailPagerAdapter.class.getSimpleName();
    private final UserDetailPagerAdapter self = this;

    private static final int PAGER_COUNT = 3;
    private static final int PAGER_USER_INFO_INDEX = 0;
    private static final int PAGER_USER_POST_ARTICLES_INDEX = 1;
    private static final int PAGER_USER_STOCKED_INDEX = 2;
    private static final int PAGER_USER_FOLLOWING_TAGS_INDEX = 3;

    private Context context;
    private String urlName;

    public UserDetailPagerAdapter(FragmentManager fm, Context context, String urlName) {
        super(fm);
        this.context = context;
        this.urlName = urlName;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case PAGER_USER_INFO_INDEX:
                return UserInfoFragment.newInstance(this.urlName);
            case PAGER_USER_POST_ARTICLES_INDEX:
                return UserArticlesFragment.newInstance(this.urlName);
            case PAGER_USER_STOCKED_INDEX:
                return UserStocksFragment.newInstance(this.urlName);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return PAGER_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        int titleResID;
        switch (position) {
            case PAGER_USER_INFO_INDEX:
                titleResID = R.string.user_detail_page_info_title;
                break;
            case PAGER_USER_STOCKED_INDEX:
                titleResID = R.string.user_detail_page_stocked_title;
                break;
            case PAGER_USER_POST_ARTICLES_INDEX:
                titleResID = R.string.user_detail_page_post_article_title;
                break;
            case PAGER_USER_FOLLOWING_TAGS_INDEX:
                titleResID = R.string.user_detail_page_following_tag_title;
                break;
            default:
                titleResID = 0;
                break;
        }

        return this.context.getResources().getString(titleResID);
    }
}
