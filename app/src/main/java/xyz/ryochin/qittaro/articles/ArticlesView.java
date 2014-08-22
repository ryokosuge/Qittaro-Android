/**
 * PACKAGE NAME xyz.ryochin.qittaro.articles
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/20
 */
package xyz.ryochin.qittaro.articles;

import java.util.List;

import xyz.ryochin.qittaro.models.ArticleModel;

public interface ArticlesView {

    public void showFullLoadingView();
    public void hideFullLoadingView();
    public void showReloadLoadingView();
    public void hideReloadLoadingView();
    public void showListFooterLoadingView();
    public void hideListFooterLoadingView();
    public void setItems(List<ArticleModel> items);
    public void addItems(List<ArticleModel> items);
    public void showMessage(String title, String message);
    public void navigateToArticleActivity(String uuid);

}
