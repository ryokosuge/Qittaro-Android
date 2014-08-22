/**
 * PACKAGE NAME xyz.ryochin.qittaro.tags
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/22
 */
package xyz.ryochin.qittaro.tags;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import xyz.ryochin.qittaro.activities.TagActivity;
import xyz.ryochin.qittaro.fragments.AlertDialogFragment;
import xyz.ryochin.qittaro.fragments.FragmentListener;
import xyz.ryochin.qittaro.models.TagModel;
import xyz.ryochin.qittaro.requests.APIRequest;
import xyz.ryochin.qittaro.utils.AppController;

public class TagsFragment extends Fragment implements TagsView, AdapterView.OnItemClickListener, AbsListView.OnScrollListener {

    private static final String TAG = TagsFragment.class.getSimpleName();
    private final TagsFragment self = this;

    private static final String ARGS_REQUEST_KEY = "APIRequest";
    private static final String ARGS_SHOW_AD_KEY = "showAd";
    private SwipeRefreshLayout swipeRefreshLayout;
    private View fullLoadingView;
    private ListView listView;
    private TagsAdapter adapter;
    private TagsPresenter presenter;
    private AdView adView;
    private View footerView;
    private FragmentListener listener;

    public static TagsFragment newInstance(APIRequest request, boolean showAd) {
        Bundle args = new Bundle();
        args.putSerializable(ARGS_REQUEST_KEY, request);
        args.putBoolean(ARGS_SHOW_AD_KEY, showAd);
        TagsFragment fragment = new TagsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.listener = (FragmentListener)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Please implement FragmentListener.");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.basic_list_view_layout, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle args = getArguments();
        APIRequest request = (APIRequest)args.getSerializable(ARGS_REQUEST_KEY);
        boolean showAd = args.getBoolean(ARGS_SHOW_AD_KEY);
        this.presenter = new TagsPresenterImpl(this, request);
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
        this.adapter = new TagsAdapter(this.getActivity());
        if (showAd) {
            this.setAdView();
        }
        presenter.start();
    }

    @Override
    public void onStart() {
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        presenter.onItemClicked(position);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        presenter.onScroll(totalItemCount, firstVisibleItem, visibleItemCount);
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
    public void showReloadLoadingView() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideReloadLoadingView() {
        swipeRefreshLayout.setRefreshing(false);
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
    public void setItems(List<TagModel> items) {
        adapter.setItems(items);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void addItems(List<TagModel> items) {
        adapter.addItems(items);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showAPIErrorMessage() {
        String title = getString(R.string.api_error_title);
        String message = getString(R.string.api_error_message);
        AlertDialogFragment fragment = AlertDialogFragment.newInstance(title, message);
        fragment.show(getActivity().getSupportFragmentManager(), null);
    }

    @Override
    public void navigateToTagActivity(String urlName, String name, String iconUrl) {
        Intent intent = new Intent(this.getActivity(), TagActivity.class);
        intent.putExtra(TagActivity.INTENT_TAG_URL_NAME_KEY, urlName);
        intent.putExtra(TagActivity.INTENT_TAG_NAME_KEY, name);
        intent.putExtra(TagActivity.INTENT_TAG_ICON_URL_KEY, iconUrl);
        listener.navigateTo(intent);
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
