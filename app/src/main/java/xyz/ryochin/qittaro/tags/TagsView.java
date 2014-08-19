

package xyz.ryochin.qittaro.tags;

import java.util.List;

import xyz.ryochin.qittaro.tags.models.TagModel;

public interface TagsView {
    public void showFullLoadingView();
    public void hideFullLoadingView();
    public void showReloadLoadingView();
    public void hideReloadLoadingView();
    public void showListFooterLoadingView();
    public void hideListFooterLoadingView();
    public void setItems(List<TagModel> items);
    public void addItems(List<TagModel> items);
    public void showMessage(String title, String message);
    public void navigateToTagActivity(String urlName, String name, String iconUrl);
}
