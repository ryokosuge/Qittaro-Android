/**
 * =====================================================
 * ENCODE : UTF-8
 * CREATED AT 14/07/30.
 * CREATED BY kosuge.
 * Copyright © Samurai Factory Inc. All rights reserved.
 * ===================================================== 
 */

package com.ryochin.qittaro.models;

import org.json.JSONException;
import org.json.JSONObject;

public class ArticleCommentModel {
    private static final String TAG = ArticleCommentModel.class.getSimpleName();
    private final ArticleCommentModel self = this;

    private static final String API_ARTICLE_COMMENT_ID_KEY = "id";
    private static final String API_ARTICLE_COMMENT_UUID_KEY = "uuid";
    private static final String API_ARTICLE_COMMENT_USER_KEY = "user";
    private static final String API_ARTICLE_COMMENT_BODY_KEY = "body";

    private Long id;
    private String uuid;
    private ArticleUserModel user;
    private String body;

    public ArticleCommentModel(JSONObject jsonResponse) throws JSONException {
        this.id = jsonResponse.getLong(API_ARTICLE_COMMENT_ID_KEY);
        this.uuid = jsonResponse.getString(API_ARTICLE_COMMENT_UUID_KEY);
        this.user = new ArticleUserModel(jsonResponse.getJSONObject(API_ARTICLE_COMMENT_USER_KEY));
        this.body = jsonResponse.getString(API_ARTICLE_COMMENT_BODY_KEY);
    }

    public Long getId() {
        return id;
    }

    public String getUuid() {
        return uuid;
    }

    public ArticleUserModel getUser() {
        return user;
    }

    public String getBody() {
        return body;
    }
}
