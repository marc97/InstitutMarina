package cat.institutmarina.insmarina.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cat.institutmarina.insmarina.R;
import cat.institutmarina.insmarina.model.SearchItem;

/**
 * Created by marcpacheco on 25/1/15.
 */
public class SearchListViewAdapter extends BaseAdapter {
    private List<SearchItem> searchItems;
    private Context ctx;
    private LayoutInflater inflater;

    public SearchListViewAdapter(Context ctx, List<SearchItem> searchItems) {
        this.ctx = ctx;
        this.searchItems = searchItems;
        this.inflater = LayoutInflater.from(ctx);
    }

    @Override
    public int getCount() {
        return searchItems.size();
    }

    @Override
    public Object getItem(int position) {
        return searchItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void addAll(List<SearchItem> searchItems) {
        this.searchItems.addAll(searchItems);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SearchItem searchItem = (SearchItem) getItem(position);
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_search, parent, false);

            holder = new ViewHolder();
            holder.txv_item_search_title = (TextView) convertView.findViewById(R.id.txv_item_search_title);
            holder.txv_item_search_summary = (TextView) convertView.findViewById(R.id.txv_item_search_summary);
            holder.txv_item_search_category = (TextView) convertView.findViewById(R.id.txv_item_search_category);
            holder.txv_item_search_date = (TextView) convertView.findViewById(R.id.txv_item_search_date);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txv_item_search_title.setText(searchItem.getTitle());
        holder.txv_item_search_summary.setText(searchItem.getSummary());
        holder.txv_item_search_date.setText(searchItem.getCreationDate());
        holder.txv_item_search_category.setText(searchItem.getCategory());

        return convertView;
    }

    class ViewHolder {
        TextView txv_item_search_title;
        TextView txv_item_search_summary;
        TextView txv_item_search_category;
        TextView txv_item_search_date;
    }
}