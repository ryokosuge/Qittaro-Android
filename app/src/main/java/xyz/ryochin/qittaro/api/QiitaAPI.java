
package xyz.ryochin.qittaro.api;

import java.util.Map;

import xyz.ryochin.qittaro.requests.Request;

public interface QiitaAPI {
    public void getItems(Request request, OnFinishedListener listener);
    public void post(Request request, Map<String, String> params, OnFinishedListener listener);
    public void cancel(Request request);
}
