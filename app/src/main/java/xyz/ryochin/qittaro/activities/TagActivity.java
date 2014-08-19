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
import xyz.ryochin.qittaro.fragments.FragmentListener;
import xyz.ryochin.qittaro.fragments.TagFragment;
import xyz.ryochin.qittaro.models.ArticleModel;
import xyz.ryochin.qittaro.models.FollowUserModel;
import xyz.ryochin.qittaro.models.TagModel;
import xyz.ryochin.qittaro.utils.AppController;

public class TagActivity extends ActionBarActivity implements FragmentListener {

    private static final String TAG = TagActivity.class.getSimpleName();
    private final TagActivity self = this;

    public static final String INTENT_TAG_NAME_KEY = "tagName";
    public static final String INTENT_TAG_URL_NAME_KEY = "tagURLName";
    public static final String INTENT_TAG_ICON_URL_KEY = "tagIconURL";

    private ImageLoader.ImageContainer imageContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.basic_fragment_container_layout);

        Intent intent = this.getIntent();
        final String tagName = intent.getStringExtra(INTENT_TAG_NAME_KEY);
        final String tagURLName = intent.getStringExtra(INTENT_TAG_URL_NAME_KEY);
        final String tagIconURL = intent.getStringExtra(INTENT_TAG_ICON_URL_KEY);


        if (savedInstanceState == null) {

            final ActionBar actionBar = this.getSupportActionBar();
            View customActionBarView = this.getActionBarView(tagName, tagIconURL);

            if (customActionBarView == null) {
                this.finish();
            }

            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setCustomView(customActionBarView);
            actionBar.setDisplayShowCustomEnabled(true);

            TagFragment fragment = TagFragment.newInstance(tagURLName);
            this.getSupportFragmentManager().beginTransaction()
                    .add(R.id.basic_fragment_container, fragment)
                    .commit();
        }
        this.overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
    }

    @Override
    protected void onStart() {
        super.onStart();
        AppController.getInstance().sendView(TAG);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
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
    public void onItemSelected(ArticleModel model) {
        Intent intent = new Intent(this, ArticleActivity.class);
        intent.putExtra(ArticleActivity.INTENT_ARTICLE_UUID_KEY, model.getUuid());
        this.startActivity(intent);
    }

    @Override
    public void onItemSelected(FollowUserModel model) {
    }

    @Override
    public void onItemSelected(TagModel model) {
    }


    private View getActionBarView(String urlName, String iconURL) {
        if (urlName == null || iconURL == null) {
            return null;
        }
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.action_bar_icon_layout, null);
        TextView textView = (TextView)view.findViewById(R.id.action_bar_title);
        textView.setText(urlName);
        NetworkImageView imageView = (NetworkImageView)view.findViewById(R.id.action_bar_icon);
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        imageView.setImageUrl(iconURL, imageLoader);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                self.finish();
            }
        });
        return view;
    }


}
