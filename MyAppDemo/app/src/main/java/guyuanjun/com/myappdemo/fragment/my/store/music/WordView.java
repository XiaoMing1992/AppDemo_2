package guyuanjun.com.myappdemo.fragment.my.store.music;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WordView extends android.support.v7.widget.AppCompatTextView {
    private List<String> mWordsList = new ArrayList();
    private Paint mLoseFocusPaint;
    private Paint mOnFocusePaint;
    private float mX = 0;
    private float mMiddleY = 0;
    private float mY = 0;
    private static final int DY = 50;
    private int mIndex = 0;

    public WordView(Context context) throws IOException {
        super(context);
        init();
    }

    public WordView(Context context, AttributeSet attrs) throws IOException {
        super(context, attrs);
        init();
    }

    public WordView(Context context, AttributeSet attrs, int defStyle)
            throws IOException {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.BLACK);
        Paint p = mLoseFocusPaint;
        p.setTextAlign(Paint.Align.CENTER);
        Paint p2 = mOnFocusePaint;
        p2.setTextAlign(Paint.Align.CENTER);

        canvas.drawText(mWordsList.get(mIndex), mX, mMiddleY, p2);

        int alphaValue = 25;
        float tempY = mMiddleY;
        for (int i = mIndex - 1; i >= 0; i--) {
            tempY -= DY;
            if (tempY < 0) {
                break;
            }
            p.setColor(Color.argb(255 - alphaValue, 245, 245, 245));
            canvas.drawText(mWordsList.get(i), mX, tempY, p);
            alphaValue += 25;
        }
        alphaValue = 25;
        tempY = mMiddleY;
        for (int i = mIndex + 1, len = mWordsList.size(); i < len; i++) {
            tempY += DY;
            if (tempY > mY) {
                break;
            }
            p.setColor(Color.argb(255 - alphaValue, 245, 245, 245));
            canvas.drawText(mWordsList.get(i), mX, tempY, p);
            alphaValue += 25;
        }
        mIndex++;
    }

    @Override
    protected void onSizeChanged(int w, int h, int ow, int oh) {
        super.onSizeChanged(w, h, ow, oh);

        mX = w * 0.5f;
        mY = h;
        mMiddleY = h * 0.3f;
    }

    @SuppressLint("SdCardPath")
    private void init() throws IOException {
        setFocusable(true);

        LrcHandle lrcHandler = new LrcHandle();
        lrcHandler.readLRC("/storage/extSdCard/music/2728244.lrc");
        mWordsList = lrcHandler.getWords();

        mLoseFocusPaint = new Paint();
        mLoseFocusPaint.setAntiAlias(true);
        mLoseFocusPaint.setTextSize(22);
        mLoseFocusPaint.setColor(Color.WHITE);
        mLoseFocusPaint.setTypeface(Typeface.SERIF);

        mOnFocusePaint = new Paint();
        mOnFocusePaint.setAntiAlias(true);
        mOnFocusePaint.setColor(Color.YELLOW);
        mOnFocusePaint.setTextSize(30);
        mOnFocusePaint.setTypeface(Typeface.SANS_SERIF);
    }
}