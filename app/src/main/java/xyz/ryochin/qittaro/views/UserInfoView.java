/**
 * PACKAGE NAME xyz.ryochin.qittaro.views
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/14
 */


package xyz.ryochin.qittaro.views;

import android.view.View;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import xyz.ryochin.qittaro.R;
import xyz.ryochin.qittaro.models.UserModel;
import xyz.ryochin.qittaro.utils.AppController;

public class UserInfoView {

    private static final String TAG = UserInfoView.class.getSimpleName();
    private final UserInfoView self = this;

    private View fullLoadingView;
    private View userInfoLayout;
    private NetworkImageView icon;
    private TextView urlName;
    private TextView name;
    private TextView url;
    private TextView description;
    private TextView web_site_url;
    private TextView organization;
    private TextView location;
    private TextView followingUserCount;
    private TextView followerCount;
    private TextView postCount;

    public UserInfoView(View v) {
        this.icon = (NetworkImageView)v.findViewById(R.id.user_info_icon);
        this.urlName = (TextView)v.findViewById(R.id.user_info_url_name);
        this.name = (TextView)v.findViewById(R.id.user_info_name);
        this.url = (TextView)v.findViewById(R.id.user_info_url);
        this.description = (TextView)v.findViewById(R.id.user_info_description);
        this.web_site_url = (TextView)v.findViewById(R.id.user_info_web_site_url);
        this.organization = (TextView)v.findViewById(R.id.user_info_organization);
        this.location = (TextView)v.findViewById(R.id.user_info_location);
        this.followingUserCount = (TextView)v.findViewById(R.id.user_info_following_user_count);
        this.followerCount = (TextView)v.findViewById(R.id.user_info_follower_count);
        this.postCount = (TextView)v.findViewById(R.id.user_info_post_count);
        this.fullLoadingView = v.findViewById(R.id.user_info_loading_layout);
        this.userInfoLayout = v.findViewById(R.id.user_info_scroll_view);

    }

    public void setUserInfo(UserModel model) {
        this.name.setText(model.getName());
        this.url.setText(model.getUrl());
        this.description.setText(model.getDescription());
        this.web_site_url.setText(model.getWebSiteURL());
        this.organization.setText(model.getOrganization());
        this.location.setText(model.getLocation());
        this.urlName.setText(model.getUrlName());
        this.postCount.setText(String.valueOf(model.getItems()));
        this.followingUserCount.setText(String.valueOf(model.getFollowingUsers()));
        this.followerCount.setText(String.valueOf(model.getFollowers()));

        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        this.icon.setImageUrl(model.getProfileImageURL(), imageLoader);
    }

    public void setUserInfoLayoutVisibility(int visibility) {
        this.userInfoLayout.setVisibility(visibility);
    }

    public void setFullLoadingViewVisibility(int visibility) {
        this.fullLoadingView.setVisibility(visibility);
    }
}
