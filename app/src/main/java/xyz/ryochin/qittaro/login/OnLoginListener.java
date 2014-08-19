/**
 * =====================================
 * ENCODE : UTF-8
 * CREATED AT 2014/08/19
 * CREATED BY kosugeryou
 * =====================================
 */
package xyz.ryochin.qittaro.login;

public interface OnLoginListener {
    public void onUserNameError();
    public void onPasswordError();
    public void onAPIError(Exception e);
    public void onFinished(String token);
}
