package cat.institutmarina.insmarina.picasa.model;

import java.io.Serializable;

/**
 * Created by marcpacheco on 2/11/14.
 */
public class PicasaPhoto implements Serializable {
    private static final long serialVersionUID = 0;

    private String photoPublishedDate;
    private String photoUrlThumbnail;
    private String photoUrl;

    public PicasaPhoto(String photoPublishedDate, String photoUrlThumbnail, String photoUrl, String photoAlbumName) {
        this.photoPublishedDate = photoPublishedDate;
        this.photoUrlThumbnail = photoUrlThumbnail;
        this.photoUrl = photoUrl;
        this.photoAlbumName = photoAlbumName;
    }

    private String photoAlbumName;

    public String getPhotoPublishedDate() {
        return photoPublishedDate;
    }

    public void setPhotoPublishedDate(String photoPublishedDate) {
        this.photoPublishedDate = photoPublishedDate;
    }

    public String getPhotoUrlThumbnail() {
        return photoUrlThumbnail;
    }

    public void setPhotoUrlThumbnail(String photoUrlThumbnail) {
        this.photoUrlThumbnail = photoUrlThumbnail;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getPhotoAlbumName() {
        return photoAlbumName;
    }

    public void setPhotoAlbumName(String photoAlbumName) {
        this.photoAlbumName = photoAlbumName;
    }
}