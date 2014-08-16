/**
 * PACKAGE NAME xyz.ryochin.qittaro.activities
 * CREATED BY kosugeryou
 * CREATED AT 2014/07/27
 */
package xyz.ryochin.qittaro.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import xyz.ryochin.qittaro.R;
import xyz.ryochin.qittaro.fragments.ArticleFragment;
import xyz.ryochin.qittaro.models.ArticleDetailModel;

public class ArticleActivity extends ActionBarActivity implements ArticleFragment.Listener{

    private static final String TAG = ArticleActivity.class.getSimpleName();
    private final ArticleActivity self = this;

    public static final String INTENT_ARTICLE_UUID_KEY = "uuid";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.basic_fragment_container_layout);

        this.getSupportActionBar().setHomeButtonEnabled(true);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {

            Intent intent = this.getIntent();
            String articleUUID = intent.getStringExtra(INTENT_ARTICLE_UUID_KEY);
            FragmentManager fragmentManager = this.getSupportFragmentManager();
            ArticleFragment fragment = ArticleFragment.newInstance(articleUUID);
            fragmentManager.beginTransaction()
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
    public void setLoadTitle() {
        this.getSupportActionBar().setTitle(R.string.article_detail_loading_title);
    }

    @Override
    public void setArticleTitle(ArticleDetailModel model) {
        this.getSupportActionBar().setTitle(model.getTitle());
    }

    @Override
    public void onLoadError() {
    }

    @Override
    public void onStockedError() {
    }

    @Override
    public void onPressUser(String urlName, String iconURL) {
        Intent intent = new Intent(this, UserActivity.class);
        intent.putExtra(UserActivity.INTENT_USER_URL_NAME_KEY, urlName);
        intent.putExtra(UserActivity.INTENT_USER_PROFILE_IMAGE_URL_KEY, iconURL);
        this.startActivity(intent);
    }

    @Override
    public void onPressTag(String tagName, String urlName, String iconURL) {
        Intent intent = new Intent(this, TagActivity.class);
        intent.putExtra(TagActivity.INTENT_TAG_URL_NAME_KEY, urlName);
        intent.putExtra(TagActivity.INTENT_TAG_NAME_KEY, tagName);
        intent.putExtra(TagActivity.INTENT_TAG_ICON_URL_KEY, iconURL);
        this.startActivity(intent);
    }

}
