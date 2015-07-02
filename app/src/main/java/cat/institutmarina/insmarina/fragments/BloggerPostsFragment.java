package cat.institutmarina.insmarina.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import cat.institutmarina.insmarina.BloggerAPI;
import cat.institutmarina.insmarina.R;
import cat.institutmarina.insmarina.activities.MainActivity;
import cat.institutmarina.insmarina.adapters.BloggerPostsListViewAdapter;
import cat.institutmarina.insmarina.model.BloggerPost;
import cat.institutmarina.insmarina.ui.widget.PageLoading;
import cat.institutmarina.insmarina.utils.EndlessScrollListener;

/**
 * Created by marcpacheco on 3/1/15.
 */
public class BloggerPostsFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    private ListView lsv_blogger_posts;
    private BloggerPostsListViewAdapter adapter;

    private PageLoading mPageLoading;

    private boolean firstRequest = true;

    private BloggerAPI bloggerAPI = new BloggerAPI();

    public static BloggerPostsFragment newInstance() {
        return new BloggerPostsFragment();
    }

    public BloggerPostsFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_blogger_posts, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lsv_blogger_posts = (ListView) view.findViewById(R.id.lsv_fragment_blogger_posts);
        mPageLoading = getPageLoading();
        mPageLoading.setViewToShow(lsv_blogger_posts);
        mPageLoading.showLoading();
        //((MainActivity) getActivity()).setAbsListViewForEnableDisableSwipeRefresh(lsv_blogger_posts);

        lsv_blogger_posts.setOnItemClickListener(this);
        lsv_blogger_posts.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                loadMorePosts();
            }
        });

        executeRequest();
    }


    private void loadMorePosts() {
        executeRequest();
    }

    @Override
    protected void onPreExecuteAsyncTask() {
        super.onPreExecuteAsyncTask();
        if (!firstRequest) {
            adapter.showLoading();
        }
    }

    @Override
    protected Object doAsyncTask() {
        return bloggerAPI.getBloggerPosts();
    }

    @Override
    protected void onAsyncTaskExecuted(Object result) {
        super.onAsyncTaskExecuted(result);
        if (firstRequest) {
            adapter = new BloggerPostsListViewAdapter(getActivity(), (List<BloggerPost>) result);
            lsv_blogger_posts.setAdapter((BloggerPostsListViewAdapter) adapter);
            firstRequest = false;
            mPageLoading.hideLoading();
        } else {
            adapter.addAll((List<BloggerPost>) result);
            adapter.hideLoading();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BloggerPost bloggerPost = (BloggerPost) parent.getItemAtPosition(position);
        ((MainActivity) getActivity()).addFragment(
                BloggerPostDetailFragment.newInstance(bloggerPost));
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.nav_aniram));
    }
}