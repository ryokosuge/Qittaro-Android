/**
 * PACKAGE NAME com.ryochin.qittaro.fragments
 * CREATED BY kosugeryou
 * CREATED AT 2014/07/27
 */
package com.ryochin.qittaro.fragments;

import com.ryochin.qittaro.models.ArticleModel;

public interface FragmentListener {
    public void onItemSelected(ArticleModel model);
    public void onCompletedLoggedin(boolean result);
}
