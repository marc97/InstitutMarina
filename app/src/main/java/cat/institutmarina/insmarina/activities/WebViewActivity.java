package cat.institutmarina.insmarina.activities;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import cat.institutmarina.insmarina.R;
import cat.institutmarina.insmarina.ui.ObservableWebView;

/**
 * Created by marcpacheco on 7/11/14.
 */
public class WebViewActivity extends BaseActivity {

    public static final String EXTRAS_URL = "extras_url";
    public static final String EXTRAS_TITLE = "extras_title";

    private String url;
    private ObservableWebView wb;
    private ProgressBar pgb;
    private ActionBarActivity ctx = this;
    private View container_toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        wb = (ObservableWebView) findViewById(R.id.wb_activity_webview);
        pgb = (ProgressBar) findViewById(R.id.pgb_webview);
        container_toolbar = findViewById(R.id.container_webview_toolbar);

        // Extras
        url = getIntent().getStringExtra(EXTRAS_URL);
        String title = getIntent().getStringExtra(EXTRAS_TITLE);

        final ActionBar ab = getSupportActionBar();
        ab.setTitle(title);

        wb.getSettings().setJavaScriptEnabled(true);
        wb.getSettings().setBuiltInZoomControls(true);
        wb.getSettings().setDisplayZoomControls(false);
        wb.getSettings().setUseWideViewPort(true);
        wb.getSettings().setLoadWithOverviewMode(true);

        wb.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, final int progress) {

                //Smoothly progress
                ObjectAnimator animation = ObjectAnimator.ofInt(pgb, "progress", progress);
                animation.setDuration(200);
                animation.setInterpolator(new DecelerateInterpolator());
                animation.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (progress == 100)
                            pgb.setProgress(0);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                animation.start();
            }
        });

        wb.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });

        wb.loadUrl(url);

        /*** QUICK RETURN TOOLBAR ***/
       /*new QuickReturnToolbar.Builder()
                .setObservableWebView(wb)
                .setToolbarView(container_toolbar)
                .build();*/
    }


    /*** QUICK RETURN TOOLBAR (NEED IMPROVEMENT!!!) **/

     /*
    boolean showing = false;
    int lastY = 0;

    @Override
    public void onScrollChanged(int x, int y, int oldx, int oldy) {
        int yPosition = 0;

        if (y - lastY > 0) {
            showing = false;
        } else {
            showing = true;
        }

        int factor = Math.abs(lastY - y);
        int bottom = container_toolbar.getBottom();

        if (showing) {
            yPosition = (int) Math.min((container_toolbar.getTranslationY() + factor) , 0);
        } else {
            yPosition = (int) Math.max((container_toolbar.getTranslationY() - factor), -bottom);
        }

        lastY = y;

       // System.out.println("cheight: " + wb.getContentHeight() + ", height: "+ wb.getHeight());

       // y = (y > 0) ?  Math.min(view.getHeight(), y) : Math.max(view.getHeight(), y);
        container_toolbar.setTranslationY(yPosition);
    }*/

    private void openUrlInBrowser(String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (wb.canGoBack()) {
            // Go back
            wb.goBack();
        } else {
            finish();
        }
    }

    /*********** Menu ActionBar ***********/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.webview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_open_url_browser:
                openUrlInBrowser(wb.getUrl());
                break;
            case R.id.action_reload_page_browser:
                wb.reload();
                break;
            case R.id.action_go_back_page_browser:
                if (wb.canGoBack()) wb.goBack();
                break;
            case R.id.action_go_forward_page_browser:
                if (wb.canGoForward()) wb.goForward();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}