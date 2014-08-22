/**
 * PACKAGE NAME xyz.ryochin.qittaro.articles.followingusers
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/20
 */
package xyz.ryochin.qittaro.followingusers;

import java.util.List;

import xyz.ryochin.qittaro.models.FollowingUserModel;

public interface FollowingUsersView {
    public void showFullLoadingView();
    public void hideFullLoadingView();
    public void showReloadLoadingView();
    public void hideReloadLoadingView();
    public void showListFooterLoadingView();
    public void hideListFooterLoadingView();
    public void setItems(List<FollowingUserModel> items);
    public void addItems(List<FollowingUserModel> items);
    public void showAPIErrorMessage();
    public void navigateToUserActivity(String urlName);
}
