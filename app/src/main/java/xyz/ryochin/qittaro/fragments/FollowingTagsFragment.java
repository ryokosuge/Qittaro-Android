/**
 * PACKAGE NAME xyz.ryochin.qittaro.fragments
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/12
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
import xyz.ryochin.qittaro.apimanagers.FollowTagsAPIManager;
import xyz.ryochin.qittaro.apimanagers.UserStocksAPIManager;
import xyz.ryochin.qittaro.models.TagModel;
import xyz.ryochin.qittaro.utils.AppController;
import xyz.ryochin.qittaro.utils.AppSharedPreference;
import xyz.ryochin.qittaro.views.TagListView;

public class FollowingTagsFragment extends Fragment implements TagListView.Listener {

    private static final String TAG = FollowingTagsFragment.class.getSimpleName();
    private final FollowingTagsFragment self = this;


    private FragmentListener listener;
    private TagListView tagListView;
    private String urlName;

    public static FollowingTagsFragment newInstance() {
        return new FollowingTagsFragment();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if ((activity instanceof FragmentListener)) {
            this.listener = (FragmentListener) activity;
        } else {
            throw new ClassCastException("Please implement the FragmentListener.");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.basic_list_view_layout, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.tagListView = new TagListView(this.getActivity(), this.getView(), true, this);
        this.urlName = AppSharedPreference.getURLName(this.getActivity());
        FollowTagsAPIManager.getInstance().getItems(this.urlName, this.getAPIManagerListener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        UserStocksAPIManager.getInstance().cancel();
    }

    @Override
    public void onStart() {
        super.onStart();
        AppController.getInstance().sendView(TAG);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.tagListView.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.tagListView.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.tagListView.destroy();
    }

    @Override
    public void onRefresh() {
        FollowTagsAPIManager.getInstance().reloadItems(this.urlName, this.reloadAPIManagerListener);
    }

    @Override
    public void onItemClicked(TagModel model) {
        this.listener.onItemSelected(model);
    }

    @Override
    public void onScrollEnd() {
        if (!FollowTagsAPIManager.getInstance().isMax()) {
            FollowTagsAPIManager.getInstance().addItems(this.addAPIManagerListener);
        }
    }

    private APIManagerListener<TagModel> getAPIManagerListener = new APIManagerListener<TagModel>() {

        @Override
        public void willStart() {
            self.tagListView.setFullLoadingViewVisibility(View.VISIBLE);
            self.tagListView.setSwipeRefreshVisibility(View.GONE);
        }

        @Override
        public void onCompleted(List<TagModel> items) {
            self.tagListView.setItems(items);
            if (FollowTagsAPIManager.getInstance().isMax()) {
                self.tagListView.setFooterLoadingViewVisibility(View.GONE);
            } else {
                self.tagListView.setFooterLoadingViewVisibility(View.VISIBLE);
            }
            self.tagListView.setSwipeRefreshVisibility(View.VISIBLE);
            self.tagListView.setFullLoadingViewVisibility(View.GONE);
        }

        @Override
        public void onError() {
            self.tagListView.setFullLoadingViewVisibility(View.GONE);
            self.tagListView.setSwipeRefreshVisibility(View.VISIBLE);
        }
    };

    private APIManagerListener<TagModel> reloadAPIManagerListener = new APIManagerListener<TagModel>() {

        @Override
        public void willStart() {
            self.tagListView.setRefresh(true);
        }

        @Override
        public void onCompleted(List<TagModel> items) {
            self.tagListView.setItems(items);
            if (FollowTagsAPIManager.getInstance().isMax()) {
                self.tagListView.setFooterLoadingViewVisibility(View.GONE);
            } else {
                self.tagListView.setFooterLoadingViewVisibility(View.VISIBLE);
            }
            self.tagListView.setRefresh(false);
        }

        @Override
        public void onError() {
            self.tagListView.setRefresh(false);
            self.tagListView.setFooterLoadingViewVisibility(View.GONE);
        }
    };

    private APIManagerListener<TagModel> addAPIManagerListener = new APIManagerListener<TagModel>() {
        @Override
        public void willStart() {
        }

        @Override
        public void onCompleted(List<TagModel> items) {
            self.tagListView.addItems(items);
            if (FollowTagsAPIManager.getInstance().isMax()) {
                self.tagListView.setFooterLoadingViewVisibility(View.GONE);
            } else {
                self.tagListView.setFooterLoadingViewVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onError() {
            self.tagListView.setFooterLoadingViewVisibility(View.GONE);
        }
    };
}
