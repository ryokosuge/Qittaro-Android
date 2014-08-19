
package xyz.ryochin.qittaro.articles;

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
import xyz.ryochin.qittaro.articles.models.ArticleModel;
import xyz.ryochin.qittaro.articles.models.ArticleUserModel;
import xyz.ryochin.qittaro.utils.AppController;

public class ArticlesAdapter extends BaseAdapter {
    private static final String TAG = ArticlesAdapter.class.getSimpleName();
    private final ArticlesAdapter self = this;


    private static final int USER_ICON_IMAGE_WIDTH = 50;
    private static final int USER_ICON_IMAGE_HEIGHT = 50;

    private Context context;
    private List<ArticleModel> items;

    private static class ViewHolder {
        TextView titleTextView;
        TextView detailTextView;
        ImageView userIconImageView;
        TextView counterTextView;

        public ViewHolder(View v) {
            this.titleTextView = (TextView) v.findViewById(R.id.list_item_detail_title);
            this.detailTextView = (TextView) v.findViewById(R.id.list_item_detail_sub_title);
            this.userIconImageView = (ImageView) v.findViewById(R.id.list_item_detail_image_view);
            this.counterTextView = (TextView)v.findViewById(R.id.list_item_detail_counter);
        }
    }

    public ArticlesAdapter(Context context) {
        this.context = context;
        this.items = new ArrayList<ArticleModel>();
    }

    public void setItems(List<ArticleModel> items) {
        this.items = items;
    }

    public void addItems(List<ArticleModel> items) {
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

        ViewHolder viewHolder;
        final ArticleModel articleModel = this.items.get(position);
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(this.context);
            convertView = inflater.inflate(R.layout.list_item_detail, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ImageLoader.ImageContainer imageContainer =
                (ImageLoader.ImageContainer)viewHolder.userIconImageView.getTag();
        if (imageContainer != null) {
            imageContainer.cancelRequest();
        }

        ArticleUserModel userModel = articleModel.getUser();
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        ImageLoader.ImageListener imageListener = imageLoader.getImageListener(
                viewHolder.userIconImageView,
                R.drawable.ic_launcher,
                android.R.drawable.ic_dialog_alert);
        viewHolder.userIconImageView.setTag(
                imageLoader.get(
                        userModel.getProfileImageUrl(),
                        imageListener,
                        USER_ICON_IMAGE_WIDTH,
                        USER_ICON_IMAGE_HEIGHT
                )
        );
        viewHolder.titleTextView.setText(articleModel.getTitle());
        String articlePostDetail = this.context.getResources().getString(
                R.string.article_post_detail_format,
                userModel.getUrlName(),
                articleModel.getCreatedAtInWords());
        viewHolder.detailTextView.setText(articlePostDetail);
        String counterText = this.context.getResources().getString(R.string.article_detail_counter_text, articleModel.getCommentCount(), articleModel.getStockCount());
        viewHolder.counterTextView.setText(counterText);
        return convertView;
    }
}
