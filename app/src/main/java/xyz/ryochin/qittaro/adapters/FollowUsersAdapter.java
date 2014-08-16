/**
 * =====================================================
 * ENCODE : UTF-8
 * CREATED AT 14/08/13.
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
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import xyz.ryochin.qittaro.R;
import xyz.ryochin.qittaro.models.FollowUserModel;
import xyz.ryochin.qittaro.utils.AppController;

public class FollowUsersAdapter extends BaseAdapter {

    private static final String TAG = FollowUsersAdapter.class.getSimpleName();
    private final FollowUsersAdapter self = this;
    private List<FollowUserModel> items;
    private Context context;
    private int userIconMaxWidth;
    private int userIconMaxHeight;

    private static class ViewHolder {
        ImageView userIconImageView;
        TextView userNameTextView;

        public ViewHolder(View v) {
            this.userIconImageView = (ImageView)v.findViewById(R.id.list_item_simple_image_view);
            this.userNameTextView = (TextView)v.findViewById(R.id.list_item_simple_title);
        }
    }

    public FollowUsersAdapter(Context context) {
        this(new ArrayList<FollowUserModel>(), context);
    }

    public FollowUsersAdapter(List<FollowUserModel> items, Context context) {
        this.items = items;
        this.context = context;
        this.userIconMaxWidth = (int)this.context.getResources().getDimension(R.dimen.user_icon_width);
        this.userIconMaxHeight = (int)this.context.getResources().getDimension(R.dimen.user_icon_height);
    }

    public void setItems(List<FollowUserModel> items) {
        this.items = items;
    }

    public void addItems(List<FollowUserModel> items) {
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
        return this.items.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        final FollowUserModel model = this.items.get(position);
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(this.context);
            convertView = inflater.inflate(R.layout.list_item_simple, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        ImageLoader.ImageContainer imageContainer =
                (ImageLoader.ImageContainer)viewHolder.userIconImageView.getTag();
        if (imageContainer != null) {
            imageContainer.cancelRequest();
        }

        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        ImageLoader.ImageListener imageListener =
                imageLoader.getImageListener(
                        viewHolder.userIconImageView,
                        R.drawable.ic_launcher,
                        android.R.drawable.ic_dialog_alert
                );
        viewHolder.userIconImageView.setTag(
                imageLoader.get(
                        model.getProfileImageURL(),
                        imageListener,
                        this.userIconMaxWidth,
                        this.userIconMaxHeight
                )
        );
        viewHolder.userNameTextView.setText(model.getUrlName());
        return convertView;
    }
}
