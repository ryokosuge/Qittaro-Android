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
import android.widget.ListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.List;

import xyz.ryochin.qittaro.R;
import xyz.ryochin.qittaro.adapters.TagsAdapter;
import xyz.ryochin.qittaro.models.TagModel;

public class TagListView implements SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener, AbsListView.OnScrollListener {

    public interface Listener {
        public void onRefresh();
        public void onItemClicked(TagModel model);
        public void onScrollEnd();
    }

    private static final String TAG = TagListView.class.getSimpleName();
    private final TagListView self = this;

    private Context context;
    private ListView tagListView;
    private SwipeRefreshLayout swipeRefresh;
    private TagsAdapter adapter;
    private AdView adView;
    private View footerLoadingView;
    private View fullLoadingView;
    private Listener listener;

    public TagListView(Context context, View v, boolean showAdView, Listener listener) {
        try {
            this.context = context;
            this.tagListView = (ListView)v.findViewById(R.id.basic_list_view);
            this.swipeRefresh = (SwipeRefreshLayout)v.findViewById(R.id.basic_list_swipe_refresh);
            this.adapter = new TagsAdapter(this.context);
            this.fullLoadingView = v.findViewById(R.id.basic_list_loading_layout);
            this.swipeRefresh.setColorSchemeResources(
                    R.color.app_first_green_color,
                    R.color.app_second_green_color,
                    R.color.app_third_green_color,
                    R.color.app_fourth_green_color
            );
            this.swipeRefresh.setOnRefreshListener(this);
            this.tagListView.addFooterView(this.getFooterLoadingView());
            this.tagListView.setOnItemClickListener(this);
            this.tagListView.setOnScrollListener(this);
            if (showAdView) {
                this.setAdView(v);
            }
            this.listener = listener;
        } catch (NullPointerException e) {
            throw new NullPointerException("layout resource id use R.layout.basic_list_view_layout");
        }
    }

    public void setItems(List<TagModel> items) {
        this.adapter.setItems(items);
        this.tagListView.setAdapter(this.adapter);
        this.adapter.notifyDataSetChanged();
    }

    public void addItems(List<TagModel> items) {
        this.adapter.addItems(items);
        this.adapter.notifyDataSetChanged();
    }

    private void setAdView(View v) throws NullPointerException {
        this.adView = (AdView)v.findViewById(R.id.basic_list_admob_view);
        this.adView.setVisibility(View.VISIBLE);
        AdRequest adRequest = new AdRequest.Builder().build();
        this.adView.loadAd(adRequest);
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
        TagModel tagModel = (TagModel)this.adapter.getItem(position);
        this.listener.onItemClicked(tagModel);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (totalItemCount != 0 && totalItemCount == visibleItemCount + firstVisibleItem) {
            this.listener.onScrollEnd();
        }
    }

    private View getFooterLoadingView() {
        if (this.footerLoadingView == null) {
            LayoutInflater inflater = LayoutInflater.from(this.context);
            this.footerLoadingView = inflater.inflate(R.layout.list_item_footer_loading, null);
        }
        return this.footerLoadingView;
    }

}
