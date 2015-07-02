package cat.institutmarina.insmarina.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

import cat.institutmarina.insmarina.R;
import cat.institutmarina.insmarina.utils.Utils;

/**
 * Created by marcpacheco on 8/6/15.
 */
public class HierarchyPositionViewer {
    private ViewGroup _container;
    private Context _ctx;
    private ArrayList<String> _deepLevels = new ArrayList<String>();

    public HierarchyPositionViewer(Context ctx, ViewGroup container) {
        this._ctx = ctx;
        this._container = container;
    }

    public void addDeepLevel(String title) {
        if (_deepLevels.size() != 0) {
            addArrow();
        }

        _deepLevels.add(title);
        ((ViewGroup) _container.getParent()).setVisibility(View.VISIBLE);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TypefacedTextView txv = new TypefacedTextView(_ctx);
        txv.setText(title);
        txv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        txv.setTypeface(_ctx.getString(R.string.font_normal));
        txv.setTextColor(_ctx.getResources().getColor(R.color.white));
        _container.addView(txv);
    }

    public void removeLastDeepLevel() {
        _container.removeViewAt(_container.getChildCount());
        _container.removeViewAt(_container.getChildCount() - 1);
    }

    private void addArrow() {
        TintableImageView imv = new TintableImageView(_ctx);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(30, 30);
        params.gravity = Gravity.CENTER_VERTICAL;
        params.setMargins((int)Utils.convertDpToPixel(_ctx, 6f), 0, (int)Utils.convertDpToPixel(_ctx, 6f), 0);
        imv.setLayoutParams(params);
        imv.setColorFilter(Color.WHITE);

        imv.setImageResource(R.drawable.ic_thin_arrow_right);
        _container.addView(imv);
    }

}