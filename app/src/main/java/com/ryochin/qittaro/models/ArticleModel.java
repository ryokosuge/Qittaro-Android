/**
 * PACKAGE NAME com.ryochin.qittaro.models
 * CREATED BY kosugeryou
 * CREATED AT 2014/07/26
 */
package com.ryochin.qittaro.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ArticleModel {
    private static final String TAG = ArticleModel.class.getSimpleName();
    private final ArticleModel self = this;

    private static final String API_ARTICLE_ID_KEY = "id";
    private static final String API_ARTICLE_UUID_KEY = "uuid";
    private static final String API_ARTICLE_USER_KEY = "user";
    private static final String API_ARTICLE_TITLE_KEY = "title";
    private static final String API_ARTICLE_BODY_KEY = "body";
    private static final String API_ARTICLE_CREATED_AT_KEY = "created_at";
    private static final String API_ARTICLE_UPDATED_AT_KEY = "updated_at";
    private static final String API_ARTICLE_CREATED_AT_IN_WORDS_KEY = "created_at_in_words";
    private static final String API_ARTICLE_UPDATED_AT_IN_WORDS_KEY = "updated_at_in_words";
    private static final String API_ARTICLE_TAGS_KEY = "tags";
    private static final String API_ARTICLE_STOCK_COUNT_KEY = "stock_count";
    private static final String API_ARTICLE_STOCK_USERS_KEY = "stock_users";
    private static final String API_ARTICLE_COMMENT_COUNT_KEY = "comment_count";
    private static final String API_ARTICLE_URL_KEY = "url";
    private static final String API_ARTICLE_GIST_URL_KEY = "gist_url";

    private int id;
    private String uuid;
    private ArticleUserModel user;
    private String title;
    private String body;
    private String createdAt;
    private String updatedAt;
    private String createdAtInWords;
    private String updatedAtInWords;
    private List<ArticleTagModel> tags;
    private int stockCount;
    private List<String> stockUsers;
    private int commentCount;
    private String url;
    private String gistURL;

    public ArticleModel(JSONObject jsonObject) throws JSONException {
        this.id = jsonObject.getInt(API_ARTICLE_ID_KEY);
        this.uuid = jsonObject.getString(API_ARTICLE_UUID_KEY);
        this.user = new ArticleUserModel(jsonObject.getJSONObject(API_ARTICLE_USER_KEY));
        this.title = jsonObject.getString(API_ARTICLE_TITLE_KEY);
        this.body = jsonObject.getString(API_ARTICLE_BODY_KEY);
        this.createdAt = jsonObject.getString(API_ARTICLE_CREATED_AT_KEY);
        this.updatedAt = jsonObject.getString(API_ARTICLE_UPDATED_AT_KEY);
        this.createdAtInWords = jsonObject.getString(API_ARTICLE_CREATED_AT_IN_WORDS_KEY);
        this.updatedAtInWords = jsonObject.getString(API_ARTICLE_UPDATED_AT_IN_WORDS_KEY);
        JSONArray jsonTags = jsonObject.getJSONArray(API_ARTICLE_TAGS_KEY);
        int jsonTagsCount = jsonTags.length();
        this.tags = new ArrayList<ArticleTagModel>(jsonTagsCount);
        for(int i = 0; i < jsonTagsCount; i ++) {
            this.tags.add(new ArticleTagModel(jsonTags.getJSONObject(i)));
        }
        this.stockCount = jsonObject.getInt(API_ARTICLE_STOCK_COUNT_KEY);
        JSONArray jsonStockUsers = jsonObject.getJSONArray(API_ARTICLE_STOCK_USERS_KEY);
        int stockUserCount = jsonStockUsers.length();
        this.stockUsers = new ArrayList<String>(stockUserCount);
        for (int i = 0; i < stockUserCount; i ++) {
            this.stockUsers.add(jsonStockUsers.getString(i));
        }
        this.commentCount = jsonObject.getInt(API_ARTICLE_COMMENT_COUNT_KEY);
        this.url = jsonObject.getString(API_ARTICLE_URL_KEY);
        this.gistURL = jsonObject.getString(API_ARTICLE_GIST_URL_KEY);
    }

    public int getId() {
        return id;
    }

    public String getUuid() {
        return uuid;
    }

    public ArticleUserModel getUser() {
        return user;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getCreatedAtInWords() {
        return createdAtInWords;
    }

    public String getUpdatedAtInWords() {
        return updatedAtInWords;
    }

    public List<ArticleTagModel> getTags() {
        return tags;
    }

    public int getStockCount() {
        return stockCount;
    }

    public List<String> getStockUsers() {
        return stockUsers;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public String getUrl() {
        return url;
    }

    public String getGistURL() {
        return gistURL;
    }

}
