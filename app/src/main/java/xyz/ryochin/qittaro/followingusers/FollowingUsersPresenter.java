/**
 * PACKAGE NAME xyz.ryochin.qittaro.articles.followingusers
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/20
 */
package xyz.ryochin.qittaro.followingusers;

public interface FollowingUsersPresenter {
    public void start();
    public void refresh();
    public void onItemClicked(int position);
    public void onScroll(int totalItemCount, int firstVisibleItem, int visibleItemCount);
    public void destroyView();
}
