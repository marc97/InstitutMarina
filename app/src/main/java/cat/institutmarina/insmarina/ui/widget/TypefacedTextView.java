package cat.institutmarina.insmarina.ui.widget;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import cat.institutmarina.insmarina.R;

/**
 * Created by marcpacheco on 2/5/15.
 */

public class TypefacedTextView extends TextView {
    private static Map<String, Typeface> mTypefaces;
    private Context ctx;

    public TypefacedTextView(final Context context) {
        this(context, null);
        ctx = context;
    }

    public TypefacedTextView(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
        ctx = context;
    }

    public void setTypeface(String typefaceAssetPath) {
        super.setTypeface(typefaceFromAssets(typefaceAssetPath));
    }

    public TypefacedTextView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        ctx = context;
        if (mTypefaces == null) {
            mTypefaces = new HashMap<String, Typeface>();
        }

        // prevent exception in Android Studio / ADT interface builder
        if (this.isInEditMode()) {
            return;
        }


        final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TypefacedTextView);
        if (array != null) {
            final String typefaceAssetPath = array.getString(
                    R.styleable.TypefacedTextView_typeface);

            if (typefaceAssetPath != null) {
                Typeface typeface = null;

                if (mTypefaces.containsKey(typefaceAssetPath)) {
                    typeface = mTypefaces.get(typefaceAssetPath);
                } else {
                    typeface = typefaceFromAssets(typefaceAssetPath);
                    mTypefaces.put(typefaceAssetPath, typeface);
                }

                super.setTypeface(typeface);
            }
            array.recycle();
        }
    }

    private Typeface typefaceFromAssets(String typefaceAssetPath) {
        AssetManager assets = ctx.getAssets();
        return Typeface.createFromAsset(assets, typefaceAssetPath);
    }
}