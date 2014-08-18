/**
 * PACKAGE NAME xyz.ryochin.qittaro.utils
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/18
 */

package xyz.ryochin.qittaro.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;

public class URLImageParser implements Html.ImageGetter {

    private static final String TAG = URLImageParser.class.getSimpleName();
    private final URLImageParser self = this;

    private Context context;
    private TextView container;

    public URLImageParser(TextView v, Context context) {
        this.container = v;
        this.context = context;
    }

    @Override
    public Drawable getDrawable(String source) {

        URLDrawable urlDrawable = new URLDrawable();

        ImageGetterAsync imageGetterAsync = new ImageGetterAsync(urlDrawable, this.context);
        imageGetterAsync.execute(source);

        return urlDrawable;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    private class ImageGetterAsync extends AsyncTask<String, Void, Drawable> {

        URLDrawable drawable;
        Context context;

        public ImageGetterAsync(URLDrawable drawable, Context context) {
            this.drawable = drawable;
            this.context = context;
        }

        @Override
        protected Drawable doInBackground(String... params) {
            String source = params[0];
            return fetchDrawable(source);
        }

        @Override
        protected void onPostExecute(Drawable drawable) {
            int width = drawable.getIntrinsicWidth();
            int height = drawable.getIntrinsicHeight();
            this.drawable.setBounds(0, 0, width, height);
            this.drawable.setDrawable(drawable);
            URLImageParser.this.container.invalidate();
        }

        private Drawable fetchDrawable(String urlStr) {
            try {
                InputStream inputStream = this.fetch(urlStr);
                Drawable drawable = Drawable.createFromStream(inputStream, "src");
                int width = drawable.getIntrinsicWidth();
                int height = drawable.getIntrinsicHeight();
                drawable.setBounds(0, 0, width, height);
                return drawable;
            } catch (IOException e) {
                Log.e(TAG, "IOException", e);
                return null;
            }
        }

        private InputStream fetch(String urlStr) throws IOException {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet request = new HttpGet(urlStr);
            HttpResponse response = httpClient.execute(request);
            return response.getEntity().getContent();
        }
    }
}
