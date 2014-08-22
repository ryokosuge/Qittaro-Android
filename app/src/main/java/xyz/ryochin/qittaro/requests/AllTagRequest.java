/**
 * PACKAGE NAME xyz.ryochin.qittaro.requests
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/22
 */

package xyz.ryochin.qittaro.requests;

public class AllTagRequest extends APIRequest {

    private static final String TAG = AllTagRequest.class.getSimpleName();
    private static final long serialVersionUID = -2507392348749219603L;
    private final AllTagRequest self = this;

    private static final String BASE_URL = "https://qiita.com/api/v1/tags";
    private static final String REQUEST_QUERY_FORMAT = "?page=%1$d&per_page=%2$d";

    @Override
    public String getTag() {
        return TAG;
    }

    @Override
    public String toURLString() {
        String requestQuery = String.format(REQUEST_QUERY_FORMAT, page, perPage);
        return BASE_URL + requestQuery;
    }
}
