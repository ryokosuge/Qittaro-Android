/**
 * PACKAGE NAME xyz.ryochin.qittaro.adapters
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/15
 */

package xyz.ryochin.qittaro.adapters;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import xyz.ryochin.qittaro.R;
import xyz.ryochin.qittaro.models.ArticleInfoModel;
import xyz.ryochin.qittaro.models.ArticleInfoModelType;
import xyz.ryochin.qittaro.utils.AppController;
import xyz.ryochin.qittaro.utils.URLImageParser;

public class ArticleInfoAdapter extends BaseAdapter {

    private static final String TAG = ArticleInfoAdapter.class.getSimpleName();
    private final ArticleInfoAdapter self = this;

    private Context context;
    private List<ArticleInfoModel> items;

    public ArticleInfoAdapter(Context context, List<ArticleInfoModel> items) {
        this.context = context;
        this.items = items;
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
    public boolean isEnabled(int position) {
        ArticleInfoModel model = this.items.get(position);
        return (model.getType() == ArticleInfoModelType.Tag ||
                model.getType() == ArticleInfoModelType.User ||
                model.getType() == ArticleInfoModelType.StockUser);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ArticleInfoModel model = this.items.get(position);
        switch (model.getType()) {
            case Title:
                return this.getInfoTitleView(model, convertView);
            case User:
                return this.getInfoUserView(model, convertView);
            case Tag:
                return this.getInfoTagView(model, convertView);
            case Comment:
                return this.getInfoCommentView(model, convertView);
            case StockUser:
                return this.getInfoStockUserView(model, convertView);
            default:
                return null;
        }
    }

    private View getInfoStockUserView(ArticleInfoModel model, View convertView) {
        StockUserViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(this.context);
            convertView = inflater.inflate(R.layout.article_info_stock_user_layout, null);
            viewHolder = new StockUserViewHolder(convertView);
            convertView.setTag(R.string.article_info_stock_user_tag_id, viewHolder);
        } else {
            viewHolder = (StockUserViewHolder)convertView.getTag(R.string.article_info_stock_user_tag_id);
            if (viewHolder == null) {
                LayoutInflater inflater = LayoutInflater.from(this.context);
                convertView = inflater.inflate(R.layout.article_info_stock_user_layout, null);
                viewHolder = new StockUserViewHolder(convertView);
                convertView.setTag(R.string.article_info_stock_user_tag_id, viewHolder);
            }
        }

        viewHolder.userName.setText(model.getTitle());
        return convertView;
    }

    private View getInfoTitleView(ArticleInfoModel model, View convertView) {
        TitleViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(this.context);
            convertView = inflater.inflate(R.layout.article_info_title_layout, null);
            viewHolder = new TitleViewHolder(convertView);
            convertView.setTag(R.string.article_info_title_tag_id, viewHolder);
        } else {
            viewHolder = (TitleViewHolder)convertView.getTag(R.string.article_info_title_tag_id);
            if (viewHolder == null) {
                LayoutInflater inflater = LayoutInflater.from(this.context);
                convertView = inflater.inflate(R.layout.article_info_title_layout, null);
                viewHolder = new TitleViewHolder(convertView);
                convertView.setTag(R.string.article_info_title_tag_id, viewHolder);
            }
        }

