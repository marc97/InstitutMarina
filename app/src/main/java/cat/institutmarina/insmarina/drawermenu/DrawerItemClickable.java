package cat.institutmarina.insmarina.drawermenu;

/**
 * Created by marcpacheco on 6/11/14.
 */
public class DrawerItemClickable extends DrawerItem {
    private String text;
    private int srcImage = -1;
    private int id;

    public DrawerItemClickable(String text, int srcImage, int id) {
        this.text = text;
        this.srcImage = srcImage;
        this.id = id;
    }

    public DrawerItemClickable(String text, int id) {
        this.text = text;
        this.id = id;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getSrcImage() {
        return srcImage;
    }

    public void setSrcImage(int src_image) {
        this.srcImage = srcImage;
    }
}