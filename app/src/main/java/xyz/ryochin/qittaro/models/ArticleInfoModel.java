/**
 * PACKAGE NAME xyz.ryochin.qittaro.apimanagers
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/15
 */

package xyz.ryochin.qittaro.models;

public class ArticleInfoModel {

    private static final String TAG = ArticleInfoModel.class.getSimpleName();
    private final ArticleInfoModel self = this;

    private ArticleInfoModelType type;
    private String title;
    private String iconURL;
    private String body;

    public ArticleInfoModel(ArticleInfoModelType type, String title) {
        if (type != ArticleInfoModelType.Title && type != ArticleInfoModelType.StockUser) {
            throw new IllegalArgumentException("ArticleInfoModelType must be a Title.");
        }

        this.type = type;
        this.title = title;
    }

    public ArticleInfoModel(ArticleInfoModelType type, String title, String iconURL) {
        if (type != ArticleInfoModelType.User) {
            throw new IllegalArgumentException("ArticleInfoModelType must be a User.");
        }

        this.type = type;
        this.title = title;
        this.iconURL = iconURL;
    }

    public ArticleInfoModel(ArticleInfoModelType type, String title, String iconURL, String body) {
        if (type != ArticleInfoModelType.Comment && type != ArticleInfoModelType.Tag) {
            throw new IllegalArgumentException("ArticleInfoModelType must be a Comment.");
        }

        this.type = type;
        this.title = title;
        this.iconURL = iconURL;
        this.body = body;
    }

    public ArticleInfoModelType getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getIconURL() {
        return iconURL;
    }

    public String getBody() {
        return body;
    }
}
