/**
 * PACKAGE NAME xyz.ryochin.qittaro.api
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/20
 */

package xyz.ryochin.qittaro.api;

public interface OnFinishedListener {
    public void onFinished(String jsonResponse);
    public void onError(Exception exception);
}
