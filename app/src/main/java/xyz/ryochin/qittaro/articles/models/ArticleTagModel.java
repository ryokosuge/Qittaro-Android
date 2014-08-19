
package xyz.ryochin.qittaro.articles.models;

import java.util.List;

public class ArticleTagModel {

    private String name;
    private String urlName;
    private String iconUrl;
    private List<String> versions;

    public String getName() {
        return name;
    }

    public String getUrlName() {
        return urlName;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public List<String> getVersions() {
        return versions;
    }
}
