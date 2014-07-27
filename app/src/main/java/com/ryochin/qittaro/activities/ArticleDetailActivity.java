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
import com.ryochin.qittaro.fragments.ArticleDetailFragment;

public class ArticleDetailActivity extends ActionBarActivity {

    private static final String TAG = ArticleDetailActivity.class.getSimpleName();
    private final ArticleDetailActivity self = this;

    public static final String INTENT_ARTICLE_URL_KEY = "article_url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_article_detail);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            Intent intent = this.getIntent();
            String articleURL = intent.getStringExtra(INTENT_ARTICLE_URL_KEY);
            ArticleDetailFragment fragment = ArticleDetailFragment.newInstance(articleURL);
            this.getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragment, null).commit();
        }

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
