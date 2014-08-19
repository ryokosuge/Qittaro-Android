
package xyz.ryochin.qittaro.followingusers;

public interface FollowingUsersPresenter {
    public void start();
    public void refresh();
    public void onItemClicked(int position);
    public void onScroll(int totalItemCount, int firstVisibleItem, int visibleItemCount);
    public void destroyView();
}
