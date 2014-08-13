/**
 * PACKAGE NAME xyz.ryochin.qittaro.activities
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/13
 */

package xyz.ryochin.qittaro.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import xyz.ryochin.qittaro.R;
import xyz.ryochin.qittaro.adapters.UserDetailPagerAdapter;
import xyz.ryochin.qittaro.utils.AppController;

public class UserActivity extends ActionBarActivity implements ViewPager.OnPageChangeListener {

    private static final String TAG = UserActivity.class.getSimpleName();
    private final UserActivity self = this;

    public static final String INTENT_USER_PROFILE_IMAGE_URL_KEY = "profileImageURL";
    public static final String INTENT_USER_URL_NAME_KEY = "urlName";

    private static final String SAVE_INSTANCE_CURRENT_INDEX_KEY = "currentIndex";

    private AdView adView;
    private ViewPager viewPager;
    private int currentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_user);

        ActionBar actionBar = this.getSupportActionBar();


        Intent intent = this.getIntent();
        String urlName = intent.getExtras().getString(INTENT_USER_URL_NAME_KEY);
        String profileImageURL = intent.getExtras().getString(INTENT_USER_PROFILE_IMAGE_URL_KEY);
        if (savedInstanceState == null) {
            View customActionBarView = this.getActionBarView(urlName, profileImageURL);

            if (customActionBarView == null) {
                this.finish();
            }

            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setCustomView(customActionBarView);
            actionBar.setDisplayShowCustomEnabled(true);
        }

        this.setAdView();
        this.viewPager = (ViewPager)this.findViewById(R.id.activity_user_view_pager);
        UserDetailPagerAdapter adapter = new UserDetailPagerAdapter(this.getSupportFragmentManager(), this, urlName);
        this.viewPager.setAdapter(adapter);
        PagerTabStrip pagerTabStrip = (PagerTabStrip)this.findViewById(R.id.activity_user_pager_tab_strip);
        pagerTabStrip.setDrawFullUnderline(true);
        pagerTabStrip.setTabIndicatorColorResource(R.color.apptheme_color);
        this.viewPager.setOnPageChangeListener(this);
        this.overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
    }

    @Override
    protected void onStart() {
        super.onStart();
        AppController.getInstance().sendView(TAG);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (this.adView != null) {
            this.adView.pause();
        }
        this.overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (this.adView != null) {
            this.adView.resume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this.adView != null) {
            this.adView.destroy();
        }
    }

    @Override
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVE_INSTANCE_CURRENT_INDEX_KEY, this.currentIndex);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.containsKey(SAVE_INSTANCE_CURRENT_INDEX_KEY)) {
            this.currentIndex = savedInstanceState.getInt(SAVE_INSTANCE_CURRENT_INDEX_KEY);
        } else {
            this.currentIndex = 0;
        }
        this.viewPager.setCurrentItem(this.currentIndex, true);
    }

    @Override
    public void onPageScrolled(int i, float v, int i2) {
    }

    @Override
    public void onPageSelected(int position) {
        this.currentIndex = position;
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    private View getActionBarView(String urlName, String profileImageURL) {
        if (urlName == null || profileImageURL == null) {
            return null;
        }
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.activity_user_action_bar_layout, null);
        TextView textView = (TextView)view.findViewById(R.id.activity_user_action_bar_title);
        textView.setText(urlName);
        NetworkImageView imageView = (NetworkImageView)view.findViewById(R.id.activity_user_action_bar_icon);
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        imageView.setImageUrl(profileImageURL, imageLoader);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                self.finish();
            }
        });
        return view;
    }

    private void setAdView() {
        this.adView = (AdView)this.findViewById(R.id.activity_user_admob_view);
        AdRequest adRequest = new AdRequest.Builder().build();
        this.adView.loadAd(adRequest);
    }
}
