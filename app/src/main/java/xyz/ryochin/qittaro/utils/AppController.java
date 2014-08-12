/**
 * PACKAGE NAME xyz.ryochin.qittaro.utils
 * CREATED BY kosugeryou
 * CREATED AT 2014/07/26
 */
package xyz.ryochin.qittaro.utils;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Logger;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;

public class AppController extends Application {

    private static final String TAG = AppController.class.getSimpleName();
    private static final String GA_PROPERTY_ID = "UA-42849812-5";
    private static final boolean GA_IS_DRY_RUN = false;
    private static final Logger.LogLevel GA_LOG_LEVEL = Logger.LogLevel.VERBOSE;
    private static final String TRACKING_PREF_KEY = "trackingPreference";

    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private static AppController instance;

    private Tracker tracker;

    @Override
    public void onCreate() {
        super.onCreate();
        this.initializeGa();
        instance = this;
    }

    public static synchronized AppController getInstance() {
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (this.requestQueue == null) {
            this.requestQueue = Volley.newRequestQueue(this.getApplicationContext());
        }
        return this.requestQueue;
    }

    public ImageLoader getImageLoader() {
        if (this.imageLoader == null) {
            this.imageLoader = new ImageLoader(this.getRequestQueue(),
                    new LruBitmapCache());
        }
        return this.imageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (this.requestQueue != null) {
            this.requestQueue.cancelAll(tag);
        }
    }

    public void sendView(String screenName) {
        this.tracker.send(MapBuilder.createAppView().set(Fields.SCREEN_NAME, screenName).build());
    }

    private void initializeGa() {
        GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
        this.tracker = analytics.getTracker(GA_PROPERTY_ID);

        // GAServiceManager.getInstance().setLocalDispatchPeriod(GA_DISPATCH_PERIOD);
        analytics.setDryRun(GA_IS_DRY_RUN);
        analytics.getLogger().setLogLevel(GA_LOG_LEVEL);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (key.equals(TRACKING_PREF_KEY)) {
                    GoogleAnalytics.getInstance(getApplicationContext()).setAppOptOut(sharedPreferences.getBoolean(key, false));
                }
            }
        });

    }
}
