package com.pihrit.nextflick.views;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;

import com.squareup.picasso.Transformation;

/* Looked help from this: https://stackoverflow.com/a/26112408/649474 */
public class RoundedTransformation implements Transformation {

    private final int mRadius;
    private final String mKey;

    public RoundedTransformation(final int radius) {
        this.mRadius = radius;
        mKey = "rounded(radius=" + radius + ")";
    }

    @Override
    public Bitmap transform(final Bitmap source) {
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));

        Bitmap output = Bitmap.createBitmap(source.getWidth(), source.getHeight(), source.getConfig());
        Canvas canvas = new Canvas(output);
        RectF rect = new RectF(0.0f, 0.0f, source.getWidth(), source.getHeight());
        canvas.drawRoundRect(rect, mRadius, mRadius, paint);

        source.recycle();
        return output;
    }

    @Override
    public String key() {
        return mKey;
    }
}
