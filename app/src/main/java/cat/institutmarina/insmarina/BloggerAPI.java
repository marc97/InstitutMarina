package cat.institutmarina.insmarina;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.blogger.Blogger;
import com.google.api.services.blogger.model.Post;
import com.google.api.services.blogger.model.PostList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cat.institutmarina.insmarina.model.BloggerPost;
import cat.institutmarina.insmarina.model.SearchItem;
import cat.institutmarina.insmarina.utils.Utils;

/**
 * Created by marcpacheco on 17/1/15.
 */
public class BloggerAPI {
    private HttpTransport HTTP_TRANSPORT;
    private JsonFactory JSON_FACTORY;

    private String nextPageToken;

    public BloggerAPI() {
        HTTP_TRANSPORT = new NetHttpTransport();
        JSON_FACTORY = new JacksonFactory();
    }

    public List<SearchItem> getSearchResults(String search) {
        List<SearchItem> items = new ArrayList<>();

       Blogger blogger = new Blogger.Builder(HTTP_TRANSPORT, JSON_FACTORY, null)
                .setApplicationName("Blogger-PostsSearch-Snippet/1.0").build();

        Blogger.Posts.Search postsSearchAction = null;
        try {
            postsSearchAction = blogger.posts().search(Config.INSTITUT_MARINA_BLOGGER_BLOG_ID, search);
        } catch (IOException e) {
            e.printStackTrace();
        }

        postsSearchAction.setQ(search);

        postsSearchAction.setFields("items(published,title,url,content,labels)");

        PostList posts = null;
        try {
            postsSearchAction.setKey(Config.BLOGGER_V3_API_KEY);
            posts = postsSearchAction.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (posts.getItems() != null && !posts.getItems().isEmpty()) {
            for (Post post : posts.getItems()) {
                SearchItem searchItem = new SearchItem();
                searchItem.setTitle(post.getTitle());
                searchItem.setSummary(Utils.getSummaryFromHTML(post.getContent(), 140));
                searchItem.setCreationDate(post.getPublished().toString());
                searchItem.setCategory(Utils.join(post.getLabels(), ","));
                searchItem.setLink(post.getUrl());
                searchItem.setTypeItem(SearchItem.SEARCH_ITEM_TYPE.TYPE_ANIRAM);
                items.add(searchItem);
            }
        }

        return items;
    }

    public List<BloggerPost> getBloggerPosts() {
        Blogger blogger = new Blogger.Builder(HTTP_TRANSPORT, JSON_FACTORY, null)
                .setApplicationName("Blogger-PostsList-Snippet/1.0").build();


        Blogger.Posts.List postsListAction = null;
        PostList posts = null;
        try {
            postsListAction = blogger.posts().list(Config.INSTITUT_MARINA_BLOGGER_BLOG_ID);

            postsListAction.setPageToken(nextPageToken);

            postsListAction.setKey(Config.BLOGGER_V3_API_KEY);
            posts = postsListAction.execute();


        } catch (IOException e) {
            e.printStackTrace();
        }

        List<BloggerPost> posts_list = new ArrayList<BloggerPost>();
        for (final Post post : posts.getItems()) {
            BloggerPost bloggerPost = new BloggerPost();
            bloggerPost.setPost(post);

            posts_list.add(bloggerPost);

            nextPageToken = posts.getNextPageToken();
        }

        return posts_list;
    }
}