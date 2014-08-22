/**
 * PACKAGE NAME xyz.ryochin.qittaro.user
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/22
 */

package xyz.ryochin.qittaro.user;

import xyz.ryochin.qittaro.models.UserModel;
import xyz.ryochin.qittaro.requests.APIRequest;

public interface UserInteractor {

    public interface Listener {
        public void onCompleted(UserModel model);
        public void onError(Exception e);
    }

    public void getUserInfo(APIRequest request, Listener listener);
    public void cancel(APIRequest request);

}
