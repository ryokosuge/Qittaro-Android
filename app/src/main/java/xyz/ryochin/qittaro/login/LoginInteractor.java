/**
 * =====================================
 * ENCODE : UTF-8
 * CREATED AT 2014/08/19
 * CREATED BY kosugeryou
 * =====================================
 */
package xyz.ryochin.qittaro.login;

import xyz.ryochin.qittaro.api.OnFinishedListener;
import xyz.ryochin.qittaro.requests.Request;

public interface LoginInteractor {
    public void login(Request request, String urlName, String password, OnLoginListener listener);
    public void getUserInfo(Request request, OnFinishedListener listener);
    public void cancel(Request request);
}
