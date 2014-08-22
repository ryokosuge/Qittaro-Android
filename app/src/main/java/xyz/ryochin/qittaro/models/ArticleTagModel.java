/**
 * PACKAGE NAME xyz.ryochin.qittaro.models
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/20
 */

package xyz.ryochin.qittaro.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class ArticleTagModel implements Parcelable {

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(urlName);
        dest.writeString(iconUrl);
        dest.writeStringList(versions);
    }

    public static final Creator<ArticleTagModel> CREATOR = new Creator<ArticleTagModel>() {
        @Override
        public ArticleTagModel createFromParcel(Parcel source) {
            return new ArticleTagModel(source);
        }

        @Override
        public ArticleTagModel[] newArray(int size) {
            return new ArticleTagModel[0];
        }
    };

    private ArticleTagModel(Parcel source) {
        name = source.readString();
        urlName = source.readString();
        iconUrl = source.readString();
        source.readStringList(versions);
    }
}
