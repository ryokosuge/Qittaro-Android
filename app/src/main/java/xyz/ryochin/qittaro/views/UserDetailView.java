/**
 * PACKAGE NAME xyz.ryochin.qittaro.views
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/14
 */


package xyz.ryochin.qittaro.views;

import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import xyz.ryochin.qittaro.R;
import xyz.ryochin.qittaro.adapters.UserDetailPagerAdapter;

public class UserDetailView implements ViewPager.OnPageChangeListener {

    public interface Listener {
        public void onChangedPageIndex(int pageIndex);
    }

    private static final String TAG = UserDetailView.class.getSimpleName();
    private final UserDetailView self = this;

    private ViewPager viewPager;
    private View fullLoadingView;
    private AdView adView;
    private Listener listener;

    public UserDetailView(View v, boolean showAdView, Listener listener) {
        try {
            this.viewPager = (ViewPager)v.findViewById(R.id.basic_view_pager);
            PagerTabStrip pagerTabStrip = (PagerTabStrip)v.findViewById(R.id.basic_view_pager_tab_strip);
            pagerTabStrip.setDrawFullUnderline(true);
            pagerTabStrip.setTabIndicatorColorResource(R.color.apptheme_color);
            this.viewPager.setOnPageChangeListener(this);
            if (showAdView) {
                this.setAdView(v);
            }
            this.fullLoadingView = v.findViewById(R.id.basic_view_pager_loading_layout);
            this.listener = listener;
        } catch (NullPointerException e) {
            throw new NullPointerException("layout resource id use R.layout.basic_view_pager_layout.");
        }
    }

    public void setAdapter(UserDetailPagerAdapter adapter) {
        this.viewPager.setAdapter(adapter);
    }

    public void setPageIndex(int pageIndex) {
        this.viewPager.setCurrentItem(pageIndex, true);
    }

    public void pause() {
        if (this.adView != null) {
            this.adView.pause();
        }
    }

    public void resume() {
        if (this.adView != null) {
            this.adView.resume();
        }
    }

    public void destroy() {
        if (this.adView != null) {
            this.adView.destroy();
        }
    }

    public void showFullLoadingView() {
        this.fullLoadingView.setVisibility(View.VISIBLE);
        this.viewPager.setVisibility(View.GONE);
    }

    public void hideFullLoadingView() {
        this.fullLoadingView.setVisibility(View.GONE);
        this.viewPager.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPageScrolled(int position, float v, int i2) {
        this.listener.onChangedPageIndex(position);
    }

    @Override
    public void onPageSelected(int position) {
        this.listener.onChangedPageIndex(position);
    }

    @Override
    public void onPageScrollStateChanged(int i) {
    }

    private void setAdView(View v) throws NullPointerException {
        this.adView = (AdView)v.findViewById(R.id.basic_view_pager_admob_view);
        AdRequest adRequest = new AdRequest.Builder().build();
        this.adView.loadAd(adRequest);
    }
}
