/**
 * PACKAGE NAME com.ryochin.qittaro.activities
 * CREATED BY kosugeryou
 * CREATED AT 2014/07/27
 */
package com.ryochin.qittaro.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import com.ryochin.qittaro.R;
import com.ryochin.qittaro.fragments.AlertDialogFragment;
import com.ryochin.qittaro.fragments.ArticleDetailFragment;

public class ArticleDetailActivity extends ActionBarActivity implements ArticleDetailFragment.ArticleDetailFragmentListener {

    private static final String TAG = ArticleDetailActivity.class.getSimpleName();
    private final ArticleDetailActivity self = this;

    public static final String INTENT_ARTICLE_UUID_KEY = "uuid";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_article_detail);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            Intent intent = this.getIntent();
            String articleUUID = intent.getStringExtra(INTENT_ARTICLE_UUID_KEY);
            ArticleDetailFragment fragment = ArticleDetailFragment.newInstance(articleUUID);
            this.getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragment, null).commit();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        this.overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
        super.onBackPressed();
    }

    @Override
    public void onLoadError() {
        String title = "エラー";
        String message = "記事の読み込みに失敗しました。お時間を押してからもう一度お試しください。";
        AlertDialogFragment fragment = AlertDialogFragment.newInstance(title, message);
        fragment.show(this.getSupportFragmentManager(), null);
        this.finish();
    }

    @Override
    public void onStockedError() {
        String title = "エラー";
        String message = "ストックに失敗しました...。";
        AlertDialogFragment fragment = AlertDialogFragment.newInstance(title, message);
        fragment.show(this.getSupportFragmentManager(), null);
        this.finish();
    }

}
