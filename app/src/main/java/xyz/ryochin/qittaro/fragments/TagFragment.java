/**
 * PACKAGE NAME xyz.ryochin.qittaro.fragments
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/11
 */

package xyz.ryochin.qittaro.fragments;

import android.app.Activity;
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

import java.util.List;

import xyz.ryochin.qittaro.R;
import xyz.ryochin.qittaro.adapters.ArticleAdapter;
import xyz.ryochin.qittaro.apimanagers.APIManagerListener;
import xyz.ryochin.qittaro.apimanagers.TagArticlesAPIManager;
import xyz.ryochin.qittaro.models.ArticleModel;
import xyz.ryochin.qittaro.utils.AppController;

public class TagFragment extends Fragment implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = TagFragment.class.getSimpleName();
    private final TagFragment self = this;
    private static final String ARGS_URL_NAME_KEY = "urlname";
    private FragmentListener listener;
    private View footerLoadingView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArticleAdapter adapter;
    private String tagURLName;

    public static TagFragment newInstance(String urlName) {
        Bundle args = new Bundle();
        args.putString(ARGS_URL_NAME_KEY, urlName);
        TagFragment fragment = new TagFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.listener = (FragmentListener)activity;
        } catch (ClassCastException e) {
            throw  new ClassCastException("Please implement the FragmentListener.");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tag, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle args = this.getArguments();
        if (args.containsKey(ARGS_URL_NAME_KEY)) {
            this.tagURLName = args.getString(ARGS_URL_NAME_KEY);
        }
        this.swipeRefreshLayout = (SwipeRefreshLayout)this.getView().findViewById(R.id.fragment_tag_swipe_refresh);
        this.swipeRefreshLayout.setColorSchemeResources(
                R.color.app_first_green_color,
                R.color.app_second_green_color,
                R.color.app_third_green_color,
                R.color.app_fourth_green_color
        );
        this.swipeRefreshLayout.setOnRefreshListener(this);
        ListView listView = (ListView)this.getView().findViewById(R.id.fragment_tag_list_view);
        this.adapter = new ArticleAdapter(this.getActivity());
        listView.addFooterView(this.getFooterLoadingView());
        listView.setOnItemClickListener(this);
        listView.setAdapter(this.adapter);
        listView.setOnScrollListener(this);
        TagArticlesAPIManager.getInstance().reloadItems(this.tagURLName, this.getAPIManagerListener);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ArticleModel articleModel = (ArticleModel)this.adapter.getItem(position);
        this.listener.onItemSelected(articleModel);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (!TagArticlesAPIManager.getInstance().isMax()) {
            if (totalItemCount != 0 && totalItemCount == visibleItemCount + firstVisibleItem) {
                TagArticlesAPIManager.getInstance().addItems(this.addAPIManagerListener);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e(TAG, "TagURLName = " + this.tagURLName + " :: onDestroyView()");
        TagArticlesAPIManager.getInstance().cancel(this.tagURLName);
    }

    @Override
    public void onStart() {
        super.onStart();
        AppController.getInstance().sendView(TAG + ":" + this.tagURLName);
    }

    private APIManagerListener<ArticleModel> getAPIManagerListener = new APIManagerListener<ArticleModel>() {
        @Override
        public void willStart() {
            self.showFooterLoadingView();
        }

        @Override
        public void onCompleted(List<ArticleModel> items) {
            self.adapter.setItems(items);
            self.adapter.notifyDataSetChanged();
            if (TagArticlesAPIManager.getInstance().isMax()) {
                self.hideFooterLoadingView();
            } else {
                self.showFooterLoadingView();
            }
            self.swipeRefreshLayout.setRefreshing(false);
        }

        @Override
        public void onError() {
            self.swipeRefreshLayout.setRefreshing(false);
            self.hideFooterLoadingView();
        }
    };

    private APIManagerListener<ArticleModel> addAPIManagerListener = new APIManagerListener<ArticleModel>() {
        @Override
        public void willStart() {
            self.showFooterLoadingView();
        }

        @Override
        public void onCompleted(List<ArticleModel> items) {
            self.adapter.addItems(items);
            self.adapter.notifyDataSetChanged();
            if (TagArticlesAPIManager.getInstance().isMax()) {
                self.hideFooterLoadingView();
            } else {
                self.showFooterLoadingView();
            }
        }

        @Override
        public void onError() {
            self.hideFooterLoadingView();
        }
    };

    private View getFooterLoadingView() {
        if (this.footerLoadingView == null) {
            this.footerLoadingView = this.getActivity().getLayoutInflater().inflate(R.layout.list_item_footer_loading, null);
        }
        return this.footerLoadingView;
    }

    private void hideFooterLoadingView() {
        View footerLoadingView = this.getFooterLoadingView();
        footerLoadingView.findViewById(R.id.progressBar).setVisibility(View.GONE);
    }

    private void showFooterLoadingView() {
        View footerLoadingView = this.getFooterLoadingView();
        footerLoadingView.findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
    }

    @Override
    public void onRefresh() {
        TagArticlesAPIManager.getInstance().reloadItems(this.tagURLName, this.getAPIManagerListener);
    }
}
