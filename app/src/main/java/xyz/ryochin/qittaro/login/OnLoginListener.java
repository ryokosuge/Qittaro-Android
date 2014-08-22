
/**
 * PACKAGE NAME xyz.ryochin.qittaro.login
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/21
 */
package xyz.ryochin.qittaro.login;

public interface OnLoginListener {
    public void onUserNameError();
    public void onPasswordError();
    public void onAPIError(Exception e);
    public void onFinished(String token);
}
