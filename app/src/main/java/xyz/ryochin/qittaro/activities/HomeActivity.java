/**
 * PACKAGE NAME xyz.ryochin.qittaro.activities
 * CREATED BY kosugeryou
 * CREATED AT 2014/07/23
 */

package xyz.ryochin.qittaro.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import xyz.ryochin.qittaro.R;
import xyz.ryochin.qittaro.adapters.LeftDrawerAdapter;
import xyz.ryochin.qittaro.adapters.LoginLeftDrawerAdapter;
import xyz.ryochin.qittaro.fragments.AlertDialogFragment;
import xyz.ryochin.qittaro.fragments.ArticlesFragment;
import xyz.ryochin.qittaro.fragments.FollowingTagsFragment;
import xyz.ryochin.qittaro.fragments.FragmentListener;
import xyz.ryochin.qittaro.fragments.LoginFragment;
import xyz.ryochin.qittaro.fragments.MyArticleFragment;
import xyz.ryochin.qittaro.fragments.StocksFragment;
import xyz.ryochin.qittaro.fragments.TagsFragment;
import xyz.ryochin.qittaro.models.ArticleModel;
import xyz.ryochin.qittaro.utils.AppSharedPreference;


public class HomeActivity extends ActionBarActivity implements FragmentListener, LoginFragment.Listener {

    private static final String TAG = HomeActivity.class.getSimpleName();
    private final HomeActivity self = this;

    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_home);
        this.drawerLayout = (DrawerLayout)this.findViewById(R.id.activity_home_drawer_layout);
        this.drawerList = (ListView)this.findViewById(R.id.activity_home_left_drawer);
        this.drawerList.setOnItemClickListener(this.clickListener);
        this.setAdapter();

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

        if (savedInstanceState == null) {
            ArticlesFragment fragment = new ArticlesFragment();
            this.getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.activity_home_fragment_container, fragment)
                    .commit();
            this.getSupportActionBar().setTitle(R.string.left_drawer_public_title);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpened = this.drawerLayout.isDrawerOpen(this.drawerList);
        menu.findItem(R.id.home_menu_search).setVisible(!drawerOpened);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (this.drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.home_menu_search:
                Intent intent = new Intent(this, SearchActivity.class);
                this.startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

    @Override
    public void onItemSelected(ArticleModel model) {
        Intent intent = new Intent(this, ArticleDetailActivity.class);
        intent.putExtra(ArticleDetailActivity.INTENT_ARTICLE_UUID_KEY, model.getUuid());
        this.startActivity(intent);
    }

    @Override
    public void onCompletedLoggedin(boolean result) {
        if (result) {
            Toast.makeText(this, R.string.login_success_message, Toast.LENGTH_SHORT).show();
            ArticlesFragment fragment = new ArticlesFragment();
            this.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.activity_home_fragment_container, fragment)
                    .commit();
            this.getSupportActionBar().setTitle(R.string.left_drawer_public_title);
            this.setAdapter();
        } else {
            String title = this.getResources().getString(R.string.login_error_title);
            String message = this.getResources().getString(R.string.login_error_message);
            AlertDialogFragment alertDialogFragment = AlertDialogFragment.newInstance(title, message);
            alertDialogFragment.show(this.getSupportFragmentManager(), null);
        }
    }

    private void setAdapter() {
        if (AppSharedPreference.isLoggedIn(this)) {
            this.drawerList.setAdapter(new LoginLeftDrawerAdapter(this));
        } else {
            this.drawerList.setAdapter(new LeftDrawerAdapter(this));
        }
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
        switch (position) {
            case LoginLeftDrawerAdapter.LOG_IN_LEFT_DRAWER_ITEM_FOLLOWING_TAG_INDEX:
                FollowingTagsFragment followFragment = FollowingTagsFragment.newInstance();
                this.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.activity_home_fragment_container, followFragment)
                        .commit();
                this.getSupportActionBar().setTitle(R.string.left_drawer_following_tag_title);
                break;
            case LoginLeftDrawerAdapter.LOG_IN_LEFT_DRAWER_ITEM_ALL_TAG_INDEX:
                TagsFragment tagsFragment = TagsFragment.newInstance();
                this.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.activity_home_fragment_container, tagsFragment)
                        .commit();
                this.getSupportActionBar().setTitle(R.string.left_drawer_tag_title);
                break;
            case LoginLeftDrawerAdapter.LOG_IN_LEFT_DRAWER_ITEM_MINE_INDEX:
                MyArticleFragment myArticleFragment = MyArticleFragment.newInstance();
                this.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.activity_home_fragment_container, myArticleFragment)
                        .commit();
                this.getSupportActionBar().setTitle(R.string.left_drawer_mine_title);
                break;
            case LoginLeftDrawerAdapter.LOG_IN_LEFT_DRAWER_ITEM_PUBLIC_INDEX:
                ArticlesFragment fragment = new ArticlesFragment();
                this.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.activity_home_fragment_container, fragment)
                        .commit();
                this.getSupportActionBar().setTitle(R.string.left_drawer_public_title);
                break;
            case LoginLeftDrawerAdapter.LOG_IN_LEFT_DRAWER_ITEM_STOCKED_INDEX:
                StocksFragment stocksFragment = StocksFragment.newInstance();
                this.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.activity_home_fragment_container, stocksFragment)
                        .commit();
                this.getSupportActionBar().setTitle(R.string.left_drawer_stocked_title);
                break;
        }
    }

    private void notLoggedInNavigateTo(int position) {
        switch (position) {
            case LeftDrawerAdapter.LEFT_DRAWER_ITEM_LOGIN_INDEX:
                LoginFragment loginFragment = LoginFragment.newInstance();
                this.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.activity_home_fragment_container, loginFragment)
                        .commit();
                this.getSupportActionBar().setTitle(R.string.left_drawer_login_title);
                break;
            case LeftDrawerAdapter.LEFT_DRAWER_ITEM_PUBLIC_INDEX:
                ArticlesFragment fragment = new ArticlesFragment();
                this.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.activity_home_fragment_container, fragment)
                        .commit();
                this.getSupportActionBar().setTitle(R.string.left_drawer_public_title);
                break;
            case LeftDrawerAdapter.LEFT_DRAWER_ITEM_TAG_INDEX:
                TagsFragment tagsFragment = TagsFragment.newInstance();
                this.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.activity_home_fragment_container, tagsFragment)
                        .commit();
                this.getSupportActionBar().setTitle(R.string.left_drawer_tag_title);
                break;
        }
    }
}
