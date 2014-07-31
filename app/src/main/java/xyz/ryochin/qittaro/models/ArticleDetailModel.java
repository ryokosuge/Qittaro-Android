/**
 * PACKAGE NAME xyz.ryochin.qittaro.apimanagers
 * CREATED BY kosugeryou
 * CREATED AT 2014/07/30
 */
package xyz.ryochin.qittaro.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ArticleDetailModel {
    private static final String TAG = ArticleDetailModel.class.getSimpleName();
    private final ArticleDetailModel self = this;

    private static final String API_ARTICLE_DETAIL_ID_KEY = "id";
    private static final String API_ARTICLE_DETAIL_UUID_KEY = "uuid";
    private static final String API_ARTICLE_DETAIL_USER_KEY = "user";
    private static final String API_ARTICLE_DETAIL_TITLE_KEY = "title";
    private static final String API_ARTICLE_DETAIL_BODY_KEY = "body";
    private static final String API_ARTICLE_DETAIL_CREATED_AT_KEY = "created_at";
    private static final String API_ARTICLE_DETAIL_UPDATED_AT_KEY = "updated_at";
    private static final String API_ARTICLE_DETAIL_CREATED_AT_IN_WORDS_KEY = "created_at_in_words";
    private static final String API_ARTICLE_DETAIL_UPDATED_AT_IN_WORDS_KEY = "updated_at_in_words";
    private static final String API_ARTICLE_DETAIL_TAGS_KEY = "tags";
    private static final String API_ARTICLE_DETAIL_STOCK_COUNT_KEY = "stock_count";
    private static final String API_ARTICLE_DETAIL_STOCK_USERS_KEY = "stock_users";
    private static final String API_ARTICLE_DETAIL_COMMENT_COUNT_KEY = "comment_count";
    private static final String API_ARTICLE_DETAIL_URL_KEY = "url";
    private static final String API_ARTICLE_DETAIL_GIST_URL = "gist_url";
    private static final String API_ARTICLE_DETAIL_TWEET_KEY = "tweet";
    private static final String API_ARTICLE_DETAIL_PRIVATE_KEY = "private";
    private static final String API_ARTICLE_DETAIL_STOCKED_KEY = "stocked";
    private static final String API_ARTICLE_DETAIL_COMMENTS_KEY = "comments";

    private Long id;
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
    private boolean tweet;
    private boolean privated;
    private boolean stocked;
    private List<ArticleCommentModel> comments;

    public ArticleDetailModel(JSONObject jsonResponse) throws JSONException {
        this.id = jsonResponse.getLong(API_ARTICLE_DETAIL_ID_KEY);
        this.uuid = jsonResponse.getString(API_ARTICLE_DETAIL_UUID_KEY);
        this.user = new ArticleUserModel(jsonResponse.getJSONObject(API_ARTICLE_DETAIL_USER_KEY));
        this.title = jsonResponse.getString(API_ARTICLE_DETAIL_TITLE_KEY);
        this.body = jsonResponse.getString(API_ARTICLE_DETAIL_BODY_KEY);
        this.createdAt = jsonResponse.getString(API_ARTICLE_DETAIL_CREATED_AT_KEY);
        this.updatedAt = jsonResponse.getString(API_ARTICLE_DETAIL_UPDATED_AT_KEY);
        this.createdAtInWords = jsonResponse.getString(API_ARTICLE_DETAIL_CREATED_AT_IN_WORDS_KEY);
        this.updatedAtInWords = jsonResponse.getString(API_ARTICLE_DETAIL_UPDATED_AT_IN_WORDS_KEY);
        this.tags = this.parseTags(jsonResponse.getJSONArray(API_ARTICLE_DETAIL_TAGS_KEY));
        this.stockCount = jsonResponse.getInt(API_ARTICLE_DETAIL_STOCK_COUNT_KEY);
        this.stockUsers = this.parseStockUsers(jsonResponse.getJSONArray(API_ARTICLE_DETAIL_STOCK_USERS_KEY));
        this.commentCount = jsonResponse.getInt(API_ARTICLE_DETAIL_COMMENT_COUNT_KEY);
        this.url = jsonResponse.getString(API_ARTICLE_DETAIL_URL_KEY);
        this.gistURL = jsonResponse.getString(API_ARTICLE_DETAIL_GIST_URL);
        this.tweet = jsonResponse.getBoolean(API_ARTICLE_DETAIL_TWEET_KEY);
        this.privated = this.getPrivated(jsonResponse);
        this.stocked = this.getStocked(jsonResponse);
        this.comments = this.parseComments(jsonResponse.getJSONArray(API_ARTICLE_DETAIL_COMMENTS_KEY));
    }

    private boolean getStocked(JSONObject jsonObject) throws JSONException {
        return jsonObject.has(API_ARTICLE_DETAIL_STOCKED_KEY) && jsonObject.getBoolean(API_ARTICLE_DETAIL_STOCKED_KEY);
    }

    private boolean getPrivated(JSONObject jsonObject) throws JSONException {
        return jsonObject.has(API_ARTICLE_DETAIL_PRIVATE_KEY) && jsonObject.getBoolean(API_ARTICLE_DETAIL_PRIVATE_KEY);
    }

    private List<ArticleCommentModel> parseComments(JSONArray jsonArray) throws JSONException {
        int jsonCount = jsonArray.length();
        List<ArticleCommentModel> comments = new ArrayList<ArticleCommentModel>(jsonCount);
        for (int i = 0; i < jsonCount; i++) {
            comments.add(new ArticleCommentModel(jsonArray.getJSONObject(i)));
        }
        return comments;
    }

    private List<String> parseStockUsers(JSONArray jsonArray) throws JSONException {
        int jsonCount = jsonArray.length();
        List<String> stockUsers = new ArrayList<String>(jsonCount);
        for (int i = 0; i < jsonCount; i++) {
            stockUsers.add(jsonArray.getString(i));
        }
        return stockUsers;
    }

    private List<ArticleTagModel> parseTags(JSONArray jsonTags) throws JSONException {
        int jsonTagsCount = jsonTags.length();
        List<ArticleTagModel> tags = new ArrayList<ArticleTagModel>(jsonTagsCount);
        for (int i = 0; i < jsonTagsCount; i++) {
            tags.add(new ArticleTagModel(jsonTags.getJSONObject(i)));
        }
        return tags;
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

    public boolean isTweet() {
        return tweet;
    }

    public boolean isPrivated() {
        return privated;
    }

    public boolean isStocked() {
        return stocked;
    }

    public List<ArticleCommentModel> getComments() {
        return comments;
    }

    public void setStocked(boolean stocked) {
        this.stocked = stocked;
    }
}
