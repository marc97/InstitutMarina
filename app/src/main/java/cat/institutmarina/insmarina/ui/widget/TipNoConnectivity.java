package cat.institutmarina.insmarina.ui.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;

import cat.institutmarina.insmarina.R;

/**
 * Created by marcpacheco on 2/5/15.
 */
public class TipNoConnectivity {
    public static final int HEADER_HIDE_ANIM_DURATION = 300;

    private TypefacedTextView txv_tip;
    private TypefacedTextView txv_tip_refresh;
    private View noConnectivity;

    private OnTipNoConnectivityPressed onTipNoConnectivityPressed;

    public void attach(Context context, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        noConnectivity = inflater.inflate(R.layout.no_connectivity, parent, false);

        parent.addView(noConnectivity);

        noConnectivity.setVisibility(View.INVISIBLE);
        noConnectivity.setTranslationY(-noConnectivity.getBottom());

        txv_tip = (TypefacedTextView) noConnectivity.findViewById(R.id.txv_tip);
        txv_tip_refresh = (TypefacedTextView) noConnectivity.findViewById(R.id.txv_tip_refresh);

        txv_tip_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
                if (onTipNoConnectivityPressed != null) onTipNoConnectivityPressed.onPressed();
            }
        });
    }

    public void show() {
        noConnectivity.setVisibility(View.VISIBLE);
        noConnectivity.animate()
                .translationY(0)
                .setDuration(HEADER_HIDE_ANIM_DURATION)
                .setInterpolator(new BounceInterpolator());
    }

    public void hide() {
        noConnectivity.animate()
                .translationY(-noConnectivity.getHeight())
                .setDuration(HEADER_HIDE_ANIM_DURATION)
                .setInterpolator(new DecelerateInterpolator());
    }

    public void setOnTipNoConnectivityListener(OnTipNoConnectivityPressed onTipNoConnectivityListener) {
        this.onTipNoConnectivityPressed = onTipNoConnectivityListener;
    }

    public interface OnTipNoConnectivityPressed {
        public abstract void onPressed();
    }

}