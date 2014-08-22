/**
 * PACKAGE NAME xyz.ryochin.qittaro.models
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/12
 */

package xyz.ryochin.qittaro.models;

import android.os.Parcel;
import android.os.Parcelable;

public class UserModel implements Parcelable {

    private static final String TAG = UserModel.class.getSimpleName();
    private final UserModel self = this;

    private Long id;
    private String urlName;
    private String profileImageUrl;
    private String name;
    private String url;
    private String description;
    private String websiteUrl;
    private String organization;
    private String location;
    private String facebook;
    private String linkedin;
    private String twitter;
    private String github;
    private int followers;
    private int followingUsers;
    private int items;

    public Long getId() {
        return id;
    }

    public String getUrlName() {
        return urlName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getDescription() {
        return description;
    }

    public String getWebSiteURL() {
        return websiteUrl;
    }

    public String getOrganization() {
        return organization;
    }

    public String getLocation() {
        return location;
    }

    public String getFacebook() {
        return facebook;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public String getTwitter() {
        return twitter;
    }

    public String getGithub() {
        return github;
    }

    public int getFollowers() {
        return followers;
    }

    public int getFollowingUsers() {
        return followingUsers;
    }

    public int getItems() {
        return items;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.urlName);
        dest.writeString(this.profileImageUrl);
        dest.writeString(this.name);
        dest.writeString(this.url);
        dest.writeString(this.description);
        dest.writeString(this.websiteUrl);
        dest.writeString(this.organization);
        dest.writeString(this.location);
        dest.writeString(this.facebook);
        dest.writeString(this.linkedin);
        dest.writeString(this.twitter);
        dest.writeString(this.github);
        dest.writeInt(this.followers);
        dest.writeInt(this.followingUsers);
        dest.writeInt(this.items);
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {

        @Override
        public UserModel createFromParcel(Parcel source) {
            return new UserModel(source);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };

    private UserModel(Parcel source) {
        this.id = source.readLong();
        this.urlName = source.readString();
        this.profileImageUrl = source.readString();
        this.name = source.readString();
        this.url = source.readString();
        this.description = source.readString();
        this.websiteUrl = source.readString();
        this.organization = source.readString();
        this.location = source.readString();
        this.facebook = source.readString();
        this.linkedin = source.readString();
        this.twitter = source.readString();
        this.github = source.readString();
        this.followers = source.readInt();
        this.followingUsers = source.readInt();
        this.items = source.readInt();
    }

}
