/**
 * PACKAGE NAME com.ryochin.qittaro.apimanagers
 * CREATED BY kosugeryou
 * CREATED AT 2014/07/26
 */
package com.ryochin.qittaro.apimanagers;

public interface APIManagerListener {
    public void willStart();
    public void onCompleted(String response);
    public void onError();
}
