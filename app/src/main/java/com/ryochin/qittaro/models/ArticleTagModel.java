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

public class ArticleTagModel {
    private static final String TAG = ArticleTagModel.class.getSimpleName();
    private final ArticleTagModel self = this;

    private static final String API_ARTICLE_TAG_NAME_KEY = "name";
    private static final String API_ARTICLE_TAG_URL_NAME_KEY = "url_name";
    private static final String API_ARTICLE_TAG_ICON_URL_KEY = "icon_url";
    private static final String API_ARTICLE_TAG_VERSIONS_KEY = "versions";

    private String name;
    private String urlName;
    private String iconURL;
    private List<String> versions;

    public ArticleTagModel(JSONObject jsonObject) throws JSONException {
        this.name = jsonObject.getString(API_ARTICLE_TAG_NAME_KEY);
        this.urlName = jsonObject.getString(API_ARTICLE_TAG_URL_NAME_KEY);
        this.iconURL = jsonObject.getString(API_ARTICLE_TAG_ICON_URL_KEY);
        JSONArray jsonVersions = jsonObject.getJSONArray(API_ARTICLE_TAG_VERSIONS_KEY);
        int jsonArrayCount = jsonVersions.length();
        this.versions = new ArrayList<String>(jsonArrayCount);
        for (int i = 0; i < jsonArrayCount; i ++) {
            this.versions.add(jsonVersions.getString(i));
        }
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

    public String getVersionAtIndex(int index) {
        return versions.get(index);
    }

    public List<String> getVersions() {
        return versions;
    }

}
