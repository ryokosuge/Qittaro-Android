/**
 * PACKAGE NAME xyz.ryochin.qittaro.activities
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/13
 */

package xyz.ryochin.qittaro.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import xyz.ryochin.qittaro.R;
import xyz.ryochin.qittaro.utils.AppController;

public class UserActivity extends ActionBarActivity {

    private static final String TAG = UserActivity.class.getSimpleName();
    private final UserActivity self = this;

    public static final String INTENT_USER_PROFILE_IMAGE_URL_KEY = "profileImageURL";
    public static final String INTENT_USER_URL_NAME_KEY = "urlName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = this.getSupportActionBar();

        Intent intent = this.getIntent();
        String urlName = intent.getExtras().getString(INTENT_USER_URL_NAME_KEY);
        String profileImageURL = intent.getExtras().getString(INTENT_USER_PROFILE_IMAGE_URL_KEY);

        if (urlName == null || profileImageURL == null) {
            this.finish();
            return ;
        }

        View customActionBarView = this.getActionBarView(urlName, profileImageURL);

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setCustomView(customActionBarView);
        actionBar.setDisplayShowCustomEnabled(true);


        this.overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
    }

    private View getActionBarView(String urlName, String profileImageURL) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.activity_user_action_bar_layout, null);
        TextView textView = (TextView)view.findViewById(R.id.activity_user_action_bar_title);
        textView.setText(urlName);
        NetworkImageView imageView = (NetworkImageView)view.findViewById(R.id.activity_user_action_bar_icon);
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        imageView.setImageUrl(profileImageURL, imageLoader);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                self.finish();
            }
        });
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
