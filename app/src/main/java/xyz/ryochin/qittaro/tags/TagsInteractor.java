/**
 * PACKAGE NAME xyz.ryochin.qittaro.tags
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/22
 */

package xyz.ryochin.qittaro.tags;

import java.util.List;

import xyz.ryochin.qittaro.requests.APIRequest;
import xyz.ryochin.qittaro.models.TagModel;

public interface TagsInteractor {
    public interface Listener {
        public void onCompleted(List<TagModel> items, boolean isMax);
        public void onError(Exception e);
    }

    public void getItems(APIRequest request, Listener listener);
    public void addItems(APIRequest request, Listener listener);
    public void cancel(APIRequest request);
    public boolean isMax();
    public boolean isLoading();
}
