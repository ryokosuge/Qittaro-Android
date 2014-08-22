
/**
 * PACKAGE NAME xyz.ryochin.qittaro.requests
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/22
 */
package xyz.ryochin.qittaro.requests;

public class UserRequest extends APIRequest {

    private static final String TAG = UserRequest.class.getSimpleName();
    private final UserRequest self = this;

    private static final String BASE_URL = "https://qiita.com/api/v1/user";
    private static final String REQUEST_QUERY_FORMAT = "?token=%1$s";
    private String token;

    public UserRequest(String token) {
        this.token = token;
    }

    @Override
    public String getTag() {
        return TAG;
    }

    @Override
    public String toURLString() {
        String requestQuery = String.format(REQUEST_QUERY_FORMAT, token);
        return BASE_URL + requestQuery;
    }
}
