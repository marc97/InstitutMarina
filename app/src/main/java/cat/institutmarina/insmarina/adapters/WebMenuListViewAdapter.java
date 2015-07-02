package cat.institutmarina.insmarina.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cat.institutmarina.insmarina.R;
import cat.institutmarina.insmarina.model.WebMenuItem;

/**
 * Created by marcpacheco on 8/6/15.
 */
public class WebMenuListViewAdapter extends BaseAdapter {
    private Context ctx;
    private List<WebMenuItem> items;

    private LayoutInflater inflater;

    public WebMenuListViewAdapter(Context ctx, List<WebMenuItem> items) {
        this.ctx = ctx;
        this.items = items;

        inflater = LayoutInflater.from(ctx);
    }

    public void setItems(List<WebMenuItem> items) {
        this.items = items;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
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
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        WebMenuItem item = (WebMenuItem) getItem(position);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_web_menu, parent, false);
        }
        TextView title = (TextView) convertView.findViewById(R.id.txv_item_web_menu);
        ImageView imv = (ImageView) convertView.findViewById(R.id.imv_item_web_menu_arrow);

        title.setText(item.getTitle());

        if (item.getItems() == null) {
            imv.setVisibility(View.GONE);
        } else {
            imv.setVisibility(View.VISIBLE);
        }

        return convertView;
    }
}