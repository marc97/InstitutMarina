package cat.institutmarina.insmarina.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cat.institutmarina.insmarina.R;
import cat.institutmarina.insmarina.model.BloggerPost;
import cat.institutmarina.insmarina.ui.ObservableWebView;
import cat.institutmarina.insmarina.utils.Connectivity;
import cat.institutmarina.insmarina.utils.Utils;

/**
 * Created by marcpacheco on 4/1/15.
 */
public class BloggerPostDetailFragment extends BaseFragment {
    public static final String ARG_BLOGGER_POST = "arg_blogger_post";

    private ObservableWebView wb_post_detail;

    public static BloggerPostDetailFragment newInstance(BloggerPost bloggerPost) {
        BloggerPostDetailFragment bloggerPostDetailFragment = new BloggerPostDetailFragment();

        Bundle args = new Bundle();
        args.putSerializable(ARG_BLOGGER_POST, bloggerPost);
        bloggerPostDetailFragment.setArguments(args);

        return bloggerPostDetailFragment;
    }

    public BloggerPostDetailFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_blogger_post_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        enableAsyncTaskRequest(false);

        wb_post_detail = (ObservableWebView) view.findViewById(R.id.wb_fragment_blogger_post_detail);

        wb_post_detail.getSettings().setLoadWithOverviewMode(true);
        wb_post_detail.getSettings().setUseWideViewPort(true);
        wb_post_detail.getSettings().setJavaScriptEnabled(true);

        wb_post_detail.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Utils.startWebViewActivity(getActivity(), url, url);
                return true;
            }
        });

        wb_post_detail.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    hideRefreshLayoutProgress();
                    if (Connectivity.isConnected(getActivity())) {
                        hideNoConnectivityError();
                    }
                }
            }
        });

        setObservableWebViewForEnableDisableSwipeRefresh(wb_post_detail);
        setOnSwipeRefreshLayoutListener(new SwipeRefreshLayoutListener() {
            @Override
            public void onRefresh() {
               loadWebViewData();
            }
        });
        loadWebViewData();
    }

    private void loadWebViewData() {
        Bundle args = getArguments();
        BloggerPost bloggerPost = (BloggerPost) args.getSerializable(ARG_BLOGGER_POST);
        String data = "<head><meta name='viewport' content='width=device-width, initial-scale=1'/>" +
                "</head><style>" +
                "@font-face {" +
                "    font-family: Titillium;" +
                "    font-style: normal;" +
                "    src: url('file:///android_asset/fonts/TitilliumText25L-400wt.ttf');" +
                "} " +
                "@font-face {" +
                "    font-family: Titillium;" +
                "    font-style: bold;" +
                "    src: url('file:///android_asset/fonts/TitilliumText25L-800wt.ttf');" +
                "} " +
                "*, span { " +
                "    font-family: 'Titillium' !important;" +
                "    color: #" + Integer.toHexString(getResources().getColor(R.color.text_color_primary)).substring(2) + ";"+
                "    font-size: 16px"+
                "} " +
                "h1 {" +
                "    font-size: 26px;" +
                "    line-height: 24px;" +
                "    color: #5E6A73;"+
                "}" +
                "body { " +
                "    padding: 20px 20px;" +
                "    overflow-x: hidden;" +
                "}" +
                ".separator a {" +
                "   margin-left: 0;"+
                "   margin-right: 0;"+
                "}" +
                "img{max-width:100%;height:auto;display:inline;}" +
                "</style><body><h1>" +
                bloggerPost.getPost().getTitle() + "</h1>" + bloggerPost.getPost().getContent() +
                "</body>";

        wb_post_detail.loadDataWithBaseURL("file:///android_asset/", data, "text/html; charset=UTF-8", null, null);

    }
}