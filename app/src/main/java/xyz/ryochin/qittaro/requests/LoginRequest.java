/**
 * =====================================
 * ENCODE : UTF-8
 * CREATED AT 2014/08/19
 * CREATED BY kosugeryou
 * =====================================
 */
package xyz.ryochin.qittaro.requests;

public class LoginRequest extends APIRequest {

    private static final String TAG = LoginRequest.class.getSimpleName();
    private final LoginRequest self = this;
    private static final String BASE_URL = "https://qiita.com/api/v1/auth";

    @Override
    public String getTag() {
        return TAG;
    }

    @Override
    public String toURLString() {
        return BASE_URL;
    }
}
