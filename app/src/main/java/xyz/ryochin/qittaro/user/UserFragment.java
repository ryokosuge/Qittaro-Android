/**
 * PACKAGE NAME xyz.ryochin.qittaro.user
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/22
 */

package xyz.ryochin.qittaro.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import xyz.ryochin.qittaro.R;
import xyz.ryochin.qittaro.adapters.UserDetailPagerAdapter;
import xyz.ryochin.qittaro.fragments.AlertDialogFragment;
import xyz.ryochin.qittaro.models.UserModel;
import xyz.ryochin.qittaro.utils.AppController;

public class UserFragment extends Fragment implements UserView, ViewPager.OnPageChangeListener {

    private static final String TAG = UserFragment.class.getSimpleName();
    private final UserFragment self = this;

    private static final String ARGS_USER_URL_NAME_KEY = "urlName";
    private static final String ARGS_SHOW_USER_TITLE_KEY = "showTitle";

    private ViewPager viewPager;
    private View fullLoadingView;
    private boolean showTitle;
    private UserPresenter presenter;
    private AdView adView;

    public static UserFragment newInstance(String urlName, boolean showTitle) {
        Bundle args = new Bundle();
        args.putString(ARGS_USER_URL_NAME_KEY, urlName);
        args.putBoolean(ARGS_SHOW_USER_TITLE_KEY, showTitle);
        UserFragment fragment = new UserFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.basic_view_pager_layout, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle args =  getArguments();
        String urlName = args.getString(ARGS_USER_URL_NAME_KEY);
        this.showTitle = args.getBoolean(ARGS_SHOW_USER_TITLE_KEY);
        this.viewPager = (ViewPager)getView().findViewById(R.id.basic_view_pager);
        this.fullLoadingView = getView().findViewById(R.id.basic_view_pager_loading_layout);
        PagerTabStrip pagerTabStrip = (PagerTabStrip)getView().findViewById(R.id.basic_view_pager_tab_strip);
        pagerTabStrip.setDrawFullUnderline(true);
        pagerTabStrip.setTabIndicatorColorResource(R.color.apptheme_color);
        this.viewPager.setOnPageChangeListener(this);
        this.presenter = new UserPresenterImpl(this, urlName);
        setAdView();
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.start();
        AppController.getInstance().sendView(TAG);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (adView != null) {
            adView.pause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (adView != null) {
            adView.destroy();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.destroyView();
    }

    @Override
    public void showFullLoadingView() {
        fullLoadingView.setVisibility(View.VISIBLE);
        viewPager.setVisibility(View.GONE);
    }

    @Override
    public void hideFullLoadingView() {
        fullLoadingView.setVisibility(View.GONE);
        viewPager.setVisibility(View.VISIBLE);
    }

    @Override
    public void setActionBarUserInfo(String urlName, String profileImageUrl) {
        if (!showTitle) {
            return;
        }
        LayoutInflater inflater = LayoutInflater.from(this.getActivity());
        View view = inflater.inflate(R.layout.action_bar_icon_layout, null);
        TextView textView = (TextView)view.findViewById(R.id.action_bar_title);
        textView.setText(urlName);
        NetworkImageView imageView = (NetworkImageView)view.findViewById(R.id.action_bar_icon);
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        imageView.setImageUrl(profileImageUrl, imageLoader);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                self.getActivity().finish();
            }
        });
        ActionBar actionBar = ((ActionBarActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(view);
    }

    @Override
    public void setActionBarLoadingTitle() {
        if (!showTitle) {
            return;
        }
        ActionBar actionBar = ((ActionBarActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.article_detail_loading_title);
    }

    @Override
    public void setFragmentAdapter(UserModel model) {
        UserDetailPagerAdapter adapter = new UserDetailPagerAdapter(
                this.getActivity().getSupportFragmentManager(),
                this.getActivity(), model
        );
        viewPager.setAdapter(adapter);
    }

    @Override
    public void showAPIErrorMessage() {
        String title = getString(R.string.api_error_title);
        String message = getString(R.string.api_error_message);
        AlertDialogFragment fragment = AlertDialogFragment.newInstance(title, message);
        fragment.show(getActivity().getSupportFragmentManager(), null);
    }

    @Override
    public void onPageScrolled(int i, float v, int i2) {
    }

    @Override
    public void onPageSelected(int i) {
    }

    @Override
    public void onPageScrollStateChanged(int i) {
    }

    private void setAdView() {
        adView = (AdView) getView().findViewById(R.id.basic_view_pager_admob_view);
        adView.setVisibility(View.VISIBLE);
        AdRequest adRequest = AppController.getInstance().getAdRequest();
        adView.loadAd(adRequest);
    }
}
