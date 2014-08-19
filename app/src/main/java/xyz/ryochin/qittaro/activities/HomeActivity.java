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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import xyz.ryochin.qittaro.R;
import xyz.ryochin.qittaro.adapters.LeftDrawerAdapter;
import xyz.ryochin.qittaro.adapters.LoginLeftDrawerAdapter;
import xyz.ryochin.qittaro.articles.ArticlesFragment;
import xyz.ryochin.qittaro.followingusers.FollowingUsersFragment;
import xyz.ryochin.qittaro.fragments.AlertDialogFragment;
import xyz.ryochin.qittaro.fragments.FragmentListener;
import xyz.ryochin.qittaro.fragments.LoginFragment;
import xyz.ryochin.qittaro.requests.APIRequest;
import xyz.ryochin.qittaro.requests.AllTagRequest;
import xyz.ryochin.qittaro.requests.FollowTagsRequest;
import xyz.ryochin.qittaro.requests.FollowingUsersRequest;
import xyz.ryochin.qittaro.requests.MineRequest;
import xyz.ryochin.qittaro.requests.PublicRequest;
import xyz.ryochin.qittaro.requests.StockRequest;
import xyz.ryochin.qittaro.tags.TagsFragment;
import xyz.ryochin.qittaro.utils.AppController;
import xyz.ryochin.qittaro.utils.AppSharedPreference;


public class HomeActivity extends ActionBarActivity implements FragmentListener, LoginFragment.Listener {

