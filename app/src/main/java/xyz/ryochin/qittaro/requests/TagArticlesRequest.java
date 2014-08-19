
package xyz.ryochin.qittaro.requests;

public class TagArticlesRequest extends APIRequest {

    private static final String TAG = TagArticlesRequest.class.getSimpleName();
    private static final long serialVersionUID = 3798943703605512864L;
    private final TagArticlesRequest self = this;

    private static final String BASE_URL = "https://qiita.com/api/v1/tags";
    private static final String REQUEST_QUERY_FORMAT = "/%1$s/items?page=%2$d&per_page=%3$d";
    private String tagName;

    public TagArticlesRequest(String tagName) {
        this.tagName = tagName;
    }

    @Override
    public String getTag() {
        return TAG;
    }

    @Override
    public String toURLString() {
        String requestQuery = String.format(REQUEST_QUERY_FORMAT, tagName, page, perPage);
        return BASE_URL + requestQuery;
    }
}
