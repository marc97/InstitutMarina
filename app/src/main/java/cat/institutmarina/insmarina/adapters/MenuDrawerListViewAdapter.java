package cat.institutmarina.insmarina.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import cat.institutmarina.insmarina.R;
import cat.institutmarina.insmarina.drawermenu.DrawerItem;
import cat.institutmarina.insmarina.drawermenu.DrawerItemClickable;
import cat.institutmarina.insmarina.drawermenu.DrawerItemSeparator;
import cat.institutmarina.insmarina.ui.widget.TintableImageView;
import cat.institutmarina.insmarina.ui.widget.TypefacedTextView;

/**
 * Created by marcpacheco on 6/11/14.
 */
public class MenuDrawerListViewAdapter extends BaseAdapter {
    private Context ctx;
    private LayoutInflater inflater;

    private List<DrawerItem> items;

    public static final int DRAWER_CLICKABLE_ITEM = 0;
    public static final int DRAWER_SEPARATOR_ITEM = 1;

    public static final int VIEW_TYPE_COUNT = 2;

    public MenuDrawerListViewAdapter(Context ctx, List<DrawerItem> items) {
        this.ctx = ctx;
        this.inflater = LayoutInflater.from(ctx);

        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        DrawerItem item = (DrawerItem) getItem(position);
        if (item instanceof DrawerItemClickable) {
            return  DRAWER_CLICKABLE_ITEM;
        } else if (item instanceof DrawerItemSeparator) {
            return  DRAWER_SEPARATOR_ITEM;
        }
        return DRAWER_CLICKABLE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        int type = getItemViewType(position);
        return (type == DRAWER_SEPARATOR_ITEM) ? false : true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int viewType = getItemViewType(position);
        DrawerItem item = (DrawerItem) getItem(position);

        switch (viewType) {
            case 0:
                ClickableViewHolder clickableViewHolder = null;

                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.item_drawer_clickable, parent, false);

                    clickableViewHolder = new ClickableViewHolder();
                    clickableViewHolder.txv_text = (TypefacedTextView) convertView.findViewById(R.id.txv_drawer_clickable_item);
                    clickableViewHolder.imv_icon = (TintableImageView) convertView.findViewById(R.id.imv_drawer_clickable_item);

                    convertView.setTag(clickableViewHolder);
                } else {

                    clickableViewHolder = (ClickableViewHolder) convertView.getTag();
                }

                DrawerItemClickable clickableItem = ((DrawerItemClickable) item);

                clickableViewHolder.txv_text.setText(clickableItem.getText());

                if (clickableItem.getSrcImage() != -1) {
                    clickableViewHolder.imv_icon.setImageResource(clickableItem.getSrcImage());
                    clickableViewHolder.imv_icon.setColorFilter((ctx.getResources().getColorStateList(R.color.selector_imv_item_drawer_clickable)));
                } else {
                    clickableViewHolder.imv_icon.setVisibility(View.GONE);
                }


                break;

            case 1:
                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.item_drawer_separator, parent, false);
                }
                break;
        }
        return convertView;
    }

    private static class ClickableViewHolder {
        public TypefacedTextView txv_text;
        public TintableImageView imv_icon;
    }
}