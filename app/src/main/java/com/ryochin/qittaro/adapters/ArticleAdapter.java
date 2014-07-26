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

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.ryochin.qittaro.R;
import com.ryochin.qittaro.apimanagers.ArticleAPIManager;
import com.ryochin.qittaro.models.ArticleModel;
import com.ryochin.qittaro.models.ArticleUserModel;
import com.ryochin.qittaro.models.TagModel;
import com.ryochin.qittaro.utils.AppController;

public class ArticleAdapter extends BaseAdapter {
    private static final String TAG = ArticleAdapter.class.getSimpleName();
    private final ArticleAdapter self = this;

    private Context context;

    private static class ViewHolder {
        NetworkImageView tagImageView;
        TextView titleTextView;
        TextView createdAtTextView;
        TextView updatedAtTextView;
        NetworkImageView userIconImageView;
        TextView userNameTextView;
    }


    public ArticleAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return ArticleAPIManager.getInstance().getCount();
    }

    @Override
    public Object getItem(int position) {
        return ArticleAPIManager.getInstance().getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        final ArticleModel articleModel = ArticleAPIManager.getInstance().getItem(position);
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(this.context);
            convertView = inflater.inflate(R.layout.fragment_article_detail, null);
            viewHolder = new ViewHolder();
            viewHolder.tagImageView = (NetworkImageView)convertView.findViewById(R.id.article_first_tag_image_view);
            viewHolder.titleTextView = (TextView)convertView.findViewById(R.id.article_title_text_view);
            viewHolder.createdAtTextView = (TextView)convertView.findViewById(R.id.article_created_at_text_view);
            viewHolder.updatedAtTextView = (TextView)convertView.findViewById(R.id.article_updated_at_text_view);
            viewHolder.userNameTextView = (TextView)convertView.findViewById(R.id.article_user_name_text_view);
            viewHolder.userIconImageView = (NetworkImageView)convertView.findViewById(R.id.article_user_icon_image_view);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        TagModel firstTagModel = articleModel.getTags().get(0);
        viewHolder.tagImageView.setImageUrl(firstTagModel.getIconURL(), imageLoader);
        ArticleUserModel userModel = articleModel.getUser();
        viewHolder.userIconImageView.setImageUrl(userModel.getProfileImageURL(), imageLoader);

        viewHolder.titleTextView.setText(articleModel.getTitle());
        String createdAt = "投稿日 : " + articleModel.getCreatedAtInWords();
        String updatedAt = "更新日 : " + articleModel.getUpdatedAtInWords();
        viewHolder.createdAtTextView.setText(createdAt);
        viewHolder.updatedAtTextView.setText(updatedAt);
        viewHolder.userNameTextView.setText(userModel.getUrlName());
        return convertView;
    }
}
