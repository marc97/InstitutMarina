package cat.institutmarina.insmarina.picasa.model;

/**
 * Created by marcpacheco on 2/11/14.
 */
public class PicasaAlbum {
    private String albumTitle;
    private String albumPublishedDate;
    private String albumImageCoverUrl;
    private String albumId;
    private int albumNumPhotos;

    public PicasaAlbum(String albumId, String albumTitle, String albumPublishedDate,
        String albumImageCoverUrl, int albumNumPhotos) {

        this.albumId = albumId;
        this.albumTitle = albumTitle;
        this.albumPublishedDate = albumPublishedDate;
        this.albumImageCoverUrl = albumImageCoverUrl;
        this.albumNumPhotos = albumNumPhotos;

    }

    public String getAlbumTitle() {
        return albumTitle;
    }

    public void setAlbumTitle(String albumTitle) {
        this.albumTitle = albumTitle;
    }

    public String getAlbumPublishedDate() {
        return albumPublishedDate;
    }

    public void setAlbumPublishedDate(String albumPublishedDate) {
        this.albumPublishedDate = albumPublishedDate;
    }

    public String getAlbumImageCoverUrl() {
        return albumImageCoverUrl;
    }

    public void setAlbumImageCoverUrl(String albumImageCoverUrl) {
        this.albumImageCoverUrl = albumImageCoverUrl;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public int getAlbumNumPhotos() {
        return albumNumPhotos;
    }

    public void setAlbumNumPhotos(int albumNumPhotos) {
        this.albumNumPhotos = albumNumPhotos;
    }
}