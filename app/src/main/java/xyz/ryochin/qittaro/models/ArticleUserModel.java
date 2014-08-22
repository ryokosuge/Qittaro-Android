/**
 * PACKAGE NAME xyz.ryochin.qittaro.models
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/20
 */


package xyz.ryochin.qittaro.models;

import android.os.Parcel;
import android.os.Parcelable;

public class ArticleUserModel implements Parcelable {

    private Long id;
    private String urlName;
    private String profileImageUrl;

    public Long getId() {
        return id;
    }

    public String getUrlName() {
        return urlName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(urlName);
        dest.writeString(profileImageUrl);
    }

    public static final Creator<ArticleUserModel> CREATOR = new Creator<ArticleUserModel>() {
        @Override
        public ArticleUserModel createFromParcel(Parcel source) {
            return new ArticleUserModel(source);
        }

        @Override
        public ArticleUserModel[] newArray(int size) {
            return new ArticleUserModel[0];
        }
    };

    private ArticleUserModel(Parcel source) {
        id = source.readLong();
        urlName = source.readString();
        profileImageUrl = source.readString();
    }
}
