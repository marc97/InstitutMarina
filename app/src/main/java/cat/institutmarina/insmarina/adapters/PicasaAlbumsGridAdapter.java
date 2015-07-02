package cat.institutmarina.insmarina.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

import cat.institutmarina.insmarina.R;
import cat.institutmarina.insmarina.picasa.model.PicasaAlbum;

/**
 * Created by marcpacheco on 2/11/14.
 */
public class PicasaAlbumsGridAdapter extends BaseAdapter {
    private Context ctx;
    private LayoutInflater inflater;

    private List<PicasaAlbum> albums;

    private DisplayImageOptions options;
    private ImageLoader imageLoader;

    public PicasaAlbumsGridAdapter(Context ctx, List<PicasaAlbum> albums) {
        this.ctx = ctx;
        this.inflater = LayoutInflater.from(ctx);
        this.albums = albums;

        //UIL
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .resetViewBeforeLoading()
                .build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(ctx));
    }

    @Override
    public int getCount() {
        return albums.size();
    }

    @Override
    public Object getItem(int position) {
        return albums.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_grid_picasa_album, parent, false);

            holder = new ViewHolder();
            holder.imv_picasa_album = (ImageView) convertView.findViewById(R.id.imv_picasa_album);
            holder.txv_albums_album_name = (TextView) convertView.findViewById(R.id.txv_albums_album_name);
            holder.txv_albums_album_photos_number = (TextView) convertView.findViewById(R.id.txv_albums_album_photos_number);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        PicasaAlbum album = (PicasaAlbum)getItem(position);

        imageLoader.displayImage(album.getAlbumImageCoverUrl(), holder.imv_picasa_album, options);
        holder.txv_albums_album_name.setText(String.valueOf(album.getAlbumTitle()));
        holder.txv_albums_album_photos_number.setText(String.valueOf(album.getAlbumNumPhotos()) + " " + ctx.getString(R.string.photos));

        return convertView;
    }

    static class ViewHolder {
        public ImageView imv_picasa_album;
        public TextView txv_albums_album_name;
        public TextView txv_albums_album_photos_number;
    }
}