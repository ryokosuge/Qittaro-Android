/**
 * PACKAGE NAME com.ryochin.qittaro.apimanagers
 * CREATED BY kosugeryou
 * CREATED AT 2014/07/26
 */
package com.ryochin.qittaro.apimanagers;

import java.util.List;

public interface APIManagerListener<T> {
    public void onCompleted(List<T> items);
    public void onError();
}
