
package xyz.ryochin.qittaro.requests;

public class MineRequest extends APIRequest {

    private static final String TAG = MineRequest.class.getSimpleName();
    private static final long serialVersionUID = 7998751616142910044L;
    private final MineRequest self = this;

    private static final String BASE_URL = "https://qiita.com/api/v1/items";
    private static final String REQUEST_QUERY_FORMAT = "?token=%1$s&page=%2$d&per_page=%3$d";
    private String token;

    public MineRequest(String token) {
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
