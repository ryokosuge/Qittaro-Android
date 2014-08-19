

package xyz.ryochin.qittaro.tags.models;

public class TagModel {

    private static final String TAG = TagModel.class.getSimpleName();
    private final TagModel self = this;

    private String name;
    private String urlName;
    private String iconUrl;

    public String getName() {
        return name;
    }

    public String getUrlName() {
        return urlName;
    }

    public String getIconUrl() {
        return iconUrl;
    }
}
