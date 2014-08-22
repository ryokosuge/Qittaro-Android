/**
 * PACKAGE NAME xyz.ryochin.qittaro.requests
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/22
 */

package xyz.ryochin.qittaro.requests;

public class StockedRequest extends APIRequest {

    private static final String TAG = StockedRequest.class.getSimpleName();
    private final StockedRequest self = this;

    private static final String BASE_URL = "https://qiita.com/api/v1/items";
    private static final String REQUEST_QUERY_FORMAT = "/%1$s/stock?token=%2$s";

    private String token;
    private String articleUUID;

    public StockedRequest(String token, String articleUUID) {
        this.token = token;
        this.articleUUID = articleUUID;
    }

    @Override
    public String getTag() {
        return TAG;
    }

    @Override
    public String toURLString() {
        String requestQuery = String.format(REQUEST_QUERY_FORMAT, articleUUID, token);
        return BASE_URL + requestQuery;
    }
}
