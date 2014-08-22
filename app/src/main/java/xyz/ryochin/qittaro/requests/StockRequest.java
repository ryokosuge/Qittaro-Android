/**
 * PACKAGE NAME xyz.ryochin.qittaro.requests
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/22
 */
package xyz.ryochin.qittaro.requests;

public class StockRequest extends APIRequest {


    private static final String TAG = StockRequest.class.getSimpleName();
    private static final long serialVersionUID = -600573120424479014L;
    private final StockRequest self = this;

    private static final String BASE_URL = "https://qiita.com/api/v1/stocks";
    private static final String REQUEST_QUERY_FORMAT = "?token=%1$s&page=%2$d&per_page=%3$d";

    private String token;

    public StockRequest(String token) {
        this.token = token;
    }

    @Override
    public String getTag() {
        return TAG;
    }

    @Override
    public String toURLString() {
        String requestQuery = String.format(REQUEST_QUERY_FORMAT, token, page, perPage);
        return BASE_URL + requestQuery;
    }
}
