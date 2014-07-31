/**
 * PACKAGE NAME xyz.ryochin.qittaro.fragments
 * CREATED BY kosugeryou
 * CREATED AT 2014/07/29
 */

package xyz.ryochin.qittaro.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import xyz.ryochin.qittaro.R;
import xyz.ryochin.qittaro.adapters.ArticleAdapter;
import xyz.ryochin.qittaro.apimanagers.APIManagerListener;
import xyz.ryochin.qittaro.apimanagers.SearchArticlesAPIManager;
import xyz.ryochin.qittaro.models.ArticleModel;
import xyz.ryochin.qittaro.utils.AppSharedPreference;

public class SearchArticleFragment extends Fragment implements AdapterView.OnItemClickListener, TextView.OnEditorActionListener, AbsListView.OnScrollListener, View.OnClickListener {

    private static final String TAG = SearchArticleFragment.class.getSimpleName();
    private final SearchArticleFragment self = this;

    private static final String SAVED_SEARCH_WORD_KEY = "searchWord";

    private FragmentListener listener;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArticleAdapter adapter;
    private EditText searchWordEditText;
    private CheckBox searchInStockedCheckBox;
    private View footerLoadingView;
    private String searchWord = "";

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if ((activity instanceof FragmentListener)) {
            this.listener = (FragmentListener)activity;
        } else {
            throw new ClassCastException("activity が ArticlesFragmentListener を実装していません.");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            this.searchWord = savedInstanceState.getString(SAVED_SEARCH_WORD_KEY);
        }
        ListView listView = (ListView) this.getView().findViewById(R.id.search_article_list_view);
        listView.addFooterView(this.getFooterLoadingView());
        this.hideFooterLoadingView();
        this.searchWordEditText = (EditText)this.getView().findViewById(R.id.search_edit_text);
        this.searchInStockedCheckBox = (CheckBox)this.getView().findViewById(R.id.search_in_stoked_check_box);
        if (!AppSharedPreference.isLoggedIn(this.getActivity())) {
            this.searchInStockedCheckBox.setVisibility(View.GONE);
        } else {
            this.searchInStockedCheckBox.setOnClickListener(this);
        }
        this.swipeRefreshLayout = (SwipeRefreshLayout)this.getView().findViewById(R.id.search_article_swipe_refresh);
        this.searchWordEditText.setOnEditorActionListener(this);
        this.adapter = new ArticleAdapter(this.getActivity());
        listView.setAdapter(this.adapter);
        this.swipeRefreshLayout.setColorScheme(
                R.color.app_main_green_color,
                R.color.app_main_bleu_color,
                R.color.app_main_orange_color,
                R.color.app_main_red_color
        );
        this.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                SearchArticlesAPIManager.getInstance().reloadItems(self.managerListener);
            }
        });
        listView.setOnItemClickListener(this);
        listView.setOnScrollListener(this);

        if (!this.searchWord.equals("")) {
            this.searchWordEditText.setText(this.searchWord);
            this.getSearchArticle();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        SearchArticlesAPIManager.getInstance().cancel();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (this.searchWord != null) {
            outState.putString(SAVED_SEARCH_WORD_KEY, this.searchWord);
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            if (event.getAction() == KeyEvent.ACTION_UP) {
                InputMethodManager inputMethodManager =
                        (InputMethodManager)this.getActivity()
                                .getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                this.searchWord = this.searchWordEditText.getText().toString();
                this.getSearchArticle();
            }
            return true;
        }
        return false;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (!SearchArticlesAPIManager.getInstance().isMax()) {
            if (this.adapter.getCount() > 0 && !this.searchWord.equals("") && totalItemCount != 0 && totalItemCount == firstVisibleItem + visibleItemCount) {
                SearchArticlesAPIManager.getInstance().addItems(this.addManagerListener);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ArticleModel articleModel = (ArticleModel)this.adapter.getItem(position);
        this.listener.onItemSelected(articleModel);
    }

    @Override
    public void onClick(View v) {
        String searchWord = this.searchWordEditText.getText().toString();
        if (searchWord == "") {
            return ;
        }
        this.searchWord = searchWord;
        this.getSearchArticle();
    }

    private void getSearchArticle() {
        SearchArticlesAPIManager.getInstance().cancel();
        this.adapter.clear();
        this.adapter.notifyDataSetChanged();
        this.showFooterLoadingView();
        boolean searchInStocked = this.searchInStockedCheckBox.isChecked();
        String token = AppSharedPreference.getToken(this.getActivity());
        SearchArticlesAPIManager.getInstance()
                .getItems(this.searchWord, searchInStocked, token, this.managerListener);
    }

    private APIManagerListener<ArticleModel> managerListener = new APIManagerListener<ArticleModel>() {
        @Override
        public void willStart() {
            self.showFooterLoadingView();
        }

        @Override
        public void onCompleted(List<ArticleModel> items) {
            if (items.size() > 0) {
                self.adapter.setItems(items);
                self.adapter.notifyDataSetChanged();
                self.swipeRefreshLayout.setRefreshing(false);
                if (SearchArticlesAPIManager.getInstance().isMax()) {
                    self.hideFooterLoadingView();
                } else {
                    self.showFooterLoadingView();
                }
            } else {
                self.hideFooterLoadingView();
                self.listener.showSearchEmptyMessage(self.searchWord);
            }
        }

        @Override
        public void onError() {
            self.swipeRefreshLayout.setRefreshing(false);
        }
    };

    private APIManagerListener<ArticleModel> addManagerListener = new APIManagerListener<ArticleModel>() {
        @Override
        public void willStart() {
        }

        @Override
        public void onCompleted(List<ArticleModel> items) {
            self.adapter.addItems(items);
            self.adapter.notifyDataSetChanged();
            if (SearchArticlesAPIManager.getInstance().isMax()) {
                self.hideFooterLoadingView();
            } else {
                self.showFooterLoadingView();
            }
        }

        @Override
        public void onError() {
        }
    };

    private View getFooterLoadingView() {
        if (this.footerLoadingView == null) {
            this.footerLoadingView = this.getActivity()
                    .getLayoutInflater().inflate(R.layout.fragment_article_loading, null);
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
}
