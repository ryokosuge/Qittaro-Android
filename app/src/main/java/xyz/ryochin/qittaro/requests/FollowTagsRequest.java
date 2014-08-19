

package xyz.ryochin.qittaro.requests;

public class FollowTagsRequest extends APIRequest {

    private static final String TAG = FollowTagsRequest.class.getSimpleName();
    private static final long serialVersionUID = -1086178730261233241L;
    private final FollowTagsRequest self = this;

    private static final String BASE_URL = "https://qiita.com/api/v1/users";
    private static final String REQUEST_QUERY_FORMAT = "/%1$s/following_tags?page=%2$d&per_page=%3$d";
    private String urlName;

    public FollowTagsRequest(String urlName) {
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
