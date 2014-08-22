/**
 * PACKAGE NAME xyz.ryochin.qittaro.models
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/22
 */
package xyz.ryochin.qittaro.models;

import android.os.Parcel;
import android.os.Parcelable;

public class FollowingUserModel implements Parcelable {

    private static final String TAG = FollowingUserModel.class.getSimpleName();
    private final FollowingUserModel self = this;

    private long id;
    private String urlName;
    private String profileImageUrl;

    public long getId() {
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
}
