
/**
 * PACKAGE NAME xyz.ryochin.qittaro.articles.search
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/21
 */
package xyz.ryochin.qittaro.articles.search;

public interface SearchPresenter {
    public void onItemClicked(int position);
    public void onScroll(int totalItemCount, int firstVisibleItem, int visibleItemCount);
    public void refresh();
    public void queryTextSubmit(String searchWord);
    public void changeSearchALL();
    public void changeSearchStocks(String token);
    public void destroyView();
}
