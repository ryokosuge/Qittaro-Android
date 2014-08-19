/**
 * =====================================
 * ENCODE : UTF-8
 * CREATED AT 2014/08/20
 * CREATED BY kosugeryou
 * =====================================
 */
package xyz.ryochin.qittaro.login;

public class UserModel {

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

    public String getWebsiteUrl() {
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
}
