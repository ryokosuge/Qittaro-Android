/**
 * PACKAGE NAME xyz.ryochin.qittaro.models
 * CREATED BY kosugeryou
 * CREATED AT 2014/07/27
 */
package xyz.ryochin.qittaro.models;

import org.json.JSONException;
import org.json.JSONObject;

public class TagModel {
    private static final String TAG = TagModel.class.getSimpleName();
    private final TagModel self = this;

    private static final String API_TAG_NAME_KEY = "name";
    private static final String API_TAG_URL_NAME_KEY = "url_name";
    private static final String API_TAG_ICON_URL_KEY = "icon_url";
    private static final String API_TAG_ITEM_COUNT_KEY = "item_count";
    private static final String API_TAG_FOLLOWER_COUNT_KEY = "follower_count";

    private String name;
    private String urlName;
    private String iconURL;
    private int itemCount;
    private int followerCount;

    public TagModel(JSONObject jsonObject) throws JSONException {
        this.name = jsonObject.getString(API_TAG_NAME_KEY);
        this.urlName = jsonObject.getString(API_TAG_URL_NAME_KEY);
        this.iconURL = jsonObject.getString(API_TAG_ICON_URL_KEY);
        this.itemCount = jsonObject.getInt(API_TAG_ITEM_COUNT_KEY);
        this.followerCount = jsonObject.getInt(API_TAG_FOLLOWER_COUNT_KEY);
    }

    public String getName() {
        return name;
    }

    public String getUrlName() {
        return urlName;
    }

    public String getIconURL() {
        return iconURL;
    }

    public int getItemCount() {
        return itemCount;
    }

    public int getFollowerCount() {
        return followerCount;
    }

}
