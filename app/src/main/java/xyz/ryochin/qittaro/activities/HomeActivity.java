/**
 * PACKAGE NAME xyz.ryochin.qittaro.activities
 * CREATED BY kosugeryou
 * CREATED AT 2014/07/23
 */

package xyz.ryochin.qittaro.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import xyz.ryochin.qittaro.R;
import xyz.ryochin.qittaro.adapters.LeftDrawerAdapter;
import xyz.ryochin.qittaro.utils.AppSharedPreference;


public class HomeActivity extends ActionBarActivity {

    private static final String TAG = HomeActivity.class.getSimpleName();
    private final HomeActivity self = this;

    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private ActionBarDrawerToggle drawerToggle;
    private BaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_home);
        this.drawerLayout = (DrawerLayout)this.findViewById(R.id.activity_home_drawer_layout);
        this.drawerList = (ListView)this.findViewById(R.id.activity_home_left_drawer);
        this.adapter = new LeftDrawerAdapter(this);
        this.drawerList.setAdapter(this.adapter);
        this.drawerList.setOnItemClickListener(this.clickListener);

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setHomeButtonEnabled(true);

        this.drawerToggle = new ActionBarDrawerToggle(
                this,
                this.drawerLayout,
                R.drawable.apptheme_ic_navigation_drawer,
                R.string.activity_home_drawer_open,
                R.string.activity_home_drawer_close
                ) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                self.getSupportActionBar().setTitle(R.string.app_name);
                supportInvalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                supportInvalidateOptionsMenu();
            }
        };
        this.drawerLayout.setDrawerListener(this.drawerToggle);
        this.navigateTo(0);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (this.drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        this.drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.drawerToggle.onConfigurationChanged(newConfig);
    }

    private AdapterView.OnItemClickListener clickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            self.navigateTo(position);
        }
    };

    private void navigateTo(int position) {
        if (AppSharedPreference.isLoggedIn(this)) {
            this.loggedInNavigateTo(position);
        } else {
            this.notLoggedInNavigateTo(position);
        }
        this.drawerLayout.closeDrawer(this.drawerList);
    }

    private void loggedInNavigateTo(int position) {
    }

    private void notLoggedInNavigateTo(int position) {
        switch (position) {
            case LeftDrawerAdapter.LEFT_DRAWER_ITEM_LOGIN_INDEX:
                this.getSupportActionBar().setTitle(R.string.left_drawer_login_title);
                break;
            case LeftDrawerAdapter.LEFT_DRAWER_ITEM_PUBLIC_INDEX:
                this.getSupportActionBar().setTitle(R.string.left_drawer_public_title);
                break;
            case LeftDrawerAdapter.LEFT_DRAWER_ITEM_SEARCH_INDEX:
                this.getSupportActionBar().setTitle(R.string.left_drawer_search_title);
                break;
            case LeftDrawerAdapter.LEFT_DRAWER_ITEM_TAG_INDEX:
                this.getSupportActionBar().setTitle(R.string.left_drawer_tag_title);
                break;
        }
    }
}
