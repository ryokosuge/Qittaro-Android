/**
 * PACKAGE NAME xyz.ryochin.qittaro.models
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/12
 */

package xyz.ryochin.qittaro.models;

import org.json.JSONException;
import org.json.JSONObject;

public class UserModel {
    private static final String TAG = UserModel.class.getSimpleName();
    private final UserModel self = this;

    private static final String API_USER_ID_KEY = "id";
    private static final String API_USER_URL_NAME_KEY = "url_name";
    private static final String API_USER_PROFILE_IMAGE_URL_KEY = "profile_image_url";
    private static final String API_USER_NAME_KEY = "name";
    private static final String API_USER_URL_KEY = "url";
    private static final String API_USER_DESCRIPTION_KEY = "description";
    private static final String API_USER_WEB_SITE_URL_KEY = "website_url";
    private static final String API_USER_ORGANIZATION_KEY = "organization";
    private static final String API_USER_LOCATION_KEY = "location";
    private static final String API_USER_FACEBOOK_KEY = "facebook";
    private static final String API_USER_LINKEDIN_KEY = "linkedin";
    private static final String API_USER_TWITTER_KEY = "twitter";
    private static final String API_USER_GITHUB_KEY = "github";
    private static final String API_USER_FOLLOWERS_KEY = "followers";
    private static final String API_USER_FOLLOWING_USERS_KEY = "following_users";
    private static final String API_USER_ITEMS_KEY = "items";

    private Long id;
    private String urlName;
    private String profileImageURL;
    private String name;
    private String url;
    private String description;
    private String webSiteURL;
    private String organization;
    private String location;
    private String facebook;
    private String linkedin;
    private String twitter;
    private String github;
    private int followers;
    private int followingUsers;
    private int items;

    public UserModel(JSONObject jsonObject) throws JSONException {
        this.id = jsonObject.getLong(API_USER_ID_KEY);
        this.urlName = jsonObject.getString(API_USER_URL_NAME_KEY);
        this.profileImageURL = jsonObject.getString(API_USER_PROFILE_IMAGE_URL_KEY);
        this.name = jsonObject.getString(API_USER_NAME_KEY);
        this.url = jsonObject.getString(API_USER_URL_KEY);
        this.description = jsonObject.getString(API_USER_DESCRIPTION_KEY);
        this.webSiteURL = jsonObject.getString(API_USER_WEB_SITE_URL_KEY);
        this.organization = jsonObject.getString(API_USER_ORGANIZATION_KEY);
        this.location = jsonObject.getString(API_USER_LOCATION_KEY);
        this.facebook = jsonObject.getString(API_USER_FACEBOOK_KEY);
        this.linkedin = jsonObject.getString(API_USER_LINKEDIN_KEY);
        this.twitter = jsonObject.getString(API_USER_TWITTER_KEY);
        this.github = jsonObject.getString(API_USER_GITHUB_KEY);
        this.followers = jsonObject.getInt(API_USER_FOLLOWERS_KEY);
        this.followingUsers = jsonObject.getInt(API_USER_FOLLOWING_USERS_KEY);
        this.items = jsonObject.getInt(API_USER_ITEMS_KEY);
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

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getDescription() {
        return description;
    }

    public String getWebSiteURL() {
        return webSiteURL;
    }

    public String getOrganization() {
        return organization;
    }

    public String getLocation() {
        return location;
    }

    public String getFacebook() {
        return facebook;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public String getTwitter() {
        return twitter;
    }

    public String getGithub() {
        return github;
    }

    public int getFollowers() {
        return followers;
    }

    public int getFollowingUsers() {
        return followingUsers;
    }

    public int getItems() {
        return items;
    }
}
