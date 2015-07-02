package cat.institutmarina.insmarina.ui.widget;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by marcpacheco on 2/5/15.
 */
public class CustomDrawerTextView extends TypefacedTextView {

    public CustomDrawerTextView(Context context) {
        super(context);
    }

    public CustomDrawerTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomDrawerTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        
    }
}