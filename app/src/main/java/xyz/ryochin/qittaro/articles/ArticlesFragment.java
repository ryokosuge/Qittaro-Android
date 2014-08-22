/**
 * PACKAGE NAME xyz.ryochin.qittaro.articles
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/20
 */
package xyz.ryochin.qittaro.articles;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.List;

import xyz.ryochin.qittaro.R;
import xyz.ryochin.qittaro.activities.ArticleActivity;
import xyz.ryochin.qittaro.fragments.AlertDialogFragment;
import xyz.ryochin.qittaro.models.ArticleModel;
import xyz.ryochin.qittaro.fragments.FragmentListener;
import xyz.ryochin.qittaro.requests.APIRequest;
import xyz.ryochin.qittaro.utils.AppController;

public class ArticlesFragment extends Fragment implements ArticlesView, AbsListView.OnScrollListener, AdapterView.OnItemClickListener {
    private static final String TAG = ArticlesFragment.class.getSimpleName();
    private final ArticlesFragment self = this;

    private static final String ARGS_REQUEST_KEY = "APIRequest";
    private static final String ARGS_SHOW_AD_KEY = "showAd";

    private SwipeRefreshLayout swipeRefreshLayout;
    private View fullLoadingView;
    private ListView listView;
    private ArticlesAdapter adapter;
    private ArticlesPresenter presenter;
    private View footerView;
    private AdView adView;
    private FragmentListener listener;

    public static ArticlesFragment newInstance(APIRequest request, boolean showAd) {
        Log.e(TAG, "newInstance");
        Bundle args = new Bundle();
        args.putSerializable(ARGS_REQUEST_KEY, request);
        args.putBoolean(ARGS_SHOW_AD_KEY, showAd);
        ArticlesFragment fragment = new ArticlesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        Log.e(TAG, "onAttach");
        super.onAttach(activity);
        try {
            this.listener = (FragmentListener)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Please implement FragmentListener.");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView");
        return inflater.inflate(R.layout.basic_list_view_layout, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.e(TAG, "onActivityCreated()");
        super.onActivityCreated(savedInstanceState);
        Bundle args = getArguments();
        APIRequest request = (APIRequest)args.getSerializable(ARGS_REQUEST_KEY);
        boolean showAd = args.getBoolean(ARGS_SHOW_AD_KEY);
        this.presenter = new ArticlesPresenterImpl(this, request);
        this.listView = (ListView)getView().findViewById(R.id.basic_list_view);
        this.listView.addFooterView(this.getFooterView());
        this.listView.setOnItemClickListener(this);
        this.listView.setOnScrollListener(this);
        this.fullLoadingView = getView().findViewById(R.id.basic_list_loading_layout);
        this.swipeRefreshLayout = (SwipeRefreshLayout)getView().findViewById(R.id.basic_list_swipe_refresh);
        this.swipeRefreshLayout.setColorSchemeResources(
                R.color.app_first_green_color,
                R.color.app_second_green_color,
                R.color.app_third_green_color,
                R.color.app_fourth_green_color
        );
        this.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                self.presenter.refresh();
            }
        });
        this.adapter = new ArticlesAdapter(this.getActivity());
        if (showAd) {
            this.setAdView();
        }
        this.presenter.start();
    }

    @Override
    public void onStart() {
        Log.e(TAG, "onStart");
        super.onStart();
        AppController.getInstance().sendView(TAG + " : " + presenter.getRequestTag());
    }

    @Override
    public void onPause() {
        super.onPause();
        if (adView != null) {
            adView.pause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (adView != null) {
            adView.destroy();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.destroyView();
    }

    @Override
    public void showFullLoadingView() {
        swipeRefreshLayout.setVisibility(View.GONE);
        fullLoadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideFullLoadingView() {
        swipeRefreshLayout.setVisibility(View.VISIBLE);
        fullLoadingView.setVisibility(View.GONE);
    }

    @Override
    public void showListFooterLoadingView() {
        getFooterView().findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
    }

    @Override
    public void hideListFooterLoadingView() {
        getFooterView().findViewById(R.id.progressBar).setVisibility(View.GONE);
    }

    @Override
    public void setItems(List<ArticleModel> items) {
        adapter.setItems(items);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void addItems(List<ArticleModel> items) {
        adapter.addItems(items);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showReloadLoadingView() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideReloadLoadingView() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showAPIErrorMessage() {
        String title = getString(R.string.api_error_title);
        String message = getString(R.string.api_error_message);
        AlertDialogFragment fragment = AlertDialogFragment.newInstance(title, message);
        fragment.show(getActivity().getSupportFragmentManager(), null);
    }

    @Override
    public void navigateToArticleActivity(String uuid) {
        Intent intent = new Intent(this.getActivity(), ArticleActivity.class);
        intent.putExtra(ArticleActivity.INTENT_ARTICLE_UUID_KEY, uuid);
        listener.navigateTo(intent);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        presenter.onScroll(totalItemCount, firstVisibleItem, visibleItemCount);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        presenter.onItemClicked(position);
    }

    private void setAdView() {
        adView = (AdView) getView().findViewById(R.id.basic_list_admob_view);
        adView.setVisibility(View.VISIBLE);
        AdRequest adRequest = AppController.getInstance().getAdRequest();
        adView.loadAd(adRequest);
    }

    private View getFooterView() {
        if (this.footerView == null) {
            LayoutInflater inflater = LayoutInflater.from(this.getActivity());
            this.footerView = inflater.inflate(R.layout.list_item_footer_loading, null);
        }
        return this.footerView;
    }
}
