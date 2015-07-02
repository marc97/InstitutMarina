package cat.institutmarina.insmarina.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import cat.institutmarina.insmarina.R;
import cat.institutmarina.insmarina.adapters.PicasaPhotoPagerAdapter;
import cat.institutmarina.insmarina.picasa.model.PicasaPhoto;

/**
 * Created by marcpacheco on 11/11/14.
 */
public class PicasaPhotoActivity extends BaseActivity {
    public static String EXTRA_PHOTO_TITLE = "extra_photo_title";
    public static String EXTRA_PHOTO_ADAPTER_POSITION = "extra_photo_adapter_position";
    public static String EXTRA_PHOTO_LIST_PICASA_PHOTOS = "extra_photo_list_picasa_photo";

    private ViewPager viewPager;
    private ShareActionProvider mShareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picasa_photo_detail);
        hideSystemUI();

        getSupportActionBar().setTitle(getIntent().getExtras().getString(EXTRA_PHOTO_TITLE));

        List<PicasaPhoto> photos = (List<PicasaPhoto>) getIntent().getExtras().getSerializable(EXTRA_PHOTO_LIST_PICASA_PHOTOS);
        int position = getIntent().getExtras().getInt(EXTRA_PHOTO_ADAPTER_POSITION);

        viewPager = (ViewPager) findViewById(R.id.vp_picasa_photo);
        viewPager.setAdapter(new PicasaPhotoPagerAdapter(this, photos));
        viewPager.setPageTransformer(true, new DepthPageTransformer());
        viewPager.setCurrentItem(position);
    }

    public class DepthPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.75f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 0) { // [-1,0]
                // Use the default slide transition when moving to the left page
                view.setAlpha(1);
                view.setTranslationX(0);
                view.setScaleX(1);
                view.setScaleY(1);

            } else if (position <= 1) { // (0,1]
                // Fade the page out.
                view.setAlpha(1 - position);

                // Counteract the default slide transition
                view.setTranslationX(pageWidth * -position);

                // Scale the page down (between MIN_SCALE and 1)
                float scaleFactor = MIN_SCALE
                        + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.photo, menu);

        MenuItem shareItem = menu.findItem(R.id.action_share);
        mShareActionProvider = (ShareActionProvider)
                MenuItemCompat.getActionProvider(shareItem);
        mShareActionProvider.setShareIntent(getDefaultIntent());

        return true;
    }

    private Intent getDefaultIntent() {
        View view = (View) viewPager.findViewWithTag(viewPager.getCurrentItem());
        ImageView imv = (ImageView) view.findViewById(R.id.imv_page_picasa_photo);

        Intent intent = new Intent(Intent.ACTION_SEND);
        Uri bmpUri = getLocalBitmapUri(imv);
        if (bmpUri != null) {
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_STREAM, bmpUri);
        }
        return intent;
    }

    public Uri getLocalBitmapUri(ImageView imageView) {
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable) {
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }

        Uri bmpUri = null;
        try {
            File file =  new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }
}