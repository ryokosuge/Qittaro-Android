
/**
 * PACKAGE NAME xyz.ryochin.qittaro.article
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/21
 */
package xyz.ryochin.qittaro.article;

import xyz.ryochin.qittaro.models.ArticleDetailModel;
import xyz.ryochin.qittaro.requests.APIRequest;

public interface ArticleInteractor {

    public interface Listener {
        public void onCompleted(ArticleDetailModel model);
        public void onError(Exception e);
    }

    public void getArticle(APIRequest request, Listener listener);
    public void putStocked(APIRequest request, Listener listener);
    public void deleteStocked(APIRequest request, Listener listener);
    public boolean isLoading();
    public void cancel(APIRequest request);
}
