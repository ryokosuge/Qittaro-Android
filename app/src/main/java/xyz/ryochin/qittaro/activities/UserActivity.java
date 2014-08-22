/**
 * PACKAGE NAME xyz.ryochin.qittaro.activities
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/13
 */

package xyz.ryochin.qittaro.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import xyz.ryochin.qittaro.R;
import xyz.ryochin.qittaro.fragments.FragmentListener;
import xyz.ryochin.qittaro.user.UserFragment;

public class UserActivity extends ActionBarActivity implements FragmentListener {

    private static final String TAG = UserActivity.class.getSimpleName();
    private final UserActivity self = this;

    public static final String INTENT_USER_URL_NAME_KEY = "urlName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.basic_fragment_container_layout);
        Intent intent = this.getIntent();
        final String urlName = intent.getExtras().getString(INTENT_USER_URL_NAME_KEY);

        if (savedInstanceState == null) {
            Fragment fragment = UserFragment.newInstance(urlName, true);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.basic_fragment_container, fragment)
                    .commit();
        }

        this.overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
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

    @Override
    protected void onPause() {
        super.onPause();
        this.overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
    }

    @Override
    public void navigateTo(Intent intent) {
        startActivity(intent);
    }
}
