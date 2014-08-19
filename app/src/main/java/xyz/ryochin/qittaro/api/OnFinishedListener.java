
package xyz.ryochin.qittaro.api;

public interface OnFinishedListener {
    public void onFinished(String jsonResponse);
    public void onError(Exception exception);
}
