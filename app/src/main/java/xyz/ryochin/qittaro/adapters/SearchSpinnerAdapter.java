/**
 * =====================================================
 * ENCODE : UTF-8
 * CREATED AT 14/08/08.
 * CREATED BY kosuge.
 * Copyright © Samurai Factory Inc. All rights reserved.
 * ===================================================== 
 */

package xyz.ryochin.qittaro.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import xyz.ryochin.qittaro.R;

public class SearchSpinnerAdapter extends BaseAdapter {
    private static final String TAG = SearchSpinnerAdapter.class.getSimpleName();
    private final SearchSpinnerAdapter self = this;

    private Context context;
    private String[] titles;
    private String searchWord;

    private static class ViewHolder {
        TextView titleTextView;
        TextView searchWordTextView;

        public ViewHolder(View v) {
            this.titleTextView = (TextView)v.findViewById(R.id.search_spinner_title);
            this.searchWordTextView = (TextView)v.findViewById(R.id.search_spinner_search_word);
        }
    }

    private static class DropDownViewHolder {
        TextView titleTextView;

        public DropDownViewHolder(View v) {
            this.titleTextView = (TextView)v.findViewById(R.id.search_spinner_drop_down_title);
        }
    }

    public SearchSpinnerAdapter(Context context, String[] titles) {
        this.context = context;
        this.titles = titles;
    }

    public void setSearchWord(String searchWord) {
        this.searchWord = searchWord;
    }

    @Override
    public int getCount() {
        return this.titles.length;
    }

    @Override
    public Object getItem(int position) {
        return this.titles[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return this.getCustomView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        DropDownViewHolder viewHolder;
        String title = this.titles[position];

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(this.context);
            convertView = inflater.inflate(R.layout.fragment_search_spinner_drop_down_view, parent, false);
            viewHolder = new DropDownViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (DropDownViewHolder)convertView.getTag();
        }

        viewHolder.titleTextView.setText(title);
        return convertView;
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        String title = this.titles[position];

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(this.context);
            convertView = inflater.inflate(R.layout.fragment_search_spinner_view, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        String prefix = this.context.getResources().getString(R.string.search_sub_title_prefix);
        String subTitle = prefix + " : " + this.searchWord;

        viewHolder.titleTextView.setText(title);
        viewHolder.searchWordTextView.setText(subTitle);

        return convertView;
    }
}
