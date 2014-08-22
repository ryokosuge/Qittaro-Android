/**
 * PACKAGE NAME xyz.ryochin.qittaro.tags
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/22
 */

package xyz.ryochin.qittaro.tags;

import java.util.List;

import xyz.ryochin.qittaro.models.TagModel;

public interface TagsView {
    public void showFullLoadingView();
    public void hideFullLoadingView();
    public void showReloadLoadingView();
    public void hideReloadLoadingView();
    public void showListFooterLoadingView();
    public void hideListFooterLoadingView();
    public void setItems(List<TagModel> items);
    public void addItems(List<TagModel> items);
    public void showAPIErrorMessage();
    public void navigateToTagActivity(String urlName, String name, String iconUrl);
}
