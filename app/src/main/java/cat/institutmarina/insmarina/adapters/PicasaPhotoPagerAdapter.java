package cat.institutmarina.insmarina.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.List;

import cat.institutmarina.insmarina.R;
import cat.institutmarina.insmarina.activities.PicasaPhotoActivity;
import cat.institutmarina.insmarina.picasa.model.PicasaPhoto;
import cat.institutmarina.insmarina.ui.widget.PageLoading;
import cat.institutmarina.insmarina.uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by marcpacheco on 27/11/14.
 */
public class PicasaPhotoPagerAdapter extends PagerAdapter {
    private Context ctx;
    private LayoutInflater inflater;
    private List<PicasaPhoto> photos;

    private DisplayImageOptions options;
    private ImageLoader imageLoader;

    private PhotoViewAttacher attacher;
    private int currentPos = -1;


    public PicasaPhotoPagerAdapter(Context ctx, List<PicasaPhoto> photos) {
        this.ctx = ctx;
        this.inflater = LayoutInflater.from(ctx);
        this.photos = photos;

        //UIL
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .cacheOnDisk(true)
                .build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(ctx));
    }

    @Override
    public int getCount() {
        return photos.size();
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);

        if (position != currentPos) {
            currentPos = position;
            if (attacher != null) attacher.cleanup();

            View view = (View) object;
            ImageView imageView = (ImageView) view.findViewById(R.id.imv_page_picasa_photo);
            attacher = new PhotoViewAttacher(imageView);
            attacher.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
                @Override
                public void onViewTap(View view, float x, float y) {
                    ((PicasaPhotoActivity) ctx).toggleActionBarVisibility();
                }
            });
        }
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = inflater.inflate(R.layout.page_picasa_photo, container, false);

        final ImageView imv = (ImageView) view.findViewById(R.id.imv_page_picasa_photo);

        final PageLoading mPageLoading = (PageLoading) view.findViewById(R.id.page_loading);
        mPageLoading.setViewToShow(imv);
        mPageLoading.showLoading();

        imageLoader.displayImage(photos.get(position).getPhotoUrl(), imv, options,
                new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                mPageLoading.hideLoading();
                if (attacher != null) {
                    attacher.update();
                }

                // Hide ActionBar when zooming
                /*final int initialBottom = (int) attacher.getDisplayRect().bottom;
                attacher.setOnMatrixChangeListener(new PhotoViewAttacher.OnMatrixChangedListener() {
                    @Override
                    public void onMatrixChanged(RectF rect) {
                        if (rect.bottom > initialBottom) {
                            if (((PicasaPhotoActivity) ctx).isActionBarVisible()) {
                                ((PicasaPhotoActivity) ctx).toggleActionBarVisibility();
                            }
                        } else {
                            if (!((PicasaPhotoActivity) ctx).isActionBarVisible()
                                    && initialBottom == rect.bottom) {
                                ((PicasaPhotoActivity) ctx).toggleActionBarVisibility();
                            }
                        }
                    }
                });*/
            }
        });

        // Add an identifier to after be able to retrieve the view.
        view.setTag(position);

        container.addView(view);
        return view;

    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Recycle bitmap to prevent OutOfMemory Error
        View view = (View) object;
        try {
            ImageView imageView = (ImageView) view.findViewById(R.id.imv_page_picasa_photo);
            if (imageView != null) {
                Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                if (!bitmap.isRecycled()) {
                    bitmap.recycle();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }
}