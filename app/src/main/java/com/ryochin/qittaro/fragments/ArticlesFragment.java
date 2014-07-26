/**
 * PACKAGE NAME com.ryochin.qittaro.fragments
 * CREATED BY kosugeryou
 * CREATED AT 2014/07/26
 */
package com.ryochin.qittaro.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ryochin.qittaro.R;
import com.ryochin.qittaro.adapters.ArticleAdapter;
import com.ryochin.qittaro.apimanagers.APIManagerListener;
import com.ryochin.qittaro.apimanagers.ArticleAPIManager;

public class ArticlesFragment extends Fragment {
    private static final String TAG = ArticlesFragment.class.getSimpleName();
    private final ArticlesFragment self = this;

    private ListView listView;
    private ArticleAdapter adapter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_article, container, false);
        this.listView = (ListView)rootView.findViewById(R.id.article_list_view);
        this.adapter = new ArticleAdapter(this.getActivity());
        this.listView.setAdapter(this.adapter);

        ArticleAPIManager.getInstance().reloadItems(new APIManagerListener() {
            @Override
            public void willStart() {
                Log.e(TAG, "willStart()");
            }

            @Override
            public void onCompleted() {
                Log.e(TAG, "onCompleted()");
                self.adapter.notifyDataSetChanged();
            }

            @Override
            public void onError() {
                Log.e(TAG, "onError()");
            }
        });

        return rootView;
    }
}
