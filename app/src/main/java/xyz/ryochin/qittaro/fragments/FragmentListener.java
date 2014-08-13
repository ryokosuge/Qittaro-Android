/**
 * PACKAGE NAME xyz.ryochin.qittaro.fragments
 * CREATED BY kosugeryou
 * CREATED AT 2014/07/27
 */
package xyz.ryochin.qittaro.fragments;

import xyz.ryochin.qittaro.models.ArticleModel;
import xyz.ryochin.qittaro.models.FollowUserModel;
import xyz.ryochin.qittaro.models.TagModel;

public interface FragmentListener {
    public void onItemSelected(ArticleModel model);
    public void onItemSelected(FollowUserModel model);
    public void onItemSelected(TagModel model);
}