    private static final String TAG = HomeActivity.class.getSimpleName();
    private final HomeActivity self = this;
    private static final String BUNDLE_NAVIGATION_DRAWER_INDEX_KEY = "index";
    private static final int DRAWER_LIST_HEADER_INDEX = -1;

    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private ActionBarDrawerToggle drawerToggle;
    private View headerUserInfo;
    private int drawerListIndex;

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
                R.string.drawer_open,
                R.string.drawer_close
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
            APIRequest request = new PublicRequest();
            ArticlesFragment fragment = ArticlesFragment.newInstance(request, true);
            this.getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.activity_home_fragment_container, fragment)
                    .commit();
            this.getSupportActionBar().setTitle(R.string.left_drawer_public_title);
            this.drawerListIndex = 1;
        } else {
            if (savedInstanceState.containsKey(BUNDLE_NAVIGATION_DRAWER_INDEX_KEY)) {
                this.drawerListIndex = savedInstanceState.getInt(BUNDLE_NAVIGATION_DRAWER_INDEX_KEY);
            } else {
                this.drawerListIndex = 1;
            }
        }
        this.navigateTo(this.drawerListIndex);
        this.overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(BUNDLE_NAVIGATION_DRAWER_INDEX_KEY, this.drawerListIndex);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.containsKey(BUNDLE_NAVIGATION_DRAWER_INDEX_KEY)) {
            this.drawerListIndex = savedInstanceState.getInt(BUNDLE_NAVIGATION_DRAWER_INDEX_KEY);
        } else {
            this.drawerListIndex = 1;
        }
        this.navigateTo(this.drawerListIndex);
    }

    @Override
    public void navigateTo(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void onCompletedLoggedin(boolean result) {
        if (result) {
            Toast.makeText(this, R.string.login_success_message, Toast.LENGTH_SHORT).show();
            APIRequest request = new PublicRequest();
            ArticlesFragment fragment = ArticlesFragment.newInstance(request, true);
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
            this.drawerList.addHeaderView(this.getHeaderUserInfo());
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
            AppSharedPreference.logout(this);
            this.notLoggedInNavigateTo(position);
        }
        this.drawerListIndex = position;
        this.drawerLayout.closeDrawer(this.drawerList);
    }

    private void loggedInNavigateTo(int position) {
        switch (position - 1) {
            case DRAWER_LIST_HEADER_INDEX:
                String urlName = AppSharedPreference.getURLName(this);
                Intent intent = new Intent(this, UserActivity.class);
                intent.putExtra(UserActivity.INTENT_USER_URL_NAME_KEY, urlName);
                this.startActivity(intent);
                break;
            case LoginLeftDrawerAdapter.LOG_IN_LEFT_DRAWER_ITEM_FOLLOWING_TAG_INDEX:
                String tagUrlName = AppSharedPreference.getURLName(this);
                APIRequest followTagsRequest = new FollowTagsRequest(tagUrlName);
                TagsFragment followTagsFragment = TagsFragment.newInstance(followTagsRequest, true);
                this.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.activity_home_fragment_container, followTagsFragment)
                        .commit();
                this.getSupportActionBar().setTitle(R.string.left_drawer_following_tag_title);
                break;
            case LoginLeftDrawerAdapter.LOG_IN_LEFT_DRAWER_ITEM_ALL_TAG_INDEX:
                APIRequest allTagRequest = new AllTagRequest();
                TagsFragment tagsFragment = TagsFragment.newInstance(allTagRequest, true);
                this.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.activity_home_fragment_container, tagsFragment)
                        .commit();
                this.getSupportActionBar().setTitle(R.string.left_drawer_tag_title);
                break;
            case LoginLeftDrawerAdapter.LOG_IN_LEFT_DRAWER_ITEM_MINE_INDEX:
                String token = AppSharedPreference.getToken(this);
                APIRequest mineRequest = new MineRequest(token);
                ArticlesFragment mineFragment = ArticlesFragment.newInstance(mineRequest, true);
                this.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.activity_home_fragment_container, mineFragment)
                        .commit();
                this.getSupportActionBar().setTitle(R.string.left_drawer_mine_title);
                break;
            case LoginLeftDrawerAdapter.LOG_IN_LEFT_DRAWER_ITEM_PUBLIC_INDEX:
                APIRequest publicRequest = new PublicRequest();
                ArticlesFragment fragment = ArticlesFragment.newInstance(publicRequest, true);
                this.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.activity_home_fragment_container, fragment)
                        .commit();
                this.getSupportActionBar().setTitle(R.string.left_drawer_public_title);
                break;
            case LoginLeftDrawerAdapter.LOG_IN_LEFT_DRAWER_ITEM_STOCKED_INDEX:
                String stockToken = AppSharedPreference.getToken(this);
                APIRequest stockRequest = new StockRequest(stockToken);
                ArticlesFragment stocksFragment = ArticlesFragment.newInstance(stockRequest, true);
                this.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.activity_home_fragment_container, stocksFragment)
                        .commit();
                this.getSupportActionBar().setTitle(R.string.left_drawer_stocked_title);
                break;
            case LoginLeftDrawerAdapter.LOG_IN_LEFT_DRAWER_ITEM_FOLLOWING_USER_INDEX:
                String uname = AppSharedPreference.getURLName(this);
                APIRequest followingUsersRequest = new FollowingUsersRequest(uname);
                FollowingUsersFragment followingUsersFragment = FollowingUsersFragment.newInstance(followingUsersRequest, true);
                this.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.activity_home_fragment_container, followingUsersFragment)
                        .commit();
                this.getSupportActionBar().setTitle(R.string.left_drawer_following_user_title);
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
                APIRequest request = new PublicRequest();
                ArticlesFragment fragment = ArticlesFragment.newInstance(request, true);
                this.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.activity_home_fragment_container, fragment)
                        .commit();
                this.getSupportActionBar().setTitle(R.string.left_drawer_public_title);
                break;
            case LeftDrawerAdapter.LEFT_DRAWER_ITEM_TAG_INDEX:
                APIRequest tagsRequest = new AllTagRequest();
                TagsFragment tagsFragment = TagsFragment.newInstance(tagsRequest, true);
                this.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.activity_home_fragment_container, tagsFragment)
                        .commit();
                this.getSupportActionBar().setTitle(R.string.left_drawer_tag_title);
                break;
        }
    }

    private View getHeaderUserInfo() {
        if (this.headerUserInfo == null) {
            this.headerUserInfo = LayoutInflater.from(this).inflate(R.layout.drawer_list_header_user_info, null);
            String urlName = AppSharedPreference.getURLName(this);
            String profileImageURL = AppSharedPreference.getProfileImageUrlKey(this);
            ((TextView)this.headerUserInfo.findViewById(R.id.user_info_user_name)).setText(urlName);
            NetworkImageView imageView = (NetworkImageView)this.headerUserInfo.findViewById(R.id.user_info_icon);
            ImageLoader imageLoader = AppController.getInstance().getImageLoader();
            imageView.setImageUrl(profileImageURL, imageLoader);
        }
        return this.headerUserInfo;
    }
}
