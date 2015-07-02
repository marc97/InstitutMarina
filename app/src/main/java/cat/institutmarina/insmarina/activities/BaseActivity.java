package cat.institutmarina.insmarina.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;

import cat.institutmarina.insmarina.R;

/**
 * Created by marcpacheco on 2/11/14.
 */
public abstract class BaseActivity extends ActionBarActivity {
    private Toolbar mActionBarToolbar;
    private boolean actionBarVisible = true;
    private View mNoConnectivity;

    public static final int HEADER_HIDE_ANIM_DURATION = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        getActionBarToolbar();

        // Display back arrow.
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        // Arrow click listener
        getActionBarToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenForNavigationClick();
            }
        });
    }

    protected void listenForNavigationClick() {
        // Default finish the activity.
        finish();
    }

    protected Toolbar getActionBarToolbar() {
        if (mActionBarToolbar == null) {
            mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
            if (mActionBarToolbar != null) {
                setSupportActionBar(mActionBarToolbar);
            }
        }
        return mActionBarToolbar;
    }

    public void toggleActionBarVisibility() {
        if (actionBarVisible) {
            mActionBarToolbar.animate()
                    .translationY(-mActionBarToolbar.getBottom())
                    .alpha(0)
                    .setDuration(HEADER_HIDE_ANIM_DURATION)
                    .setInterpolator(new DecelerateInterpolator());
            actionBarVisible = false;

        } else {
            mActionBarToolbar.animate()
                    .translationY(0)
                    .alpha(1)
                    .setDuration(HEADER_HIDE_ANIM_DURATION)
                    .setInterpolator(new DecelerateInterpolator());
            actionBarVisible = true;
        }
    }

    public boolean isActionBarVisible() {
        return actionBarVisible;
    }

    protected void hideSystemUI() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    protected void showSystemUI() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
    }

    // Not working properly
    private boolean systemUIVisibility = true;
    protected void toggleSystemUIVisibility() {
        if (systemUIVisibility) {
            hideSystemUI();
            systemUIVisibility = false;
        } else {
            showSystemUI();
            systemUIVisibility = true;
        }
    }
}