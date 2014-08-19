

package xyz.ryochin.qittaro.requests;

public class UserStocksRequest extends APIRequest {

    private static final String TAG = UserStocksRequest.class.getSimpleName();
    private static final long serialVersionUID = -1140689972292188065L;
    private final UserStocksRequest self = this;

    private static final String BASE_URL = "https://qiita.com/api/v1/users";
    private static final String REQUEST_QUERY_FORMAT = "/%1$s/stocks?page=%2$d&per_page=%3$d";
    private String urlName;

    public UserStocksRequest(String urlName) {
        this.urlName = urlName;
    }

    @Override
    public String getTag() {
        return TAG;
    }

    @Override
    public String toURLString() {
        String requestQuery = String.format(REQUEST_QUERY_FORMAT, urlName, page, perPage);
        return BASE_URL + requestQuery;
    }
}
