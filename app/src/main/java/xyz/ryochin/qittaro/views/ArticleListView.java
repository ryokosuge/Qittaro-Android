/**
 * PACKAGE NAME xyz.ryochin.qittaro.views
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/14
 */

package xyz.ryochin.qittaro.views;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.List;

import xyz.ryochin.qittaro.R;
import xyz.ryochin.qittaro.adapters.ArticleAdapter;
import xyz.ryochin.qittaro.models.ArticleModel;

public class ArticleListView implements SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener, AbsListView.OnScrollListener {

    public interface Listener {
        public void onRefresh();
        public void onItemClicked(ArticleModel model);
        public void onScrollEnd();
    }

    private static final String TAG = ArticleListView.class.getSimpleName();
    private final ArticleListView self = this;

    private Context context;
    private android.widget.ListView listView;
    private ArticleAdapter adapter;
    private SwipeRefreshLayout swipeRefresh;
    private View fullLoadingView;
    private View footerLoadingView;
    private AdView adView;
    private Listener listener;


    public ArticleListView(Context context, View v, boolean showAdView, Listener listener) {
        try {
            this.context = context;
            this.listView = (android.widget.ListView)v.findViewById(R.id.basic_list_view);
            this.swipeRefresh = (SwipeRefreshLayout)v.findViewById(R.id.basic_list_swipe_refresh);
            this.fullLoadingView = v.findViewById(R.id.basic_list_loading_layout);
            this.swipeRefresh.setColorSchemeResources(
                    R.color.app_first_green_color,
                    R.color.app_second_green_color,
                    R.color.app_third_green_color,
                    R.color.app_fourth_green_color
            );
            this.swipeRefresh.setOnRefreshListener(this);
            this.listView.addFooterView(this.getFooterLoadingView());
            this.listView.setOnItemClickListener(this);
            this.listView.setOnScrollListener(this);
            if (showAdView) {
                this.setAdView(v);
            }
            this.listener = listener;
        } catch (NullPointerException e) {
            throw new NullPointerException("layout resource id use R.layout.fragment_article.");
        }
    }

    public void setItems(List<ArticleModel> items) {
        this.adapter = new ArticleAdapter(this.context);
        this.adapter.setItems(items);
        this.listView.setAdapter(this.adapter);
        this.adapter.notifyDataSetChanged();
    }

    public void addItems(List<ArticleModel> items) {
        this.adapter.addItems(items);
        this.adapter.notifyDataSetChanged();
    }

    public void setFooterLoadingViewVisibility(int visibility) {
        this.getFooterLoadingView().findViewById(R.id.progressBar).setVisibility(visibility);
    }

    public void setFullLoadingViewVisibility(int visibility) {
        this.fullLoadingView.setVisibility(visibility);
    }

    public void setSwipeRefreshVisibility(int visibility) {
        this.swipeRefresh.setVisibility(visibility);
    }

    public void setRefresh(boolean refreshing) {
        this.swipeRefresh.setRefreshing(refreshing);
    }

    public void pause() {
        if (this.adView != null) {
            this.adView.pause();
        }
    }

    public void resume() {
        if (this.adView != null) {
            this.adView.resume();
        }
    }

    public void destroy() {
        if (this.adView != null) {
            this.adView.destroy();
        }
    }

    @Override
    public void onRefresh() {
        this.listener.onRefresh();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ArticleModel model = (ArticleModel)this.adapter.getItem(position);
        this.listener.onItemClicked(model);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (totalItemCount != 0 && totalItemCount == firstVisibleItem + visibleItemCount) {
            this.listener.onScrollEnd();
        }
    }

    private void setAdView(View v) throws NullPointerException {
        this.adView = (AdView)v.findViewById(R.id.basic_list_admob_view);
        this.adView.setVisibility(View.VISIBLE);
        AdRequest adRequest = new AdRequest.Builder().build();
        this.adView.loadAd(adRequest);
    }

    private View getFooterLoadingView() {
        if (this.footerLoadingView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            this.footerLoadingView = inflater.inflate(R.layout.list_item_footer_loading, null);
        }
        return this.footerLoadingView;
    }

}
