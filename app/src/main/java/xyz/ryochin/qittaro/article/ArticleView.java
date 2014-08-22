/**
 * PACKAGE NAME xyz.ryochin.qittaro.article
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/21
 */

package xyz.ryochin.qittaro.article;

import android.webkit.WebViewClient;

import java.util.List;

import xyz.ryochin.qittaro.models.ArticleInfoModel;

public interface ArticleView {
    public void showFullLoadingView();
    public void hideFullLoadingView();
    public void showActionBarItem(boolean stocked);
    public void hideActionBarItem();
    public void showActionBarProgress();
    public void hideActionBarProgress(boolean stocked);
    public void showLoadingTitle();
    public void showArticleTitle(String title);
    public void showSuccessStockMessage(boolean stocked);
    public void setWebView(WebViewClient webViewClient, String url);
    public void setSideInfoItems(List<ArticleInfoModel> items);
    public void navigateToUserActivity(String urlName);
    public void navigateToTagActivity(String urlName, String tagName, String tagIconUrl);
    public void navigateToBrowser(String articleUrl);
    public void showAPIErrorMessage();
}
