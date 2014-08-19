

package xyz.ryochin.qittaro.tags;

public interface TagsPresenter {
    public void start();
    public void refresh();
    public void onItemClicked(int position);
    public void onScroll(int totalItemCount, int firstVisibleItem, int visibleItemCount);
    public void destroyView();
}
