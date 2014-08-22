/**
 * PACKAGE NAME xyz.ryochin.qittaro.requests
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/22
 */
package xyz.ryochin.qittaro.requests;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class SearchRequest extends APIRequest {

    private static final String TAG = SearchRequest.class.getSimpleName();
    private static final long serialVersionUID = 4764406192706206313L;
    private final SearchRequest self = this;

    private static final String BASE_URL = "https://qiita.com/api/v1/search";
    private static final String REQUEST_ALL_ARTICLE_SEARCH_QUERY_FORMAT = "?q=%1$s&page=%2$d&per_page=%3$d";
    private static final String REQUEST_STOCKS_ARTICLE_SEARCH_QUERY_FORMAT = "?q=%1$s&stocked=%2$b&token=%3$s&page=%4$d&per_page=%5$d";

    private String searchWord;
    private boolean searchInStocks;
    private String token;

    public SearchRequest() {
        this.searchWord = null;
        this.searchInStocks = false;
        this.token = null;
    }

    public SearchRequest(boolean searchInStocks, String token) {
        this.searchWord = null;
        this.searchInStocks = searchInStocks;
        this.token = token;
    }

    public void setSearchWord(String searchWord) {
        this.searchWord = searchWord;
    }

    public void setSearchInStocks(boolean searchInStocks) {
        this.searchInStocks = searchInStocks;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String getTag() {
        return TAG;
    }

    @Override
    public String toURLString() {
        try {
            String encodeSearchWord = URLEncoder.encode(searchWord, "UTF-8");
            String requestQuery;
            if (searchInStocks && token != null) {
                requestQuery = String.format(REQUEST_STOCKS_ARTICLE_SEARCH_QUERY_FORMAT, encodeSearchWord, searchInStocks, token, page, perPage);
            } else {
                requestQuery = String.format(REQUEST_ALL_ARTICLE_SEARCH_QUERY_FORMAT, encodeSearchWord, page, perPage);
            }
            return BASE_URL + requestQuery;
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "toURLString", e);
            return null;
        }
    }
}
