/**
 * PACKAGE NAME xyz.ryochin.qittaro.activities
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/11
 */
package xyz.ryochin.qittaro.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

import xyz.ryochin.qittaro.R;
import xyz.ryochin.qittaro.utils.AppSharedPreference;

public class TagActivity extends ActionBarActivity implements ActionBar.OnNavigationListener {

    private static final String TAG = TagActivity.class.getSimpleName();
    private final TagActivity self = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_tag);

        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        if (AppSharedPreference.isLoggedIn(this)) {
            // Logged in.
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
            String[] spinnerTitles = this.getResources().getStringArray(R.array.tag_spinner_titles);
            SpinnerAdapter spinnerAdapter = ArrayAdapter.createFromResource(this,
                    R.array.tag_spinner_titles,
                    android.R.layout.simple_dropdown_item_1line);
            actionBar.setListNavigationCallbacks(spinnerAdapter, this);
        } else {
            // No logged in.
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle(R.string.tag_no_logged_in_title);
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
    public boolean onNavigationItemSelected(int position, long id) {
        return false;
    }
}
