
package xyz.ryochin.qittaro.interactors;

import xyz.ryochin.qittaro.requests.Request;

public interface Interactor {
    public void getItems(Request request, OnFinishedListener listener);
    public void cancel(Request request);
}
