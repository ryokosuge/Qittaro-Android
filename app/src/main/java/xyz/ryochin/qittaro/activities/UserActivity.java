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
import xyz.ryochin.qittaro.adapters.UserDetailPagerAdapter;
import xyz.ryochin.qittaro.apimanagers.UserInfoAPIManager;
import xyz.ryochin.qittaro.fragments.FragmentListener;
import xyz.ryochin.qittaro.models.ArticleModel;
import xyz.ryochin.qittaro.models.FollowUserModel;
import xyz.ryochin.qittaro.models.TagModel;
import xyz.ryochin.qittaro.models.UserModel;
import xyz.ryochin.qittaro.utils.AppController;
import xyz.ryochin.qittaro.views.UserDetailView;

public class UserActivity extends ActionBarActivity implements FragmentListener, UserDetailView.Listener {

    private static final String TAG = UserActivity.class.getSimpleName();
    private final UserActivity self = this;

    public static final String INTENT_USER_URL_NAME_KEY = "urlName";
    private static final String SAVE_INSTANCE_CURRENT_INDEX_KEY = "currentIndex";

    private UserDetailView view;
    private int currentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.basic_view_pager_layout);
        Intent intent = this.getIntent();
        final String urlName = intent.getExtras().getString(INTENT_USER_URL_NAME_KEY);
        View currentView = this.findViewById(android.R.id.content);
        this.view = new UserDetailView(currentView, true, this);
        this.view.showFullLoadingView();
        UserInfoAPIManager.getInstance().getItem(urlName, new UserInfoAPIManager.Listener() {

            @Override
            public void willStart() {
                ActionBar actionBar = self.getSupportActionBar();
                actionBar.setDisplayShowHomeEnabled(true);
                actionBar.setDisplayShowTitleEnabled(true);
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setTitle(R.string.article_detail_loading_title);
            }

            @Override
            public void onCompleted(UserModel model) {
                self.setTitle(model);
                UserDetailPagerAdapter adapter = new UserDetailPagerAdapter(
                        self.getSupportFragmentManager(), self, urlName, model);
                self.view.setAdapter(adapter);
                self.view.hideFullLoadingView();
            }

            @Override
            public void onError() {
                self.view.hideFullLoadingView();
            }
        });

        this.overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
    }

    private void setTitle(UserModel model) {
        View customActionBarView = this.getActionBarView(model.getUrlName(), model.getProfileImageURL());

        if (customActionBarView == null) {
            this.finish();
        }

        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setCustomView(customActionBarView);
        actionBar.setDisplayShowCustomEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        AppController.getInstance().sendView(TAG);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.view.pause();
        this.overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.view.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.view.destroy();
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVE_INSTANCE_CURRENT_INDEX_KEY, this.currentIndex);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.containsKey(SAVE_INSTANCE_CURRENT_INDEX_KEY)) {
            this.currentIndex = savedInstanceState.getInt(SAVE_INSTANCE_CURRENT_INDEX_KEY);
        } else {
            this.currentIndex = 0;
        }
        this.view.setPageIndex(this.currentIndex);
    }

    @Override
    public void onItemSelected(ArticleModel model) {
        Intent intent = new Intent(this, ArticleActivity.class);
        intent.putExtra(ArticleActivity.INTENT_ARTICLE_UUID_KEY, model.getUuid());
        this.startActivity(intent);
    }

    @Override
    public void onItemSelected(FollowUserModel model) {
    }

    @Override
    public void onChangedPageIndex(int pageIndex) {
        this.currentIndex = pageIndex;
    }

    @Override
    public void onItemSelected(TagModel model) {
        Intent intent = new Intent(this, TagActivity.class);
        intent.putExtra(TagActivity.INTENT_TAG_URL_NAME_KEY, model.getUrlName());
        intent.putExtra(TagActivity.INTENT_TAG_NAME_KEY, model.getName());
        intent.putExtra(TagActivity.INTENT_TAG_ICON_URL_KEY, model.getIconURL());
        this.startActivity(intent);
    }

    private View getActionBarView(String urlName, String profileImageURL) {
        if (urlName == null || profileImageURL == null) {
            return null;
        }
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.action_bar_icon_layout, null);
        TextView textView = (TextView)view.findViewById(R.id.action_bar_title);
        textView.setText(urlName);
        NetworkImageView imageView = (NetworkImageView)view.findViewById(R.id.action_bar_icon);
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
}
