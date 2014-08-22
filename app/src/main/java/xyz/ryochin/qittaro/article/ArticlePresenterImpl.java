/**
 * PACKAGE NAME xyz.ryochin.qittaro.article
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/21
 */
package xyz.ryochin.qittaro.article;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.ShareCompat;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.ArrayList;
import java.util.List;

import xyz.ryochin.qittaro.R;
import xyz.ryochin.qittaro.models.ArticleCommentModel;
import xyz.ryochin.qittaro.models.ArticleDetailModel;
import xyz.ryochin.qittaro.models.ArticleTagModel;
import xyz.ryochin.qittaro.models.ArticleInfoModel;
import xyz.ryochin.qittaro.models.ArticleInfoModelType;
import xyz.ryochin.qittaro.requests.APIRequest;
import xyz.ryochin.qittaro.requests.ArticleRequest;
import xyz.ryochin.qittaro.requests.StockedRequest;

public class ArticlePresenterImpl implements ArticlePresenter {

    private static final String TAG = ArticlePresenterImpl.class.getSimpleName();
    private final ArticlePresenterImpl self = this;

    private ArticleView view;
    private Context context;
    private APIRequest request;
    private ArticleDetailModel model;
    private List<ArticleInfoModel> items;
    private ArticleInteractor interactor;

    public ArticlePresenterImpl(ArticleView view, Context context, String articleUUID, String token) {
        this.view = view;
        this.context = context;
        this.request = new ArticleRequest(articleUUID, token);
        this.interactor = new ArticleInteractorImpl();
        this.items = new ArrayList<ArticleInfoModel>();
    }

    @Override
    public void start() {
        view.showLoadingTitle();
        view.showFullLoadingView();
        view.hideActionBarItem();
        interactor.getArticle(request, new ArticleInteractor.Listener() {
            @Override
            public void onCompleted(ArticleDetailModel model) {
                self.model = model;
                view.showArticleTitle(model.getTitle());
                WebViewClient client = self.createWebViewClient();
                self.view.setWebView(client, model.getUrl());
                self.items = self.createInfoItems(model);
                self.view.setSideInfoItems(items);
                self.view.showActionBarItem(model.isStocked());
            }

            @Override
            public void onError(Exception e) {
            }
        });
    }

    private WebViewClient createWebViewClient() {
        return new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                self.view.hideFullLoadingView();
            }
        };
    }

    private List<ArticleInfoModel> createInfoItems(ArticleDetailModel model) {
        List<ArticleInfoModel> items = new ArrayList<ArticleInfoModel>();

        String userTitle = this.context.getResources().getString(R.string.article_info_user_title);
        ArticleInfoModel userTitleModel = new ArticleInfoModel(ArticleInfoModelType.Title, userTitle);
        items.add(userTitleModel);

        ArticleInfoModel userModel = new ArticleInfoModel(
                ArticleInfoModelType.User,
                model.getUser().getUrlName(),
                model.getUser().getProfileImageUrl()
        );
        items.add(userModel);

        String tagTitle = this.context.getResources().getString(R.string.article_info_tag_title, model.getTags().size());
        ArticleInfoModel tagTitleModel = new ArticleInfoModel(ArticleInfoModelType.Title, tagTitle);
        items.add(tagTitleModel);

        for(ArticleTagModel tagModel : model.getTags()) {
            ArticleInfoModel tagInfoModel = new ArticleInfoModel(
                    ArticleInfoModelType.Tag,
                    tagModel.getName(),
                    tagModel.getIconUrl(),
                    tagModel.getUrlName()
            );
            items.add(tagInfoModel);
        }

        String stockUserTitle = this.context.getResources().getString(R.string.article_info_stock_user_title, model.getStockCount());
        ArticleInfoModel stockUserTitleModel = new ArticleInfoModel(ArticleInfoModelType.Title, stockUserTitle);
        items.add(stockUserTitleModel);

        for (String stockUserName : model.getStockUsers()) {
            ArticleInfoModel stockUserModel = new ArticleInfoModel(
                    ArticleInfoModelType.StockUser,
                    stockUserName
            );
            items.add(stockUserModel);
        }

        String commentTitle = this.context.getResources().getString(R.string.article_info_comment_title, model.getCommentCount());
        ArticleInfoModel commentTitleModel = new ArticleInfoModel(
                ArticleInfoModelType.Title,
                commentTitle
        );
        items.add(commentTitleModel);

        for(ArticleCommentModel commentModel : model.getComments()) {
            ArticleInfoModel commentInfoModel = new ArticleInfoModel(
                    ArticleInfoModelType.Comment,
                    commentModel.getUser().getUrlName(),
                    commentModel.getUser().getProfileImageUrl(),
                    commentModel.getBody()
            );
            items.add(commentInfoModel);
        }

        return items;
    }

    @Override
    public void destroyView() {
        interactor.cancel(request);
    }

    @Override
    public void stock(String token) {
        view.showActionBarProgress();
        APIRequest request = new StockedRequest(token, model.getUuid());
        if (model.isStocked()) {
            interactor.deleteStocked(request, new ArticleInteractor.Listener() {
                @Override
                public void onCompleted(ArticleDetailModel model) {
                    self.model.setStocked(false);
                    self.view.hideActionBarProgress(self.model.isStocked());
                    self.view.showSuccessStockMessage(self.model.isStocked());
                }
                @Override
                public void onError(Exception e) {
                }
            });
        } else {
            interactor.putStocked(request, new ArticleInteractor.Listener() {
                @Override
                public void onCompleted(ArticleDetailModel model) {
                    self.model.setStocked(true);
                    self.view.hideActionBarProgress(self.model.isStocked());
                    self.view.showSuccessStockMessage(self.model.isStocked());
                }
                @Override
                public void onError(Exception e) {
                }
            });
        }
    }

    @Override
    public void share(Activity activity) {
        ShareCompat.IntentBuilder builder = ShareCompat.IntentBuilder.from(activity);
        builder.setChooserTitle(R.string.menu_article_detail_share_chooser_title);
        String articleTitle = model.getTitle();
        String articleText = articleTitle + " " + model.getUrl();
        builder.setSubject(articleTitle);
        builder.setText(articleText);
        builder.setType("text/plain");
        builder.startChooser();
    }

    @Override
    public void openBrowser() {
        String articleURL = model.getUrl();
        view.navigateToBrowser(articleURL);
    }

    @Override
    public void onItemClicked(int position) {
        ArticleInfoModel model = this.items.get(position);
        if (model.getType() == ArticleInfoModelType.User) {
            String urlName = model.getTitle();
            view.navigateToUserActivity(urlName);
        } else if (model.getType() == ArticleInfoModelType.Tag) {
            String urlName = model.getBody();
            String name = model.getBody();
            String iconURL = model.getIconURL();
            view.navigateToTagActivity(urlName, name, iconURL);
        } else if (model.getType() == ArticleInfoModelType.StockUser) {
            String urlName = model.getTitle();
            view.navigateToUserActivity(urlName);
        }
    }
}
