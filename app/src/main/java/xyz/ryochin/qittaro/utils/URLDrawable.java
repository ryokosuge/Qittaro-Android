/**
 * PACKAGE NAME xyz.ryochin.qittaro.utils
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/18
 */
package xyz.ryochin.qittaro.utils;

import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

@SuppressWarnings("ALL")
public class URLDrawable extends BitmapDrawable {
    private static final String TAG = URLDrawable.class.getSimpleName();
    private final URLDrawable self = this;

    private Drawable drawable;

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    @Override
    public void draw(Canvas canvas) {
        Log.e(TAG, "draw(Canvas canvas)");
        if (drawable != null) {
            drawable.draw(canvas);
        }
    }
}
