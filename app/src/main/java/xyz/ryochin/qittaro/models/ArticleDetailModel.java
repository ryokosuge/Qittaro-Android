/**
 * PACKAGE NAME xyz.ryochin.qittaro.models
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/21
 */

package xyz.ryochin.qittaro.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class ArticleDetailModel implements Parcelable {

    private static final String TAG = ArticleDetailModel.class.getSimpleName();
    private final ArticleDetailModel self = this;

    private long id;
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
    private boolean tweet;
    private boolean stocked;
    private List<ArticleCommentModel> comments;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(uuid);
        dest.writeParcelable(user, flags);
        dest.writeString(title);
        dest.writeString(body);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        dest.writeString(createdAtInWords);
        dest.writeString(updatedAtInWords);
        dest.writeTypedList(tags);
        dest.writeInt(stockCount);
        dest.writeStringList(stockUsers);
        dest.writeInt(commentCount);
        dest.writeString(url);
        dest.writeString(gistUrl);
        dest.writeByte((byte)(tweet ? 1 : 0));
        dest.writeByte((byte)(stocked ? 1 : 0));
        dest.writeTypedList(comments);
    }

    private ArticleDetailModel(Parcel source) {
        id = source.readLong();
        uuid = source.readString();
        user = source.readParcelable(ArticleUserModel.class.getClassLoader());
        title = source.readString();
        body = source.readString();
        createdAt = source.readString();
        updatedAt = source.readString();
        createdAtInWords = source.readString();
        updatedAtInWords = source.readString();
        tags = source.createTypedArrayList(ArticleTagModel.CREATOR);
        stockCount = source.readInt();
        source.readStringList(stockUsers);
        commentCount = source.readInt();
        url = source.readString();
        gistUrl = source.readString();
        tweet = (source.readByte() != 0);
        stocked = (source.readByte() != 0);
    }

    public static final Creator<ArticleDetailModel> CREATOR = new Creator<ArticleDetailModel>() {
        @Override
        public ArticleDetailModel createFromParcel(Parcel source) {
            return new ArticleDetailModel(source);
        }

        @Override
        public ArticleDetailModel[] newArray(int size) {
            return new ArticleDetailModel[0];
        }
    };

    public long getId() {
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

    public boolean isTweet() {
        return tweet;
    }

    public boolean isStocked() {
        return stocked;
    }

    public void setStocked(boolean stocked) {
        this.stocked = stocked;
    }

    public List<ArticleCommentModel> getComments() {
        return comments;
    }
}
