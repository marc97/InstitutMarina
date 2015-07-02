package cat.institutmarina.insmarina.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

import cat.institutmarina.insmarina.R;
import cat.institutmarina.insmarina.picasa.model.PicasaPhoto;

/**
 * Created by marcpacheco on 4/11/14.
 */
public class PicasaPhotosGridAdapter extends BaseAdapter {
    private Context ctx;
    private LayoutInflater inflater;

    private DisplayImageOptions options;
    private ImageLoader imageLoader;

    private List<PicasaPhoto> photos;

    public PicasaPhotosGridAdapter(Context ctx, List<PicasaPhoto> photos) {
        this.ctx = ctx;
        this.inflater = LayoutInflater.from(ctx);

        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .resetViewBeforeLoading()
                .build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(ctx));

        this.photos = photos;
    }

    public List<PicasaPhoto> getPhotos() {
        return photos;
    }

    @Override
    public int getCount() {
        return photos.size();
    }

    @Override
    public Object getItem(int position) {
        return photos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_grid_picasa_photo, parent, false);

            holder = new ViewHolder();
            holder.imv_picasa_album_photo = (ImageView) convertView.findViewById(R.id.imv_picasa_album_photo);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        PicasaPhoto photo = (PicasaPhoto) getItem(position);
        imageLoader.displayImage(photo.getPhotoUrlThumbnail(), holder.imv_picasa_album_photo, options);

        return convertView;
    }

    static class ViewHolder {
        ImageView imv_picasa_album_photo;
    }
}