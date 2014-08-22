/**
 * PACKAGE NAME xyz.ryochin.qittaro.articles.search
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/21
 */

package xyz.ryochin.qittaro.articles.search;

import java.util.List;

import xyz.ryochin.qittaro.models.ArticleModel;

public interface SearchView {
    public void showFullLoadingView();
    public void hideFullLoadingView();
    public void showReloadLoadingView();
    public void hideReloadLoadingView();
    public void showListFooterLoadingView();
    public void hideListFooterLoadingView();
    public void setItems(List<ArticleModel> items);
    public void addItems(List<ArticleModel> items);
    public void setActionBarTitle(String searchWord);
    public void showNoArticleMessage(String searchWord);
    public void showAPIErrorMessage();
    public void navigateToArticleActivity(String uuid);
    public void changeSearchType(SearchFragment.Type type);
}
