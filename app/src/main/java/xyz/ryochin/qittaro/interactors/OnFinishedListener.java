
package xyz.ryochin.qittaro.interactors;

public interface OnFinishedListener {
    public void onFinished(String jsonResponse);
    public void onError(Exception exception);
}
