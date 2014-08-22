/**
 * PACKAGE NAME xyz.ryochin.qittaro.login
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/21
 */
package xyz.ryochin.qittaro.login;

import xyz.ryochin.qittaro.api.OnFinishedListener;
import xyz.ryochin.qittaro.requests.Request;

public interface LoginInteractor {
    public void login(Request request, String urlName, String password, OnLoginListener listener);
    public void getUserInfo(Request request, OnFinishedListener listener);
    public void cancel(Request request);
}
