

package xyz.ryochin.qittaro.articles.models;

import java.util.List;

public class ArticleModel {

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
    private String gistUrl;
    private boolean stocked;

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

    public String getGistUrl() {
        return gistUrl;
    }

    public boolean isStocked() {
        return stocked;
    }
}
