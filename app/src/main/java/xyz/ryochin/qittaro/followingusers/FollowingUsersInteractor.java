/**
 * PACKAGE NAME xyz.ryochin.qittaro.articles.followingusers
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/21
 */
package xyz.ryochin.qittaro.followingusers;

import java.util.List;

import xyz.ryochin.qittaro.models.FollowingUserModel;
import xyz.ryochin.qittaro.requests.APIRequest;

public interface FollowingUsersInteractor {
    public interface Listener {
        public void onCompleted(List<FollowingUserModel> items, boolean isMax);
        public void onError(Exception e);
    }

    public void getItems(APIRequest request, Listener listener);
    public void addItems(APIRequest request, Listener listener);
    public boolean isMax();
    public boolean isLoading();
    public void cancel(APIRequest request);
}
