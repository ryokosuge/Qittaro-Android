/**
 * PACKAGE NAME xyz.ryochin.qittaro.apimanagers
 * CREATED BY kosugeryou
 * CREATED AT 2014/07/26
 */
package xyz.ryochin.qittaro.apimanagers;

import java.util.List;

public interface APIManagerListener<T> {
    public void willStart();
    public void onCompleted(List<T> items);
    public void onError();
}
