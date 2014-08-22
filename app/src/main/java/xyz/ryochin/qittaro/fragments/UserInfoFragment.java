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

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import xyz.ryochin.qittaro.R;
import xyz.ryochin.qittaro.models.UserModel;
import xyz.ryochin.qittaro.utils.AppController;


public class UserInfoFragment extends Fragment {

    private static final String TAG = UserInfoFragment.class.getSimpleName();
    private final UserInfoFragment self = this;
    private static final String ARGS_USER_MODEL_KEY = "userModel";
    private static final String ARGS_SHOW_AD_KEY = "showAd";

    public static UserInfoFragment newInstance(UserModel model, boolean showAd) {
        Bundle args = new Bundle();
        args.putParcelable(ARGS_USER_MODEL_KEY, model);
        args.putBoolean(ARGS_SHOW_AD_KEY, showAd);
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
        UserModel model = args.getParcelable(ARGS_USER_MODEL_KEY);

        NetworkImageView icon = (NetworkImageView) getView().findViewById(R.id.user_info_icon);
        TextView urlName = (TextView) getView().findViewById(R.id.user_info_url_name);
        TextView name = (TextView) getView().findViewById(R.id.user_info_name);
        TextView url = (TextView) getView().findViewById(R.id.user_info_url);
        TextView description = (TextView) getView().findViewById(R.id.user_info_description);
        TextView web_site_url = (TextView) getView().findViewById(R.id.user_info_web_site_url);
        TextView organization = (TextView) getView().findViewById(R.id.user_info_organization);
        TextView location = (TextView) getView().findViewById(R.id.user_info_location);
        TextView followingUserCount = (TextView) getView().findViewById(R.id.user_info_following_user_count);
        TextView followerCount = (TextView) getView().findViewById(R.id.user_info_follower_count);
        TextView postCount = (TextView) getView().findViewById(R.id.user_info_post_count);

        name.setText(model.getName());
        url.setText(model.getUrl());
        description.setText(model.getDescription());
        web_site_url.setText(model.getWebSiteURL());
        organization.setText(model.getOrganization());
        location.setText(model.getLocation());
        urlName.setText(model.getUrlName());
        postCount.setText(String.valueOf(model.getItems()));
        followingUserCount.setText(String.valueOf(model.getFollowingUsers()));
        followerCount.setText(String.valueOf(model.getFollowers()));
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        icon.setImageUrl(model.getProfileImageUrl(), imageLoader);
    }


}
