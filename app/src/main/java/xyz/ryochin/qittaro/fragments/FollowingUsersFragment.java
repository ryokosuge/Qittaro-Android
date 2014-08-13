/**
 * PACKAGE NAME xyz.ryochin.qittaro.fragments
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/13
 */


package xyz.ryochin.qittaro.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.List;

import xyz.ryochin.qittaro.R;
import xyz.ryochin.qittaro.adapters.FollowUsersAdapter;
import xyz.ryochin.qittaro.apimanagers.APIManagerListener;
import xyz.ryochin.qittaro.apimanagers.FollowUsersAPIManager;
import xyz.ryochin.qittaro.models.FollowUserModel;
import xyz.ryochin.qittaro.utils.AppController;
import xyz.ryochin.qittaro.utils.AppSharedPreference;

public class FollowingUsersFragment extends Fragment {

    private static final String TAG = FollowingUsersFragment.class.getSimpleName();
    private final FollowingUsersFragment self = this;

    private FragmentListener listener;
    private AdView adView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FollowUsersAdapter adapter;
    private View footerLoadingView;

    public static FollowingUsersFragment newInstance() {
        return new FollowingUsersFragment();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.listener = (FragmentListener)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Please implement the FragmentListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_article, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.setAdView();
        ListView listView = (ListView)this.getView().findViewById(R.id.article_list_view);
        listView.addFooterView(this.getFooterLoadingView());
        this.adapter = new FollowUsersAdapter(this.getActivity());
        listView.setAdapter(this.adapter);
        this.swipeRefreshLayout = (SwipeRefreshLayout)this.getView().findViewById(R.id.article_swipe_refresh);
        this.swipeRefreshLayout.setColorSchemeResources(
                R.color.app_first_green_color,
                R.color.app_second_green_color,
                R.color.app_third_green_color,
                R.color.app_fourth_green_color
        );
        String urlName = AppSharedPreference.getURLName(this.getActivity());
        FollowUsersAPIManager.getInstance().getItems(urlName, this.getItemListener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        FollowUsersAPIManager.getInstance().cancel();
    }

    @Override
    public void onStart() {
        super.onStart();
        AppController.getInstance().sendView(TAG);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (this.adView != null) {
            this.adView.pause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (this.adView != null) {
            this.adView.resume();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (this.adView != null) {
            this.adView.destroy();
        }
    }

    private APIManagerListener<FollowUserModel> getItemListener = new APIManagerListener<FollowUserModel>() {
        @Override
        public void willStart() {
            self.swipeRefreshLayout.setVisibility(View.GONE);
            self.showFullLoadingView();
        }

        @Override
        public void onCompleted(List<FollowUserModel> items) {
            Log.e(TAG, "onCompleted()");
            self.adapter.setItems(items);
            self.adapter.notifyDataSetChanged();
            if (FollowUsersAPIManager.getInstance().isMax()) {
                self.hideFooterLoadingView();
            } else {
                self.showFooterLoadingView();
            }

            self.swipeRefreshLayout.setVisibility(View.VISIBLE);
            self.hideFullLoadingView();
        }

        @Override
        public void onError() {
            self.hideFooterLoadingView();
        }
    };

    private void setAdView() {
        this.adView = (AdView)this.getView().findViewById(R.id.article_admob_view);
        AdRequest adRequest = new AdRequest.Builder().build();
        this.adView.loadAd(adRequest);
    }

    private View getFooterLoadingView() {
        if (this.footerLoadingView == null) {
            this.footerLoadingView = this.getActivity().getLayoutInflater().inflate(R.layout.list_view_footer_loading_layout, null);
        }
        return this.footerLoadingView;
    }

    private void showFooterLoadingView() {
        this.getFooterLoadingView().findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
    }

    private void hideFooterLoadingView() {
        this.getFooterLoadingView().findViewById(R.id.progressBar).setVisibility(View.GONE);
    }

    private void showFullLoadingView() {
        this.getView().findViewById(R.id.article_loading_layout).setVisibility(View.VISIBLE);
    }

    private void hideFullLoadingView() {
        this.getView().findViewById(R.id.article_loading_layout).setVisibility(View.GONE);
    }

}
