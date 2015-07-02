package cat.institutmarina.insmarina.ui;

import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * Created by marcpacheco on 23/11/14.
 */
public class QuickReturnToolbar {
    private View toolbarView = null;
    private ObservableWebView observableWebView = null;
    private int ANIM_DURATION = 100;

    private boolean showing = false;
    private int lastY = 0;
    private boolean scaleChanged = false;

    private QuickReturnToolbar(Builder builder) {
        this.toolbarView = builder.toolbarView;
        this.observableWebView = builder.observableWebView;
        //this.ANIM_DURATION = (builder.duration != null) ? builder.duration : ANIM_DURATION;
        init();
    }

    public static class Builder {
        private View toolbarView;
        private ObservableWebView observableWebView;
        private int duration;

        public Builder setToolbarView(View toolbarView) {
            this.toolbarView = toolbarView;
            return this;
        }
        public Builder setObservableWebView(ObservableWebView observableWebView) {
            this.observableWebView = observableWebView;
            return this;
        }
        public Builder setAnimateDuration(int duration) {
            this.duration = duration;
            return this;
        }
        public QuickReturnToolbar build() {
            return new QuickReturnToolbar(this);
        }
    }

    private void init() {
        if (observableWebView != null) {

         /*   toolbarView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    observableWebView.setTranslationY(toolbarView.getBottom());
                }
            });*/
            observableWebView.getLayoutParams().height = observableWebView.getLayoutParams().height;
            observableWebView.requestLayout();


            observableWebView.setCallbacks(new ObservableWebView.Callbacks() {
                @Override
                public void onScrollChanged(int scrollY) {
                    if (!scaleChanged) {
                        translateToolbarView(scrollY);
                        /*if (observableWebView.getTranslationY() >= 0)
                            observableWebView.setTranslationY( observableWebView.getTranslationY() - factor );*/
                    }
                }

                @Override
                public void onScaleChanged() {

                }

                @Override
                public void onMotionEvent(MotionEvent ev) {
                    switch (ev.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            break;
                        case MotionEvent.ACTION_MOVE:
                            if (ev.getPointerCount() > 1) {
                                scaleChanged = true;
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL:
                            //autoHideShowToolbarView();
                            scaleChanged = false;
                            break;
                    }
                }

            });
        }
    }

    int factor = 0;
    private void translateToolbarView(int y) {
        int yPosition = 0;

        if (y - lastY > 0) {
            showing = false;
        } else {
            showing = true;
        }

        factor = Math.abs(lastY - y);
        int bottom = toolbarView.getBottom();

        if (showing) {
            yPosition = (int) Math.min((toolbarView.getTranslationY() + factor) , 0);
        } else {
            yPosition = (int) Math.max((toolbarView.getTranslationY() - factor), -bottom);
        }

        lastY = y;
        toolbarView.setTranslationY(yPosition);
    }

    private void autoHideShowToolbarView() {
        int bottom = toolbarView.getBottom();
        int translationY = Math.abs((int) toolbarView.getTranslationY());
        if ((bottom / 2) > translationY) {
            showToolbarViewAnimated();
        } else {
            hideToolbarViewAnimated();
        }
    }

    private void showToolbarViewAnimated() {
        toolbarView.animate()
                .setDuration(ANIM_DURATION)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .translationY(0)
                .start();
    }

    private void hideToolbarViewAnimated() {
        toolbarView.animate()
                .setDuration(ANIM_DURATION)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .translationY(-toolbarView.getBottom())
                .start();
    }
}