/**
 * PACKAGE NAME xyz.ryochin.qittaro.articles
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/20
 */
package xyz.ryochin.qittaro.articles;

public interface ArticlesPresenter {
    public void start();
    public void refresh();
    public void onItemClicked(int position);
    public void onScroll(int totalItemCount, int firstVisibleItem, int visibleItemCount);
    public String getRequestTag();
    public void destroyView();
}
