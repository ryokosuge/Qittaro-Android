/**
 * PACKAGE NAME xyz.ryochin.qittaro.models
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/20
 */
package xyz.ryochin.qittaro.models;

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
