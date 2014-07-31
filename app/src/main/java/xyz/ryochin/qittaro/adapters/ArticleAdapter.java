/**
 * PACKAGE NAME xyz.ryochin.qittaro.adapters
 * CREATED BY kosugeryou
 * CREATED AT 2014/07/27
 */
package xyz.ryochin.qittaro.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.List;

import xyz.ryochin.qittaro.R;
import xyz.ryochin.qittaro.models.ArticleModel;
import xyz.ryochin.qittaro.models.ArticleUserModel;
import xyz.ryochin.qittaro.utils.AppController;

public class ArticleAdapter extends BaseAdapter {
    private static final String TAG = ArticleAdapter.class.getSimpleName();
    private final ArticleAdapter self = this;

    private Context context;
    private List<ArticleModel> items;

    private static class ViewHolder {
        TextView titleTextView;
        TextView createdAtTextView;
        TextView updatedAtTextView;
        NetworkImageView userIconImageView;
        TextView userNameTextView;

        public ViewHolder(View v) {
            this.titleTextView = (TextView) v.findViewById(R.id.article_title_text_view);
            this.createdAtTextView = (TextView) v.findViewById(R.id.article_created_at_text_view);
            this.updatedAtTextView = (TextView) v.findViewById(R.id.article_updated_at_text_view);
            this.userNameTextView = (TextView) v.findViewById(R.id.article_user_name_text_view);
            this.userIconImageView = (NetworkImageView) v.findViewById(R.id.article_user_icon_image_view);
        }
    }

    public ArticleAdapter(Context context) {
        this.context = context;
        this.items = new ArrayList<ArticleModel>();
    }

    public void clear() {
        this.items.clear();
    }

    public void setItems(List<ArticleModel> items) {
        this.items = items;
    }

    public void addItem(ArticleModel item) {
        this.items.add(item);
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
            convertView = inflater.inflate(R.layout.fragment_article_list_detail, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        ArticleUserModel userModel = articleModel.getUser();
        viewHolder.userIconImageView.setImageUrl(userModel.getProfileImageURL(), imageLoader);
        viewHolder.titleTextView.setText(articleModel.getTitle());
        String userText = userModel.getUrlName() + "さんが投稿しました。";
        String createdAt = "投稿日 : " + articleModel.getCreatedAtInWords() + "前";
        String updatedAt = "更新日 : " + articleModel.getUpdatedAtInWords() + "前";
        viewHolder.createdAtTextView.setText(createdAt);
        viewHolder.updatedAtTextView.setText(updatedAt);
        viewHolder.userNameTextView.setText(userText);
        return convertView;
    }
}
