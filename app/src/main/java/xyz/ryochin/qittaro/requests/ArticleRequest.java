/**
 * =====================================================
 * ENCODE : UTF-8
 * CREATED AT 14/08/21.
 * CREATED BY kosuge.
 * Copyright © Samurai Factory Inc. All rights reserved.
 * ===================================================== 
 */

package xyz.ryochin.qittaro.requests;

import android.support.annotation.Nullable;

public class ArticleRequest extends APIRequest {

    private static final String TAG = ArticleRequest.class.getSimpleName();
    private final ArticleRequest self = this;

    private static final String BASE_URL = "https://qiita.com/api/v1/items";
    private static final String REQUEST_QUERY_FORMAT = "/%1$s";
    private static final String REQUEST_QUERY_TOKEN_FORMAT = "?token=%1$s";

    private String articleUUID;
    private String token;

    public ArticleRequest(String articleUUID, @Nullable String token) {
        this.articleUUID = articleUUID;
        this.token = token;
    }

    @Override
    public String getTag() {
        return TAG;
    }

    @Override
    public String toURLString() {
        String requestQuery = String.format(REQUEST_QUERY_FORMAT, articleUUID);
        if (token != null) {
            requestQuery += String.format(REQUEST_QUERY_TOKEN_FORMAT, token);
        }
        return BASE_URL + requestQuery;
    }
}
