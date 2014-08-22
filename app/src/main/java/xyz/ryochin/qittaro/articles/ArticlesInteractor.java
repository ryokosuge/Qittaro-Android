/**
 * PACKAGE NAME xyz.ryochin.qittaro.articles
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/20
 */
package xyz.ryochin.qittaro.articles;

import java.util.List;

import xyz.ryochin.qittaro.models.ArticleModel;
import xyz.ryochin.qittaro.requests.APIRequest;

public interface ArticlesInteractor {
    public interface Listener {
        public void onCompleted(List<ArticleModel> items, boolean isMax);
        public void onError(Exception e);
    }

    public void getItems(APIRequest request, Listener listener);
    public void addItems(APIRequest request, Listener listener);
    public void cancel(APIRequest request);
    public boolean isMax();
    public boolean isLoading();
}
