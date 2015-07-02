package cat.institutmarina.insmarina.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by marcpacheco on 27/11/14.
 */

/** Created for fix the bug that causes pointerIndex out of range **/
public class ViewPagerBugFixed extends android.support.v4.view.ViewPager {

    public ViewPagerBugFixed(Context context) {
        super(context);
    }

    public ViewPagerBugFixed(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            return super.onTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}