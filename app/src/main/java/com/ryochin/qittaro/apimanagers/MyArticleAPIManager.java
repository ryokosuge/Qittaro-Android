/**
 * PACKAGE NAME com.ryochin.qittaro.apimanagers
 * CREATED BY kosugeryou
 * CREATED AT 2014/07/29
 */

package com.ryochin.qittaro.apimanagers;

import com.ryochin.qittaro.models.ArticleModel;

import java.util.List;

public class MyArticleAPIManager {

    private static final String TAG = MyArticleAPIManager.class.getSimpleName();
    private final MyArticleAPIManager self = this;

    private static MyArticleAPIManager instance;
    private int page;
    private boolean loading;
    private List<ArticleModel> items;


}
