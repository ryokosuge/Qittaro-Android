/**
 * PACKAGE NAME xyz.ryochin.qittaro.user
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/22
 */

package xyz.ryochin.qittaro.user;

import xyz.ryochin.qittaro.models.UserModel;

public interface UserView {
    public void showFullLoadingView();
    public void hideFullLoadingView();
    public void setActionBarUserInfo(String urlName, String profileImageUrl);
    public void setActionBarLoadingTitle();
    public void setFragmentAdapter(UserModel model);
    public void showAPIErrorMessage();
}
