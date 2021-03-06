/**
 * PACKAGE NAME xyz.ryochin.qittaro.adapters
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/12
 */

package xyz.ryochin.qittaro.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import xyz.ryochin.qittaro.R;

public class LoginLeftDrawerAdapter extends BaseAdapter {

    private static final String TAG = LoginLeftDrawerAdapter.class.getSimpleName();
    private final LoginLeftDrawerAdapter self = this;

    private static final int LOG_IN_LEFT_DRAWER_ITEM_COUNT = 6;
    public static final int LOG_IN_LEFT_DRAWER_ITEM_PUBLIC_INDEX = 0;
    public static final int LOG_IN_LEFT_DRAWER_ITEM_STOCKED_INDEX = 1;
    public static final int LOG_IN_LEFT_DRAWER_ITEM_MINE_INDEX = 4;
    public static final int LOG_IN_LEFT_DRAWER_ITEM_ALL_TAG_INDEX = 5;
    public static final int LOG_IN_LEFT_DRAWER_ITEM_FOLLOWING_TAG_INDEX = 2;
    public static final int LOG_IN_LEFT_DRAWER_ITEM_FOLLOWING_USER_INDEX = 3;

    private Context context;

    private static class ViewHolder {

        ImageView imageView;
        TextView textView;

        public ViewHolder(View v) {
            this.imageView = (ImageView)v.findViewById(R.id.left_drawer_menu_icon);
            this.textView = (TextView)v.findViewById(R.id.left_drawer_menu_title);
        }
    }

    public LoginLeftDrawerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return LOG_IN_LEFT_DRAWER_ITEM_COUNT;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        int titleResID = 0;
        int imgResID = 0;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(this.context);
            convertView = inflater.inflate(R.layout.drawer_list_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        switch (position) {
            case LOG_IN_LEFT_DRAWER_ITEM_PUBLIC_INDEX:
                titleResID = R.string.left_drawer_public_title;
                imgResID = R.drawable.ic_menu_public;
                break;
            case LOG_IN_LEFT_DRAWER_ITEM_STOCKED_INDEX:
                titleResID = R.string.left_drawer_stocked_title;
                imgResID = R.drawable.ic_menu_stock;
                break;
            case LOG_IN_LEFT_DRAWER_ITEM_MINE_INDEX:
                titleResID = R.string.left_drawer_mine_title;
                imgResID = R.drawable.ic_menu_mine;
                break;
            case LOG_IN_LEFT_DRAWER_ITEM_ALL_TAG_INDEX:
                titleResID = R.string.left_drawer_tag_title;
                imgResID = R.drawable.ic_menu_tag;
                break;
            case LOG_IN_LEFT_DRAWER_ITEM_FOLLOWING_TAG_INDEX:
                titleResID = R.string.left_drawer_following_tag_title;
                imgResID = R.drawable.ic_menu_following_tag;
                break;
            case LOG_IN_LEFT_DRAWER_ITEM_FOLLOWING_USER_INDEX:
                titleResID = R.string.left_drawer_following_user_title;
                imgResID = R.drawable.ic_menu_following_user;
        }

        viewHolder.imageView.setImageResource(imgResID);
        viewHolder.textView.setText(titleResID);

        return convertView;
    }
}
