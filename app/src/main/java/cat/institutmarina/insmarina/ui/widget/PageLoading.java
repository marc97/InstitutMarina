package cat.institutmarina.insmarina.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import cat.institutmarina.insmarina.R;

/**
 * Created by marcpacheco on 15/11/14.
 */
public class PageLoading extends FrameLayout {
    private Context context;
    private static int FADE_DURATION = 200;
    private boolean isShowingLoading = false;

    // Styleable
    private int pageLoadinglayout;
    private int errorlayout;

    private View pageLoading;
    private View viewToShow;
    private View errorView;

    private ProgressBar pgbPageLoading;

    public PageLoading(Context context) {
        super(context);
    }

    public PageLoading(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context, attrs);
    }

    public PageLoading(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context, attrs);
    }

    private void initViews(Context context, AttributeSet attrs) {
        this.context = context;

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.PageLoading, 0, 0);

        try {
            pageLoadinglayout = a.getResourceId(R.styleable.PageLoading_page_loading_layout, 0);
            errorlayout = a.getResourceId(R.styleable.PageLoading_error_layout, 0);

        } finally {
            a.recycle();
        }

        if (pageLoadinglayout == 0) {
            throw new RuntimeException("You must set the attribute pageLoadingLayout");
        }

        LayoutInflater inflater = LayoutInflater.from(context);
        pageLoading = inflater.inflate(pageLoadinglayout, null);

        if (errorlayout != 0) {
            errorView = inflater.inflate(errorlayout, null);
            errorView.setVisibility(View.INVISIBLE);
            ((ViewGroup)pageLoading).addView(errorView);
        }

        //Add PageLoading View to their parent
        this.addView(pageLoading);

        //Hide PageLoading initially
        pageLoading.setVisibility(View.GONE);

        pgbPageLoading = (ProgressBar) pageLoading.findViewById(R.id.pgb_page_loading);
    }

    public void setViewToShow(View viewToShow) {
        this.viewToShow = viewToShow;
    }

    public void setErrorView(View errorView) {
        this.errorView = errorView;
    }

    public View getErrorView() {
        return errorView;
    }


    public void hideLoading() {
        /*if (!isShowingLoading()) {
            pageLoading.setVisibility(View.GONE);
        } else {
            fadeInOutViews(viewToShow, pageLoading, FADE_DURATION);
            isShowingLoading = false;
        }*/

        fadeInOutViews(viewToShow, pageLoading, FADE_DURATION);
        isShowingLoading = false;
    }

    public void showLoading() {
        /*if (!isShowingLoading()) {
            pageLoading.setVisibility(View.VISIBLE);
            pgbPageLoading.setVisibility(View.VISIBLE);
            hideError();
        } else {
            fadeInOutViews(pageLoading, viewToShow, FADE_DURATION);
            isShowingLoading = true;
        }*/
        fadeInOutViews(pageLoading, viewToShow, FADE_DURATION);
        //pageLoading.setVisibility(View.VISIBLE);
        pgbPageLoading.setVisibility(View.VISIBLE);
        isShowingLoading = true;
        hideError();
    }

    public void showError() {
        if (errorView != null)
        pageLoading.setVisibility(View.VISIBLE);
        fadeInOutViews(errorView, pgbPageLoading, FADE_DURATION);

        //pgbPageLoading.setVisibility(View.GONE)
        //errorView.setVisibility(View.VISIBLE);
    }

    public void hideError() {
        if (errorView != null) errorView.setVisibility(View.INVISIBLE);
    }

    public boolean isShowingLoading()  {
        return isShowingLoading;
    }

    public void setFadeDuration(int time) {
        FADE_DURATION = time;
    }

    public int getFadeDuration() {
        return FADE_DURATION;
    }

    private void fadeInOutViews(final View in, final View out, int duration) {
        // View in.
        AlphaAnimation aAnimationIn = new AlphaAnimation(0f, 1f);
        aAnimationIn.setDuration(duration);
        in.setVisibility(View.VISIBLE);
        in.startAnimation(aAnimationIn);
        aAnimationIn.setAnimationListener(null);

        // View out.
        AlphaAnimation aAnimationOut = new AlphaAnimation(1f, 0f);
        aAnimationOut.setDuration(duration);
        out.startAnimation(aAnimationOut);
        aAnimationOut.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationRepeat(Animation animation) { }

            @Override
            public void onAnimationEnd(Animation animation) {
                out.setVisibility(View.GONE);
            }
        });
    }
}