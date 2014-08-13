/**
 * PACKAGE NAME xyz.ryochin.qittaro.fragments
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/13
 */

package xyz.ryochin.qittaro.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import xyz.ryochin.qittaro.R;
import xyz.ryochin.qittaro.apimanagers.UserInfoAPIManager;
import xyz.ryochin.qittaro.models.UserModel;

public class UserInfoFragment extends Fragment {
    private static final String TAG = UserInfoFragment.class.getSimpleName();
    private final UserInfoFragment self = this;
    private static final String ARGS_URL_NAME_KEY = "urlName";

    public static UserInfoFragment newInstance(String urlName) {
        Bundle args = new Bundle();
        args.putString(ARGS_URL_NAME_KEY, urlName);
        UserInfoFragment fragment = new UserInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_info, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle args = this.getArguments();
        String urlName = args.getString(ARGS_URL_NAME_KEY);
        UserInfoAPIManager.getInstance().getItem(urlName, new UserInfoAPIManager.Listener() {
            @Override
            public void willStart() {
                self.setFullLoadingViewVisibility(View.VISIBLE);
                self.setScrollLayoutVisibility(View.GONE);
            }

            @Override
            public void onCompleted(UserModel model) {
                self.setUserInfoView(model);
                self.setFullLoadingViewVisibility(View.GONE);
                self.setScrollLayoutVisibility(View.VISIBLE);
            }

            @Override
            public void onError() {
                self.setFullLoadingViewVisibility(View.GONE);
                self.setScrollLayoutVisibility(View.VISIBLE);
            }
        });
    }

    private void setUserInfoView(UserModel model) {
        ((TextView)this.getView().findViewById(R.id.user_info_name)).setText(model.getName());
        ((TextView)this.getView().findViewById(R.id.user_info_url)).setText(model.getUrl());
        ((TextView)this.getView().findViewById(R.id.user_info_description)).setText(model.getDescription());
        ((TextView)this.getView().findViewById(R.id.user_info_web_site_url)).setText(model.getWebSiteURL());
        ((TextView)this.getView().findViewById(R.id.user_info_organization)).setText(model.getOrganization());
        ((TextView)this.getView().findViewById(R.id.user_info_location)).setText(model.getLocation());
    }

    private void setFullLoadingViewVisibility(int visibility) {
        this.getView().findViewById(R.id.user_info_loading_layout).setVisibility(visibility);
    }

    private void setScrollLayoutVisibility(int visibility) {
        this.getView().findViewById(R.id.user_info_scroll_view).setVisibility(visibility);
    }
}
