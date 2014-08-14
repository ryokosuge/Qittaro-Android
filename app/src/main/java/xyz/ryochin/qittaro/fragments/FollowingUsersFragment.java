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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import xyz.ryochin.qittaro.R;
import xyz.ryochin.qittaro.apimanagers.APIManagerListener;
import xyz.ryochin.qittaro.apimanagers.FollowUsersAPIManager;
import xyz.ryochin.qittaro.models.FollowUserModel;
import xyz.ryochin.qittaro.utils.AppController;
import xyz.ryochin.qittaro.utils.AppSharedPreference;
import xyz.ryochin.qittaro.views.FollowUserListView;

public class FollowingUsersFragment extends Fragment implements FollowUserListView.Listener {

    private static final String TAG = FollowingUsersFragment.class.getSimpleName();
    private final FollowingUsersFragment self = this;

    private FragmentListener listener;
    private FollowUserListView view;

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
        return inflater.inflate(R.layout.basic_list_view_layout, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.view = new FollowUserListView(this.getActivity(), this.getView(), true, this);
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
        this.view.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.view.resume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.view.destroy();
    }

    @Override
    public void onRefresh() {
        String urlName = AppSharedPreference.getURLName(this.getActivity());
        FollowUsersAPIManager.getInstance().reloadItems(urlName, this.reloadItemListener);
    }

    @Override
    public void onItemClicked(FollowUserModel model) {
        this.listener.onItemSelected(model);
    }

    @Override
    public void onScrollEnd() {
        if (!FollowUsersAPIManager.getInstance().isMax()) {
            FollowUsersAPIManager.getInstance().addItems(this.addItemListener);
        }
    }

    private APIManagerListener<FollowUserModel> getItemListener = new APIManagerListener<FollowUserModel>() {
        @Override
        public void willStart() {
            self.view.setSwipeRefreshVisibility(View.GONE);
            self.view.setFullLoadingViewVisibility(View.VISIBLE);
        }

        @Override
        public void onCompleted(List<FollowUserModel> items) {
            self.view.setItems(items);
            if (FollowUsersAPIManager.getInstance().isMax()) {
                self.view.setFooterLoadingViewVisibility(View.GONE);
            } else {
                self.view.setFooterLoadingViewVisibility(View.VISIBLE);
            }
            self.view.setSwipeRefreshVisibility(View.VISIBLE);
            self.view.setFullLoadingViewVisibility(View.GONE);
        }

        @Override
        public void onError() {
            self.view.setSwipeRefreshVisibility(View.INVISIBLE);
            self.view.setFullLoadingViewVisibility(View.GONE);
        }
    };

    private APIManagerListener<FollowUserModel> reloadItemListener = new APIManagerListener<FollowUserModel>() {
        @Override
        public void willStart() {
            self.view.setRefresh(true);
        }

        @Override
        public void onCompleted(List<FollowUserModel> items) {
            self.view.setItems(items);
            if (FollowUsersAPIManager.getInstance().isMax()) {
                self.view.setFooterLoadingViewVisibility(View.GONE);
            } else {
                self.view.setFooterLoadingViewVisibility(View.VISIBLE);
            }
            self.view.setRefresh(false);
        }

        @Override
        public void onError() {
            self.view.setRefresh(false);
        }
    };

    private APIManagerListener<FollowUserModel> addItemListener = new APIManagerListener<FollowUserModel>() {
        @Override
        public void willStart() {
        }

        @Override
        public void onCompleted(List<FollowUserModel> items) {
            self.view.addItems(items);
            if (FollowUsersAPIManager.getInstance().isMax()) {
                self.view.setFooterLoadingViewVisibility(View.GONE);
            } else {
                self.view.setFooterLoadingViewVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onError() {
            self.view.setFooterLoadingViewVisibility(View.GONE);
        }
    };
}
