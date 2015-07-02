package cat.institutmarina.insmarina.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import cat.institutmarina.insmarina.R;
import cat.institutmarina.insmarina.ui.ObservableWebView;
import cat.institutmarina.insmarina.ui.widget.PageLoading;
import cat.institutmarina.insmarina.ui.widget.TipNoConnectivity;
import cat.institutmarina.insmarina.utils.Connectivity;

/**
 * Created by marcpacheco on 6/11/14.
 */
public abstract class BaseFragment extends Fragment {
    private PageLoading mPageLoading;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private SwipeRefreshLayoutListener swipeRefreshLayoutListener;
    private boolean mEnableAsyncTaskRequest = true;

    private TipNoConnectivity mTipNoConnectivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        getSwipeRefreshLayout(view);
        mTipNoConnectivity = new TipNoConnectivity();
        mTipNoConnectivity.attach(getActivity(), (ViewGroup)view);
        mTipNoConnectivity.setOnTipNoConnectivityListener(new TipNoConnectivity.OnTipNoConnectivityPressed() {
            @Override
            public void onPressed() {
                onSwipeRefreshLayout();
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    public PageLoading getPageLoading() {
        if (mPageLoading == null) {
            mPageLoading = (PageLoading) getView().findViewById(R.id.page_loading);
        }
        return mPageLoading;
    }

    // Swipe to refresh layout.

    public SwipeRefreshLayout getSwipeRefreshLayout(View view) {
        if (mSwipeRefreshLayout == null) {
            mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        }

        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    onSwipeRefreshLayout();
                }
            });

            mSwipeRefreshLayout.setColorScheme(android.R.color.holo_green_dark,
                    android.R.color.holo_red_dark,
                    android.R.color.holo_blue_dark,
                    android.R.color.holo_orange_dark);

            mSwipeRefreshLayout.setRefreshing(false);
            mSwipeRefreshLayout.setEnabled(true);
        }
        return mSwipeRefreshLayout;
    }

    public void showRefreshLayoutProgress() {
        if (mSwipeRefreshLayout != null) mSwipeRefreshLayout.setRefreshing(true);
    }

    public void hideRefreshLayoutProgress() {
        if (mSwipeRefreshLayout != null) mSwipeRefreshLayout.setRefreshing(false);
    }

    public void enableSwipeRefresh() {
        if (mSwipeRefreshLayout != null) mSwipeRefreshLayout.setEnabled(true);
    }

    public void disableSwipeRefresh() {
        if (mSwipeRefreshLayout != null) mSwipeRefreshLayout.setEnabled(false);
    }


    public void setOnSwipeRefreshLayoutListener(SwipeRefreshLayoutListener swipeRefreshLayoutListener) {
        this.swipeRefreshLayoutListener = swipeRefreshLayoutListener;
    }

    public interface SwipeRefreshLayoutListener {
        public void onRefresh();
    }

    protected void onSwipeRefreshLayout() {
        if (swipeRefreshLayoutListener != null) {
            swipeRefreshLayoutListener.onRefresh();
        }
        executeRequest();
           /* if (Connectivity.isConnected(getActivity())) {
                hideNoConnectivityError();
                swipeRefreshLayoutListener.onRefresh();
                executeRequest();
            } else {
                showNoConnectivityError();
                hideRefreshLayoutProgress();
            }
        }*/
    }

    public void hideNoConnectivityError() {
        mTipNoConnectivity.hide();
    }

    public void showNoConnectivityError() {
        mTipNoConnectivity.show();
    }

    public void setAbsListViewForEnableDisableSwipeRefresh(final AbsListView absListView) {
        absListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int topRowVerticalPosition = (view == null || view.getChildCount() == 0) ?
                        0 : view.getChildAt(0).getTop();
                if (topRowVerticalPosition >= 0 && firstVisibleItem == 0) {
                    enableSwipeRefresh();
                } else {
                    disableSwipeRefresh();
                }
            }
        });
    }

    public void setObservableWebViewForEnableDisableSwipeRefresh(ObservableWebView wb) {
        wb.setCallbacks(new ObservableWebView.Callbacks() {
            @Override
            public void onScrollChanged(int scrollY) {
                if (scrollY == 0) {
                    enableSwipeRefresh();
                } else {
                    disableSwipeRefresh();
                }
            }

            @Override
            public void onScaleChanged() {

            }

            @Override
            public void onMotionEvent(MotionEvent motionEvent) {

            }
        });
    }

    // Async Operations.

    protected void executeAsyncTask() {
        new AsyncLoader().execute();
    }

    protected void onPreExecuteAsyncTask() {

    }

    protected Object doAsyncTask() {
        return null;
    }

    protected void onAsyncTaskExecuted(Object result) {
        hideRefreshLayoutProgress();
        hideNoConnectivityError();
    }

    private class AsyncLoader extends AsyncTask<Void, Void, Object> {
        @Override
        protected void onPreExecute() {
            onPreExecuteAsyncTask();
        }

        @Override
        protected Object doInBackground(Void... params) {
            return doAsyncTask();
        }

        @Override
        protected void onPostExecute(Object o) {
            onAsyncTaskExecuted(o);
        }
    }

    // Default Request middleware
    public void executeRequest() {
        if (Connectivity.isConnected(getActivity())) {
           if (mEnableAsyncTaskRequest) { executeAsyncTask(); }
        } else {
            showNoConnectivityError();
            hideRefreshLayoutProgress();
        }
    }

    public void enableAsyncTaskRequest(boolean mEnableAsyncTaskRequest) {
        this.mEnableAsyncTaskRequest = mEnableAsyncTaskRequest;
    }
}