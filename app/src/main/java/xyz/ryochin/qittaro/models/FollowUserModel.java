/**
 * PACKAGE NAME xyz.ryochin.qittaro.models
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/13
 */

package xyz.ryochin.qittaro.models;

import org.json.JSONException;
import org.json.JSONObject;

public class FollowUserModel {

    private static final String TAG = FollowUserModel.class.getSimpleName();

    private static final String API_FOLLOW_USER_ID_KEY = "id";
    private static final String API_FOLLOW_USER_URL_NAME_KEY = "url_name";
    private static final String API_FOLLOW_PROFILE_IMAGE_URL_KEY = "profile_image_url";

    private long id;
    private String urlName;
    private String profileImageURL;

    public FollowUserModel(JSONObject jsonObject) throws JSONException {
        this.id = jsonObject.getLong(API_FOLLOW_USER_ID_KEY);
        this.urlName = jsonObject.getString(API_FOLLOW_USER_URL_NAME_KEY);
        this.profileImageURL = jsonObject.getString(API_FOLLOW_PROFILE_IMAGE_URL_KEY);
    }

    public long getId() {
        return id;
    }

    public String getUrlName() {
        return urlName;
    }

    public String getProfileImageURL() {
        return profileImageURL;
    }
}
