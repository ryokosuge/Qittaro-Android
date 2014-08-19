
package xyz.ryochin.qittaro.followingusers;

import java.util.List;

import xyz.ryochin.qittaro.followingusers.models.FollowingUserModel;

public interface FollowingUsersView {
    public void showFullLoadingView();
    public void hideFullLoadingView();
    public void showReloadLoadingView();
    public void hideReloadLoadingView();
    public void showListFooterLoadingView();
    public void hideListFooterLoadingView();
    public void setItems(List<FollowingUserModel> items);
    public void addItems(List<FollowingUserModel> items);
    public void showMessage(String title, String message);
    public void navigateToUserActivity(String urlName);
}
