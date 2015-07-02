package cat.institutmarina.insmarina.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by marcpacheco on 8/6/15.
 */
public class WebMenuItem implements Serializable {
    private static final long serialVersionUID = 0;

    private String title;
    private String url;
    private List<WebMenuItem> items;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<WebMenuItem> getItems() {
        return items;
    }

    public void setItems(List<WebMenuItem> items) {
        this.items = items;
    }
}