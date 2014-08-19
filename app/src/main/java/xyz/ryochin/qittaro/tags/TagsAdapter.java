

package xyz.ryochin.qittaro.tags;

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
import xyz.ryochin.qittaro.tags.models.TagModel;
import xyz.ryochin.qittaro.utils.AppController;

public class TagsAdapter extends BaseAdapter {
    private static final String TAG = TagsAdapter.class.getSimpleName();
    private final TagsAdapter self = this;

    private Context context;
    private List<TagModel> items;
    private int iconMaxWidth;
    private int iconMaxHeight;

    private static class ViewHolder {
        private ImageView icon;
        private TextView tagName;

        public ViewHolder(View v) {
            this.icon = (ImageView)v.findViewById(R.id.list_item_simple_image_view);
            this.tagName = (TextView)v.findViewById(R.id.list_item_simple_title);
        }
    }

    public TagsAdapter(Context context) {
        this(context, new ArrayList<TagModel>());
    }

    public TagsAdapter(Context context, List<TagModel> items) {
        this.context = context;
        this.items = items;
        this.iconMaxWidth = (int)this.context.getResources().getDimension(R.dimen.user_icon_width);
        this.iconMaxHeight = (int)this.context.getResources().getDimension(R.dimen.user_icon_height);
    }

    public void setItems(List<TagModel> items) {
        this.items = items;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        TagModel tagModel = this.items.get(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(this.context);
            convertView = inflater.inflate(R.layout.list_item_simple, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        ImageLoader.ImageContainer imageContainer = (ImageLoader.ImageContainer)viewHolder.icon.getTag();
        if (imageContainer != null) {
            imageContainer.cancelRequest();
        }

        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        ImageLoader.ImageListener imageListener = imageLoader.getImageListener(
                viewHolder.icon,
                R.drawable.ic_launcher,
                android.R.drawable.ic_dialog_alert
        );
        viewHolder.icon.setTag(
                imageLoader.get(
                        tagModel.getIconUrl(),
                        imageListener,
                        this.iconMaxWidth,
                        this.iconMaxHeight
                )
        );
        viewHolder.tagName.setText(tagModel.getName());
        return convertView;
    }
}
