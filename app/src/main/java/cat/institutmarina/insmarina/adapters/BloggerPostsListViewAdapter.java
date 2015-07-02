package cat.institutmarina.insmarina.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

import cat.institutmarina.insmarina.R;
import cat.institutmarina.insmarina.model.BloggerPost;
import cat.institutmarina.insmarina.utils.Utils;

/**
 * Created by marcpacheco on 3/1/15.
 */
public class BloggerPostsListViewAdapter extends BaseAdapter {
    private List<BloggerPost> posts;
    private LayoutInflater inflater;
    private Context ctx;

    public static final int VIEW_TYPE_POST = 0;
    public static final int VIEW_TYPE_LOADING = 1;

    private boolean isViewLoadingShowed = false;

    public static int VIEW_TYPE_COUNT = 2;

    private DisplayImageOptions options;
    private ImageLoader imageLoader;

    public BloggerPostsListViewAdapter(Context ctx, List<BloggerPost> posts) {
        this.ctx = ctx;
        this.posts = posts;
        inflater = LayoutInflater.from(ctx);

        //UIL
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .resetViewBeforeLoading()
                .build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(ctx));
    }

    public void showLoading() {
        isViewLoadingShowed = true;
        notifyDataSetChanged();
    }

    public void hideLoading() {
        isViewLoadingShowed = false;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return (position >= posts.size()) ? VIEW_TYPE_LOADING
                : VIEW_TYPE_POST;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    public void addAll(List<BloggerPost> posts) {
        this.posts.addAll(posts);
        notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return (isViewLoadingShowed) ? posts.size() + 1 : posts.size();
    }

    @Override
    public Object getItem(int position) {
        return posts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int viewType = getItemViewType(position);

        BloggerPost bloggerPost = null;
        if (viewType == VIEW_TYPE_POST) {
            bloggerPost = (BloggerPost) getItem(position);
        }

        switch (viewType) {
            case VIEW_TYPE_POST:
                PostViewHolder postViewHolder = null;

                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.item_blogger_post, parent, false);

                    postViewHolder = new PostViewHolder();
                    postViewHolder.txv_title = (TextView) convertView.findViewById(R.id.txv_item_blogger_post_title);
                    postViewHolder.txv_summary = (TextView) convertView.findViewById(R.id.txv_item_blogger_post_summary);
                    postViewHolder.txv_date = (TextView) convertView.findViewById(R.id.txv_item_blogger_post_date);
                    postViewHolder.txv_labels = (TextView) convertView.findViewById(R.id.txv_item_blogger_post_labels);
                    postViewHolder.imv_thumbnail = (ImageView) convertView.findViewById(R.id.imv_item_blogger_post_image);

                    convertView.setTag(postViewHolder);

                } else {
                    postViewHolder = (PostViewHolder) convertView.getTag();
                }

                postViewHolder.txv_title.setText(bloggerPost.getPost().getTitle());
                postViewHolder.txv_summary.setText(bloggerPost.getSummary());

                // Labels
                List<String> labels = bloggerPost.getPost().getLabels();
                postViewHolder.txv_labels.setText(Utils.join(labels, ","));

                // Date
                postViewHolder.txv_date.setText(Utils.rfc3339toHumanReadable(bloggerPost.getPost().getPublished().toStringRfc3339()));

                imageLoader.displayImage(bloggerPost.getImageThumbnail(), postViewHolder.imv_thumbnail, options);

                break;

            case VIEW_TYPE_LOADING:
                convertView = inflater.inflate(R.layout.item_loading, parent, false);
                break;
        }

        return convertView;
    }

    class PostViewHolder {
        TextView txv_title;
        TextView txv_summary;
        TextView txv_date;
        TextView txv_labels;
        ImageView imv_thumbnail;
    }
}