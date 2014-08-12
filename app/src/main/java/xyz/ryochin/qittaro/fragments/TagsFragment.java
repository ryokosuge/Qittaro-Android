/**
 * PACKAGE NAME xyz.ryochin.qittaro.fragments
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/12
 */

package xyz.ryochin.qittaro.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.List;

import xyz.ryochin.qittaro.R;
import xyz.ryochin.qittaro.adapters.TagViewPagerAdapter;
import xyz.ryochin.qittaro.apimanagers.APIManagerListener;
import xyz.ryochin.qittaro.apimanagers.TagsAPIManager;
import xyz.ryochin.qittaro.models.TagModel;

public class TagsFragment extends Fragment {

    private static final String TAG = TagsFragment.class.getSimpleName();
    private final TagsFragment self = this;

    private static final int ADD_TAGS_LOADING_INDICATION = 5;
    private AdView adView;
    private ViewPager viewPager;
    private TagViewPagerAdapter adapter;

    public static TagsFragment newInstance() {
        return new TagsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tags, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.setAdView();
        this.viewPager = (ViewPager)this.getView().findViewById(R.id.fragment_tags_view_pager);
        PagerTabStrip pagerTabStrip = (PagerTabStrip) this.getView().findViewById(R.id.fragment_tags_pager_tab_strip);
        pagerTabStrip.setDrawFullUnderline(true);
        pagerTabStrip.setTabIndicatorColorResource(R.color.apptheme_color);
        TagsAPIManager.getInstance().reloadItems(this.reloadTagListener);
        this.viewPager.setOnPageChangeListener(this.pageChangeListener);
    }

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i2) {
        }

        @Override
        public void onPageSelected(int position) {
            int pageCount = self.adapter.getCount();
            if (ADD_TAGS_LOADING_INDICATION > (pageCount - position)) {
                if (!TagsAPIManager.getInstance().isMax()) {
                    TagsAPIManager.getInstance().addItems(self.addTagListener);
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {
        }
    };

    private APIManagerListener<TagModel> reloadTagListener = new APIManagerListener<TagModel>() {
        @Override
        public void willStart() {
        }

        @Override
        public void onCompleted(List<TagModel> items) {
            self.adapter = new TagViewPagerAdapter(getChildFragmentManager(), items);
            self.viewPager.setAdapter(self.adapter);
            self.viewPager.setCurrentItem(0, true);
        }

        @Override
        public void onError() {
        }
    };

    private APIManagerListener<TagModel> addTagListener = new APIManagerListener<TagModel>() {
        @Override
        public void willStart() {
        }

        @Override
        public void onCompleted(List<TagModel> items) {
            self.adapter.addItems(items);
            self.adapter.notifyDataSetChanged();
        }

        @Override
        public void onError() {
        }
    };

    private void setAdView() {
        this.adView = (AdView)this.getView().findViewById(R.id.fragment_tags_admob_view);
        AdRequest adRequest = new AdRequest.Builder().build();
        this.adView.loadAd(adRequest);
    }

}
