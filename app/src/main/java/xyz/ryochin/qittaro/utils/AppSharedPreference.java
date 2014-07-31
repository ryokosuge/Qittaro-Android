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

    public static String getToken(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String token = sp.getString(TOKEN_KEY, null);
        return token;
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
}
