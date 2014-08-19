
package xyz.ryochin.qittaro.requests;

import java.io.Serializable;

public class PublicRequest extends APIRequest implements Serializable {

    private static final String TAG = PublicRequest.class.getSimpleName();
    private static final long serialVersionUID = 8149964268916373193L;
    private final PublicRequest self = this;

    private static final String BASE_URL = "https://qiita.com/api/v1/items";
    private static final String REQUEST_QUERY_FORMAT = "?page=%1$d&per_page=%2$d";

    @Override
    public String getTag() {
        return TAG;
    }

    @Override
    public String toURLString() {
        String request = String.format(REQUEST_QUERY_FORMAT, this.page, this.perPage);
        return BASE_URL + request;
    }
}
