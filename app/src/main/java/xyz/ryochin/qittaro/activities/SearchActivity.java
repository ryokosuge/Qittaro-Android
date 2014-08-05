/**
 * PACKAGE NAME xyz.ryochin.qittaro.activities
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/05
 */

package xyz.ryochin.qittaro.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import xyz.ryochin.qittaro.R;
import xyz.ryochin.qittaro.fragments.AlertDialogFragment;
import xyz.ryochin.qittaro.fragments.SearchFragment;
import xyz.ryochin.qittaro.models.ArticleModel;

public class SearchActivity extends ActionBarActivity implements SearchFragment.Listener {

    private static final String TAG = SearchActivity.class.getSimpleName();
    private final SearchActivity self = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        this.getSupportActionBar().setDisplayShowTitleEnabled(false);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (savedInstanceState == null) {
            FragmentManager fragmentManager = this.getSupportFragmentManager();
            SearchFragment fragment = new SearchFragment();
            fragmentManager.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
        this.overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
    }

    @Override
    public void onItemClicked(ArticleModel model) {
        Intent intent = new Intent(this, ArticleDetailActivity.class);
        intent.putExtra(ArticleDetailActivity.INTENT_ARTICLE_UUID_KEY, model.getUuid());
        this.startActivity(intent);
    }

    @Override
    public void onOptionMenuClicked(MenuItem menu) {
        switch(menu.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void noSerchArticle(String searchWord) {
        String title = this.getResources().getString(R.string.search_empty_title);
        StringBuilder builder = new StringBuilder();
        String message = builder.append(this.getResources().getString(R.string.search_empty_message))
                .append("\n")
                .append(this.getResources().getString(R.string.search_empty_search_word_top))
                .append(" [")
                .append(searchWord)
                .append("]").toString();
        AlertDialogFragment fragment = AlertDialogFragment.newInstance(title, message);
        fragment.show(this.getSupportFragmentManager(), null);
    }
}
