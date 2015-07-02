package cat.institutmarina.insmarina.model;

import cat.institutmarina.insmarina.utils.Utils;

/**
 * Created by marcpacheco on 25/1/15.
 */
public class SearchItem {
    private String title, summary, category, creationDate, link;
    private SEARCH_ITEM_TYPE type = SEARCH_ITEM_TYPE.TYPE_MARINA;

    public static enum SEARCH_ITEM_TYPE {
        TYPE_MARINA,
        TYPE_ANIRAM
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        /*this.title = (type == SEARCH_ITEM_TYPE.TYPE_MARINA) ?
                title.substring(title.indexOf(".") + 2, title.length()) : title;*/
        this.title = title;
    }

    public void setTypeItem(SEARCH_ITEM_TYPE type) {
        this.type = type;
    }

    public SEARCH_ITEM_TYPE getTypeItem() {
        return type;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getCreationDate() {
        if (type == SEARCH_ITEM_TYPE.TYPE_ANIRAM) {
            return Utils.rfc3339toHumanReadable(creationDate);
        } else {
            return creationDate;
        }
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getCategory() {
        if (type == SEARCH_ITEM_TYPE.TYPE_ANIRAM) {
            return Utils.getSummaryFromHTML(category, 140);
        }
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}