/**
 * PACKAGE NAME xyz.ryochin.qittaro.utils
 * CREATED BY kosugeryou
 * CREATED AT 2014/07/28
 */

package xyz.ryochin.qittaro.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AppSharedPreference {

    private static final String TAG = AppSharedPreference.class.getSimpleName();
    private final AppSharedPreference self = this;
    private static final String TOKEN_KEY = "token";
    private static final String URL_NAME_KEY = "urlName";
    private static final String PROFILE_IMAGE_URL_KEY = "profileImageURL";

    public static String getToken(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(TOKEN_KEY, null);
    }

    public static String getURLName(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(URL_NAME_KEY, null);
    }

    public static String getProfileImageUrlKey(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(PROFILE_IMAGE_URL_KEY, null);
    }

    public static boolean isLoggedIn(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String token = sp.getString(TOKEN_KEY, null);
        return (token != null);
    }

    public static boolean setToken(Context context, String token) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.edit().putString(TOKEN_KEY, token).commit();
    }

    public static boolean setURLName(Context context, String urlName) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.edit().putString(URL_NAME_KEY, urlName).commit();
    }

    public static boolean setProfileImageURL(Context context, String profileImageURL) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.edit().putString(PROFILE_IMAGE_URL_KEY, profileImageURL).commit();
    }
}
