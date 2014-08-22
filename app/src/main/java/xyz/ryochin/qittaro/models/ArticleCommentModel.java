/**
 * PACKAGE NAME xyz.ryochin.qittaro.models
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/21
 */
package xyz.ryochin.qittaro.models;

import android.os.Parcel;
import android.os.Parcelable;

public class ArticleCommentModel implements Parcelable {

    private static final String TAG = ArticleCommentModel.class.getSimpleName();
    private final ArticleCommentModel self = this;

    private long id;
    private String uuid;
    private ArticleUserModel user;
    private String body;

    public long getId() {
        return id;
    }

    public String getUuid() {
        return uuid;
    }

    public ArticleUserModel getUser() {
        return user;
    }

    public String getBody() {
        return body;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(uuid);
        dest.writeParcelable(user, flags);
        dest.writeString(body);
    }

    public static final Creator<ArticleCommentModel> CREATOR = new Creator<ArticleCommentModel>() {
        @Override
        public ArticleCommentModel createFromParcel(Parcel source) {
            return new ArticleCommentModel(source);
        }

        @Override
        public ArticleCommentModel[] newArray(int size) {
            return new ArticleCommentModel[0];
        }
    };

    private ArticleCommentModel(Parcel source) {
        id = source.readLong();
        uuid = source.readString();
        user = source.readParcelable(ArticleUserModel.class.getClassLoader());
        body = source.readString();
    }
}
