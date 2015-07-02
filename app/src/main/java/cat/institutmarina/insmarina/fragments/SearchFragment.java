package cat.institutmarina.insmarina.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import cat.institutmarina.insmarina.BloggerAPI;
import cat.institutmarina.insmarina.R;
import cat.institutmarina.insmarina.SearchAPI;
import cat.institutmarina.insmarina.adapters.SearchListViewAdapter;
import cat.institutmarina.insmarina.model.SearchItem;
import cat.institutmarina.insmarina.ui.widget.PageLoading;
import cat.institutmarina.insmarina.utils.Utils;

/**
 * Created by marcpacheco on 25/1/15.
 */
public class SearchFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    public static final String ARG_TYPE = "arg_type";

    public static final int TYPE_MARINA = 0;
    public static final int TYPE_ANIRAM = 1;

    private PageLoading pageLoading;

    private int currentType;

    private ListView lsv_search;

    private String search = "";

    public static SearchFragment newInstance(int type) {
        SearchFragment searchFragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TYPE, type);
        searchFragment.setArguments(args);
        return searchFragment;
    }

    public int getPageType() {
        return currentType;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    public void updateSearch(String search) {
        this.search = search;

        //executeAsyncTask();
        executeRequest();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        currentType = getArguments().getInt(ARG_TYPE);

        pageLoading = (PageLoading) view.findViewById(R.id.page_loading);
        pageLoading.setViewToShow(view.findViewById(R.id.lsv_fragment_search));
        searchNoResults();

        lsv_search = (ListView) view.findViewById(R.id.lsv_fragment_search);
        lsv_search.setOnItemClickListener(this);
    }

    private void searchNoResults() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pageLoading.showError();
            }
        });
    }

    private void hideLoading() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pageLoading.hideLoading();
            }
        });
    }

    private void showLoading() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pageLoading.showLoading();
            }
        });
    }

    @Override
    protected Object doAsyncTask() {
        SearchListViewAdapter searchListViewAdapter = null;
        showLoading();
        switch (currentType) {
            case TYPE_MARINA:
                SearchAPI searchAPI = new SearchAPI();
                searchListViewAdapter =
                        new SearchListViewAdapter(getActivity(), searchAPI.getResult(search));

                break;

            case TYPE_ANIRAM:
                BloggerAPI bloggerAPI = new BloggerAPI();
                searchListViewAdapter =
                        new SearchListViewAdapter(getActivity(), bloggerAPI.getSearchResults(search));
                break;
        }

        return searchListViewAdapter;
    }

    @Override
    protected void onAsyncTaskExecuted(Object result) {
        super.onAsyncTaskExecuted(result);
        lsv_search.setAdapter((SearchListViewAdapter) result);
        if (((SearchListViewAdapter) result).getCount() == 0) {
            searchNoResults();
        } else {
            hideLoading();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final SearchItem item = (SearchItem) parent.getItemAtPosition(position);
        Utils.startWebViewActivity(getActivity(), item.getTitle(), item.getLink());
    }
}