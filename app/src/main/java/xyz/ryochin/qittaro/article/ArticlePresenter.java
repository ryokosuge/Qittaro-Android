/**
 * PACKAGE NAME xyz.ryochin.qittaro.article
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/21
 */
package xyz.ryochin.qittaro.article;

import android.app.Activity;

public interface ArticlePresenter {
    public void start();
    public void destroyView();
    public void stock(String token);
    public void share(Activity activity);
    public void openBrowser();
    public void onItemClicked(int position);
}
