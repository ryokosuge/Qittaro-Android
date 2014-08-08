/**
 * PACKAGE NAME xyz.ryochin.qittaro.fragments
 * CREATED BY kosugeryou
 * CREATED AT 2014/07/29
 */

package xyz.ryochin.qittaro.fragments;


import android.view.MenuItem;
import xyz.ryochin.qittaro.models.ArticleModel;

public interface SearchFragmentListener {
    public void noSerchArticle(String searchWord);
    public void onItemClicked(ArticleModel model);
    public void setSearchWord(String searchWord);
    public void onOptionMenuClicked(MenuItem menu);
}