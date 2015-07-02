package cat.institutmarina.insmarina.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

/**
 * Created by marcpacheco on 20/11/14.
 */
public class MultiSwipeRefreshLayout extends SwipeRefreshLayout {
    private CanChildScrollUpCallback mCanChildScrollUpCallback;

    private Drawable mForegroundDrawable;

    public MultiSwipeRefreshLayout(Context context) {
        this(context, null);
    }

    public MultiSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    public void setCanChildScrollUpCallback(CanChildScrollUpCallback canChildScrollUpCallback) {
        mCanChildScrollUpCallback = canChildScrollUpCallback;
    }

    public static interface CanChildScrollUpCallback {
        public boolean canSwipeRefreshChildScrollUp();
    }

    @Override
    public boolean canChildScrollUp() {
        if (mCanChildScrollUpCallback != null) {
            return mCanChildScrollUpCallback.canSwipeRefreshChildScrollUp();
        }
        return super.canChildScrollUp();
    }
}
