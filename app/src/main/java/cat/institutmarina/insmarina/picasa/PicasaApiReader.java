package cat.institutmarina.insmarina.picasa;

import android.content.Context;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cat.institutmarina.insmarina.picasa.model.PicasaAlbum;
import cat.institutmarina.insmarina.picasa.model.PicasaPhoto;

/**
 * Created by marcpacheco on 2/11/14.
 */

// This API implementation need to be implement partial requests, disk cache and another features.

public class PicasaApiReader {
    private String userId;
    private Context context;

    //&max-results=10
    private final String PICASA_ALBUMS = "https://picasaweb.google.com/data/feed/api/user/%s?alt=json&thumbsize=250";
    private final String PICASA_ALBUM_PHOTOS = "https://picasaweb.google.com/data/feed/api/user/%s/albumid/%s?alt=json&imgmax=800";
    private final String PICASA_PHOTO = "";


    private final int cacheSize = 10 * 1024 * 1024;

    public PicasaApiReader(Context context, String userId) {
        this.context = context;
        this.userId = userId;
    }

    private JSONObject makeApiRequest(String url) throws IOException, JSONException {
        File cacheDir = (context.getExternalCacheDir() != null) ?
                context.getExternalCacheDir() : context.getCacheDir();


            OkHttpClient client = new OkHttpClient();

            // Disk Cache
            /*Cache cache = new Cache(cacheDir, cacheSize);
            client.setCache(cache);*/

            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = client.newCall(request).execute();

        return new JSONObject(response.body().string());
    }

    public void enableDiskCache(boolean enable) {
        if (enable) {

        }
    }

    public List<PicasaAlbum> getAlbums() throws IOException, JSONException {
        return parseListAlbums(makeApiRequest(String.format(PICASA_ALBUMS, userId)));
    }

    public List<PicasaPhoto> getPhotos(String albumId) throws IOException, JSONException {
        return parseListPhotos(makeApiRequest(String.format(PICASA_ALBUM_PHOTOS, userId, albumId)));
    }

    private List<PicasaPhoto> parseListPhotos(JSONObject json) throws JSONException {
        List<PicasaPhoto> photos = new ArrayList<PicasaPhoto>();

        JSONObject feed = json.getJSONObject("feed");
        JSONArray entries = feed.getJSONArray("entry");

        for (int i = 0; i < entries.length(); i++) {
            JSONObject photo = entries.getJSONObject(i);

            String photoUrl = photo.getJSONObject("media$group").getJSONArray("media$content").getJSONObject(0).getString("url");
            String photoUrlThumbnail = photo.getJSONObject("media$group").getJSONArray("media$thumbnail").getJSONObject(2).getString("url");
            String photoPublishedDate = photo.getJSONObject("published").getString("$t");
            String photoAlbumName = feed.getJSONObject("title").getString("$t");

            photos.add(new PicasaPhoto(photoPublishedDate, photoUrlThumbnail, photoUrl, photoAlbumName));
        }

        return photos;
    }

    private List<PicasaAlbum> parseListAlbums(JSONObject json) throws JSONException {
        List<PicasaAlbum> albums = new ArrayList<PicasaAlbum>();

        JSONObject feed = json.getJSONObject("feed");
        JSONArray entries = feed.getJSONArray("entry");

        for(int i = 0; i < entries.length(); i++) {
            JSONObject album = entries.getJSONObject(i);

            String published_date = album.getJSONObject("published").getString("$t");
            String title = album.getJSONObject("title").getString("$t");
            String album_id = album.getJSONObject("gphoto$id").getString("$t");//media$content
            String image_cover_url = album.getJSONObject("media$group").getJSONArray("media$thumbnail").getJSONObject(0).getString("url");
            int albumNumPhotos = album.getJSONObject("gphoto$numphotos").getInt("$t");

            albums.add(new PicasaAlbum(album_id, title, published_date, image_cover_url, albumNumPhotos));
        }

        return albums;
    }
}