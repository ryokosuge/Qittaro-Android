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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import xyz.ryochin.qittaro.R;
import xyz.ryochin.qittaro.apimanagers.APIManagerListener;
import xyz.ryochin.qittaro.apimanagers.TagArticlesAPIManager;
import xyz.ryochin.qittaro.models.ArticleModel;
import xyz.ryochin.qittaro.utils.AppController;
import xyz.ryochin.qittaro.views.ArticleListView;

public class TagFragment extends Fragment implements ArticleListView.Listener {

    private static final String TAG = TagFragment.class.getSimpleName();
    private final TagFragment self = this;
    private static final String ARGS_URL_NAME_KEY = "urlname";

    private FragmentListener listener;
    private String tagURLName;
    private ArticleListView view;

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
        return inflater.inflate(R.layout.basic_list_view_layout, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle args = this.getArguments();
        this.tagURLName = args.getString(ARGS_URL_NAME_KEY);
        this.view = new ArticleListView(this.getActivity(), this.getView(), true, this);
        TagArticlesAPIManager.getInstance().reloadItems(this.tagURLName, this.getAPIManagerListener);
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

    @Override
    public void onResume() {
        super.onResume();
        this.view.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.view.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.view.destroy();
    }

    @Override
    public void onRefresh() {
        TagArticlesAPIManager.getInstance().reloadItems(this.tagURLName, this.reloadAPIManagerListener);
    }

    @Override
    public void onItemClicked(ArticleModel model) {
        this.listener.onItemSelected(model);
    }

    @Override
    public void onScrollEnd() {
        if (!TagArticlesAPIManager.getInstance().isMax()) {
            TagArticlesAPIManager.getInstance().addItems(this.addAPIManagerListener);
        }
    }

    private APIManagerListener<ArticleModel> getAPIManagerListener = new APIManagerListener<ArticleModel>() {
        @Override
        public void willStart() {
            self.view.setFullLoadingViewVisibility(View.VISIBLE);
            self.view.setFooterLoadingViewVisibility(View.GONE);
            self.view.setSwipeRefreshVisibility(View.GONE);
        }

        @Override
        public void onCompleted(List<ArticleModel> items) {
            self.view.setItems(items);
            if (TagArticlesAPIManager.getInstance().isMax()) {
                self.view.setFooterLoadingViewVisibility(View.GONE);
            } else {
                self.view.setFooterLoadingViewVisibility(View.VISIBLE);
            }
            self.view.setFullLoadingViewVisibility(View.GONE);
            self.view.setSwipeRefreshVisibility(View.VISIBLE);
        }

        @Override
        public void onError() {
            self.view.setFullLoadingViewVisibility(View.GONE);
            self.view.setSwipeRefreshVisibility(View.VISIBLE);
        }
    };

    private APIManagerListener<ArticleModel> reloadAPIManagerListener = new APIManagerListener<ArticleModel>() {
        @Override
        public void willStart() {
            self.view.setRefresh(true);
        }

        @Override
        public void onCompleted(List<ArticleModel> items) {
            self.view.setItems(items);
            if (TagArticlesAPIManager.getInstance().isMax()) {
                self.view.setFooterLoadingViewVisibility(View.GONE);
            } else {
                self.view.setFooterLoadingViewVisibility(View.VISIBLE);
            }
            self.view.setRefresh(false);
        }

        @Override
        public void onError() {
            self.view.setRefresh(false);
        }
    };

    private APIManagerListener<ArticleModel> addAPIManagerListener = new APIManagerListener<ArticleModel>() {
        @Override
        public void willStart() {
            self.view.setFooterLoadingViewVisibility(View.VISIBLE);
        }

        @Override
        public void onCompleted(List<ArticleModel> items) {
            self.view.addItems(items);
            if (TagArticlesAPIManager.getInstance().isMax()) {
                self.view.setFooterLoadingViewVisibility(View.GONE);
            } else {
                self.view.setFooterLoadingViewVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onError() {
            self.view.setFooterLoadingViewVisibility(View.GONE);
        }
    };
}
