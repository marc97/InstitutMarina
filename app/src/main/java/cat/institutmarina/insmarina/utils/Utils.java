package cat.institutmarina.insmarina.utils;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.format.Time;
import android.util.DisplayMetrics;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.List;

import cat.institutmarina.insmarina.activities.WebViewActivity;

/**
 * Created by marcpacheco on 4/1/15.
 */
public class Utils {
    public static void startWebViewActivity(Context ctx, String title, String url) {
        Intent i = new Intent(ctx, WebViewActivity.class);
        i.putExtra(WebViewActivity.EXTRAS_URL, url);
        i.putExtra(WebViewActivity.EXTRAS_TITLE, title);
        ctx.startActivity(i);
    }

    public static Intent getOpenFacebookIntent(Context context, String id, String name) {
        try {
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/" + id));
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/" + name));
        }
    }

    public static String rfc3339toHumanReadable(String date) {
        Time time = new Time();
        time.parse3339(date);
        return time.monthDay + "/" + (time.month + 1) + "/" + time.year;
    }

    public static String join(List<String> list, String joiner) {
        String result = "";
        if (list != null) {
            for (String item : list) {
                result += item + joiner + " ";
            }
            result = result.substring(0, result.length() - 2);
        }
        return result;
    }

    // Obtain a summary of the content.
    public static String getSummaryFromHTML(String html, int maxSummaryCharacters) {
        Document doc = Jsoup.parse(html);
        String summary = doc.body().text().replaceAll("[\n\r]", "").replaceAll("\\s+", " ");
        if (summary.length() > maxSummaryCharacters) {
            summary = summary.substring(0, maxSummaryCharacters);
        }
        return summary;
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(Context context, float dp) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(Context context, float px) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;
    }

    //Improooooove method
    public static Bitmap scaleImage(Context ctx, int resid, int dp) {
        dp = (int) convertDpToPixel(ctx, dp);

        Bitmap b = null;
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;

        BitmapFactory.decodeResource(ctx.getResources(), resid, o);

        float sc = 0.0f;
        int scale = 1;
        if (o.outHeight > o.outWidth) {
            sc = o.outHeight / dp;
            scale = Math.round(sc);
        } else {
            sc = o.outWidth / dp;
            scale = Math.round(sc);
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        b = BitmapFactory.decodeResource(ctx.getResources(), resid, o2);
        return b;
    }
}