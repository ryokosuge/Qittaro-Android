/**
 * PACKAGE NAME xyz.ryochin.qittaro.models
 * CREATED BY kosugeryou
 * CREATED AT 2014/07/26
 */
package xyz.ryochin.qittaro.models;

import org.json.JSONException;
import org.json.JSONObject;

public class ArticleUserModel {
    private static final String TAG = ArticleUserModel.class.getSimpleName();
    private final ArticleUserModel self = this;

    private static final String API_ARTICLE_ID_KEY = "id";
    private static final String API_ARTICLE_USER_URL_NAME_KEY = "url_name";
    private static final String API_ARTICLE_USER_PROFILE_IMAGE_URL_KEY = "profile_image_url";

    private Long id;
    private String urlName;
    private String profileImageURL;

    public ArticleUserModel(JSONObject jsonObject) throws JSONException {
        this.id = jsonObject.getLong(API_ARTICLE_ID_KEY);
        this.urlName = jsonObject.getString(API_ARTICLE_USER_URL_NAME_KEY);
        this.profileImageURL = jsonObject.getString(API_ARTICLE_USER_PROFILE_IMAGE_URL_KEY);
    }

    public Long getId() {
        return id;
    }

    public String getUrlName() {
        return urlName;
    }

    public String getProfileImageURL() {
        return profileImageURL;
    }
}
