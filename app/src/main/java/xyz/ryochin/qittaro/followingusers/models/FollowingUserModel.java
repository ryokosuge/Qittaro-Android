
package xyz.ryochin.qittaro.followingusers.models;

public class FollowingUserModel {

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
}
