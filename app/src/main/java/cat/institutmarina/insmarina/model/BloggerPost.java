package cat.institutmarina.insmarina.model;

import com.google.api.services.blogger.model.Post;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.Serializable;

import cat.institutmarina.insmarina.utils.Utils;

/**
 * Created by marcpacheco on 3/1/15.
 */
public class BloggerPost implements Serializable {
    private Post post;
    private String summary;
    private String imageThumbnail;

    public int maxSummaryCharacters = 140;

    public String getSummary() {
        return summary;
    }

    public String getImageThumbnail() {
        return imageThumbnail;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;

        // Obtain the first image from the html.
        try {
            Document document = Jsoup.parse(post.getContent());
            Element image = document.select("img").first();

            // Hacky.
            this.imageThumbnail = image.attr("src").replace("s1600", "s900");
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.summary = Utils.getSummaryFromHTML(post.getContent(), maxSummaryCharacters);
    }
}