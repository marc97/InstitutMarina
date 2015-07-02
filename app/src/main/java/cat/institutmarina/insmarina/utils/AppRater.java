package cat.institutmarina.insmarina.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

public class AppRater {
	private Context ctx;
	private String title, message, positiveBtn, neutralBtn, negativeBtn;
	private int minLaunchesUntilPrompt, minDaysUntilPrompt, minLaunchesUntilNextPrompt, minDaysUntilNextPrompt;
	
	public static final String APP_RATER_SHARED_PREFERENCES = "APP_RATER_SHARED_PREFERENCES";
	private static final String DATE_FIRST_LAUNCH = "FIRST_LAUNCH";
	private static final String LAUNCH_COUNT = "LAUNCH_COUNT";
	private static final String DONT_SHOW_AGAIN = "DONT_SHOW_AGAIN";
	private static final String SHOW_LATER = "SHOW_LATER";
	
	private SharedPreferences prefs;
	private SharedPreferences.Editor editor;
	
    private AppRater(Builder builder) {
    	this.ctx = builder.ctx;
    	this.title = builder.title;
    	this.message = builder.message;
    	this.positiveBtn = builder.positiveBtn;
    	this.neutralBtn = builder.neutralBtn;
    	this.negativeBtn = builder.negativeBtn;
    	this.minLaunchesUntilPrompt = builder.minLaunchesUntilPrompt;
    	this.minDaysUntilPrompt = builder.minDaysUntilPrompt;
    	this.minLaunchesUntilNextPrompt = builder.minLaunchesUntilNextPrompt;
    	this.minDaysUntilNextPrompt = builder.minDaysUntilNextPrompt;
    	
    	prefs = ctx.getSharedPreferences(APP_RATER_SHARED_PREFERENCES, Context.MODE_PRIVATE);
    	editor = prefs.edit();
	}
    
	public static class Builder {
		private Context ctx;
		private String title, message, positiveBtn, neutralBtn, negativeBtn;
		private int minLaunchesUntilPrompt, minDaysUntilPrompt, minLaunchesUntilNextPrompt, minDaysUntilNextPrompt;
		
		public Builder(Context ctx) {
			this.ctx = ctx;
		}
		
		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}
		public Builder setMessage(String message) {
			this.message = message;
			return this;
		}
		public Builder setPositiveBtn(String positiveBtn) {
			this.positiveBtn = positiveBtn;
			return this;
		}
		public Builder setNegativeBtn(String negativeBtn) {
			this.negativeBtn = negativeBtn;
			return this;
		}
		public Builder setNeutralBtn(String neutralBtn) {
			this.neutralBtn = neutralBtn;
			return this;
		}
		public Builder setPromptMins(int minLaunchesUntilPrompt, int minDaysUntilPrompt, int minLaunchesUntilNextPrompt, int minDaysUntilNextPrompt) {
			this.minLaunchesUntilPrompt = minLaunchesUntilPrompt;
			this.minDaysUntilPrompt = minDaysUntilPrompt;
			this.minLaunchesUntilNextPrompt = minLaunchesUntilNextPrompt;
			this.minDaysUntilNextPrompt = minDaysUntilNextPrompt;
			return this;
		}
		public AppRater build() {
			return new AppRater(this);
		}
	}
	
    public static void startActivityForRateApp(Context ctx) {
		ctx.startActivity(new Intent(Intent.ACTION_VIEW, Uri
				.parse("market://details?id="
						+ ctx.getPackageName())));
	}
    
    public void clearStatus() {
    	editor.clear();
    	editor.commit();
    }
	
	public void run() {
       
		// If don't show again don't show.
		if (prefs.getBoolean(DONT_SHOW_AGAIN, false)) { return ; }
        
        // Increment launch counter.
        long launch_count = prefs.getLong(LAUNCH_COUNT, 0) + 1;
        editor.putLong(LAUNCH_COUNT, launch_count);

        // Get date of first launch.
        Long dateFirstLaunch = prefs.getLong(DATE_FIRST_LAUNCH, 0);
        if (dateFirstLaunch == 0) {
            dateFirstLaunch = System.currentTimeMillis();
            editor.putLong(DATE_FIRST_LAUNCH, dateFirstLaunch);
        }
        
        editor.commit();
        
        // Wait at least n days before show.
        if (prefs.getBoolean(SHOW_LATER, false)) {
        	if (launch_count >= minLaunchesUntilNextPrompt) {
                if (System.currentTimeMillis() >= dateFirstLaunch + 
                        (minDaysUntilNextPrompt * 24 * 60 * 60 * 1000)) {
                    showRateAppDialog();
                }
            }
        	
        } else {
        	if (launch_count >= minLaunchesUntilPrompt) {
                if (System.currentTimeMillis() >= dateFirstLaunch + 
                        (minDaysUntilPrompt * 24 * 60 * 60 * 1000)) {
                    showRateAppDialog();
                    
                 // Clean variables.
                editor.putLong(LAUNCH_COUNT, 0);
                editor.putLong(DATE_FIRST_LAUNCH, System.currentTimeMillis());
                }
            }
        }
	}
	
	public void showRateAppDialog() {
		
		// Reestore show later initial status.
		if (prefs.getBoolean(SHOW_LATER, false)) {
			editor.putBoolean(SHOW_LATER, false);
		}
		
		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(ctx);
		
		alertBuilder.setTitle(title);
		alertBuilder.setMessage(message);
		alertBuilder.setPositiveButton(positiveBtn, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				startActivityForRateApp(ctx);
				editor.putBoolean(DONT_SHOW_AGAIN, true);
				editor.commit();
				dialog.cancel();
			}
		});
		alertBuilder.setNeutralButton(neutralBtn, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				editor.putBoolean(SHOW_LATER, true);
				editor.commit();
				dialog.cancel();
			}
		});
		alertBuilder.setNegativeButton(negativeBtn, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				editor.putBoolean(DONT_SHOW_AGAIN, true);
				editor.commit();
				dialog.cancel();
			}
		});
		
		alertBuilder.show();
	}
}