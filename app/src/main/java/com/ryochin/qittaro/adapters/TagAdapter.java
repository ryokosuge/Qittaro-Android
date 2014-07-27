/**
 * PACKAGE NAME com.ryochin.qittaro.adapters
 * CREATED BY kosugeryou
 * CREATED AT 2014/07/27
 */
package com.ryochin.qittaro.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ryochin.qittaro.R;
import com.ryochin.qittaro.models.TagModel;

import java.util.ArrayList;
import java.util.List;

public class TagAdapter extends BaseAdapter {
    private static final String TAG = TagAdapter.class.getSimpleName();
    private final TagAdapter self = this;

    private Context context;
    private List<TagModel> items;

    private static class ViewHolder {
        TextView nameTextView;
        TextView itemCountTextView;

        public ViewHolder(View v) {
            this.nameTextView = (TextView)v.findViewById(R.id.spinner_tag_name);
            this.itemCountTextView = (TextView)v.findViewById(R.id.spinner_tag_item_count);
        }
    }

    public TagAdapter(Context context) {
        this.context = context;
        this.items = new ArrayList<TagModel>();
    }

    public void setItems(List<TagModel> items) {
        this.items = items;
    }

    public void addItem(TagModel item) {
        this.items.add(item);
    }

    public void addItems(List<TagModel> items) {
        this.items.addAll(items);
    }

    @Override
    public int getCount() {
        return this.items.size();
    }

    @Override
    public Object getItem(int position) {
        return this.items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return this.getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return this.getCustomView(position, convertView, parent);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        final TagModel tagModel = this.items.get(position);

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(this.context);
            convertView = inflater.inflate(R.layout.fragment_tags_spinner_detail, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.nameTextView.setText(tagModel.getName());
        String itemCountStr = "記事数 : " + tagModel.getItemCount() + "  /  フォロー数 : " + tagModel.getFollowerCount();
        viewHolder.itemCountTextView.setText(itemCountStr);

        return convertView;
    }
}
