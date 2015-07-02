package cat.institutmarina.insmarina.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by marcpacheco on 23/11/14.
 */
public class ObservableWebView extends WebView {
    private Callbacks mCallbacks;

    public ObservableWebView(Context context) {
        super(context);
    }

    public ObservableWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ObservableWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mCallbacks != null) {
            mCallbacks.onScrollChanged(t);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mCallbacks != null) {
            mCallbacks.onMotionEvent(ev);
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public int computeVerticalScrollRange() {
        return super.computeVerticalScrollRange();
    }

    public void setCallbacks(Callbacks listener) {
        mCallbacks = listener;

        setWebViewClient(new WebViewClient() {
            @Override
            public void onScaleChanged(WebView view, float oldScale, float newScale) {
                super.onScaleChanged(view, oldScale, newScale);
                if (mCallbacks != null) {
                    mCallbacks.onScaleChanged();
                }
            }
        });
    }

    public static interface Callbacks {
        public void onScrollChanged(int scrollY);
        public void onScaleChanged();
        public void onMotionEvent(MotionEvent motionEvent);
    }
}