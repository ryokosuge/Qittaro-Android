/**
 * =====================================
 * ENCODE : UTF-8
 * CREATED AT 2014/08/20
 * CREATED BY kosugeryou
 * =====================================
 */
package xyz.ryochin.qittaro.login;

public class LoginResultModel {

    private static final String TAG = LoginResultModel.class.getSimpleName();
    private final LoginResultModel self = this;

    private String urlName;
    private String token;

    public String getUrlName() {
        return urlName;
    }

    public String getToken() {
        return token;
    }
}
