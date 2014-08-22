/**
 * =====================================================
 * ENCODE : UTF-8
 * CREATED AT 14/08/22.
 * CREATED BY kosuge.
 * Copyright © Samurai Factory Inc. All rights reserved.
 * ===================================================== 
 */

package xyz.ryochin.qittaro.requests;

public class FollowUserRequest extends APIRequest {

    private static final String TAG = FollowUserRequest.class.getSimpleName();
    private final FollowUserRequest self = this;

    private static final String REQUEST_URL_FORMAT = "https://qiita.com/api/v1/users/%1$s";
    private String urlName;

    public FollowUserRequest(String urlName) {
        this.urlName = urlName;
    }

    @Override
    public String getTag() {
        return TAG;
    }

    @Override
    public String toURLString() {
        return String.format(REQUEST_URL_FORMAT, urlName);
    }
}