        viewHolder.title.setText(model.getTitle());
        return convertView;
    }

    private View getInfoUserView(ArticleInfoModel model, View convertView) {
        UserViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(this.context);
            convertView = inflater.inflate(R.layout.article_info_user_layout, null);
            viewHolder = new UserViewHolder(convertView);
            convertView.setTag(R.string.article_info_user_tag_id, viewHolder);
        } else {
            viewHolder = (UserViewHolder)convertView.getTag(R.string.article_info_user_tag_id);
            if (viewHolder == null) {
                LayoutInflater inflater = LayoutInflater.from(this.context);
                convertView = inflater.inflate(R.layout.article_info_user_layout, null);
                viewHolder = new UserViewHolder(convertView);
                convertView.setTag(R.string.article_info_user_tag_id, viewHolder);
            }
        }

        viewHolder.userName.setText(model.getTitle());

        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        viewHolder.icon.setImageUrl(model.getIconURL(), imageLoader);
        return convertView;
    }

    private View getInfoTagView(ArticleInfoModel model, View convertView) {
        TagViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(this.context);
            convertView = inflater.inflate(R.layout.article_info_tag_layout, null);
            viewHolder = new TagViewHolder(convertView);
            convertView.setTag(R.string.article_info_tag_tag_id, viewHolder);
        } else {
            viewHolder = (TagViewHolder)convertView.getTag(R.string.article_info_tag_tag_id);
            if (viewHolder == null) {
                LayoutInflater inflater = LayoutInflater.from(this.context);
                convertView = inflater.inflate(R.layout.article_info_tag_layout, null);
                viewHolder = new TagViewHolder(convertView);
                convertView.setTag(R.string.article_info_tag_tag_id, viewHolder);
            }
        }

        viewHolder.tagName.setText(model.getTitle());
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        viewHolder.icon.setImageUrl(model.getIconURL(), imageLoader);
        return convertView;
    }

    private View getInfoCommentView(ArticleInfoModel model, View convertView) {
        CommentViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(this.context);
            convertView = inflater.inflate(R.layout.article_info_comment_layout, null);
            viewHolder = new CommentViewHolder(convertView);
            convertView.setTag(R.string.article_info_comment_tag_id, viewHolder);
        } else {
            viewHolder = (CommentViewHolder)convertView.getTag(R.string.article_info_comment_tag_id);
            if (viewHolder == null) {
                LayoutInflater inflater = LayoutInflater.from(this.context);
                convertView = inflater.inflate(R.layout.article_info_comment_layout, null);
                viewHolder = new CommentViewHolder(convertView);
                convertView.setTag(R.string.article_info_comment_tag_id, viewHolder);
            }
        }

        MovementMethod movementMethod = LinkMovementMethod.getInstance();
        viewHolder.body.setMovementMethod(movementMethod);
        URLImageParser imageParser = new URLImageParser(viewHolder.body, this.context);
        // CharSequence bodyHtml = Html.fromHtml(model.getBody(), imageParser, null);
        Spanned bodyHtml = Html.fromHtml(model.getBody(), imageParser, null);
        Log.e(TAG, "model.getBody() = " + model.getBody());
        viewHolder.body.setText(bodyHtml);
        viewHolder.userName.setText(model.getTitle());
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        viewHolder.userIcon.setImageUrl(model.getIconURL(), imageLoader);

        return convertView;
    }

    private static class TitleViewHolder {
        TextView title;

        public TitleViewHolder(View v) {
            this.title = (TextView)v.findViewById(R.id.article_info_title);
        }
    }

    private static class UserViewHolder {

        NetworkImageView icon;
        TextView userName;

        public UserViewHolder(View v) {
            this.icon = (NetworkImageView)v.findViewById(R.id.article_info_user_icon);
            this.userName = (TextView)v.findViewById(R.id.article_info_user_name);
        }

    }

    private static class TagViewHolder {

        NetworkImageView icon;
        TextView tagName;

        public TagViewHolder(View v) {
            this.icon = (NetworkImageView)v.findViewById(R.id.article_info_tag_icon);
            this.tagName = (TextView)v.findViewById(R.id.article_info_tag_name);
        }
    }

    private static class CommentViewHolder {
        TextView body;
        NetworkImageView userIcon;
        TextView userName;

        public CommentViewHolder(View v) {
            this.body = (TextView)v.findViewById(R.id.article_info_comment_body);
            this.userIcon = (NetworkImageView)v.findViewById(R.id.article_info_comment_user_icon);
            this.userName = (TextView)v.findViewById(R.id.article_info_comment_user_name);
        }
    }

    private static class StockUserViewHolder {
        TextView userName;

        public StockUserViewHolder(View v) {
            this.userName = (TextView)v.findViewById(R.id.article_info_stock_user_name);
        }
    }
}
