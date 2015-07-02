package cat.institutmarina.insmarina;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cat.institutmarina.insmarina.model.SearchItem;

/**
 * Created by marcpacheco on 25/1/15.
 */
public class SearchAPI {
    public static final String url = "http://www.institutmarina.cat/index.php?type=raw" +
            "&option=com_search&searchphrase=any&ordering=newest&limit=20&searchword=%s&tmpl=component";

    public List<SearchItem> getResult(String search) {
        List<SearchItem> searchItems = new ArrayList<SearchItem>();

        try {
            search = java.net.URLEncoder.encode(search, "UTF-8").replace(" ", "%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(String.format(url, search))
                .build();
        Response response = null;

        try {
            response = client.newCall(request).execute();
            Document doc = Jsoup.parse(response.body().string());

            Elements titles = doc.select(".result-title");
            Elements summaries = doc.select(".result-text");
            Elements categories = doc.select(".result-category");
            Elements creationDates = doc.select(".result-created");

            for (int i = 0; i < titles.size(); i++) {
                SearchItem searchItem = new SearchItem();

                searchItem.setTitle(titles.get(i).text());
                searchItem.setSummary(summaries.get(i).text());
                searchItem.setCategory(categories.get(i).text());
                searchItem.setCreationDate(creationDates.get(i).text());
                searchItem.setLink(Config.URL_MARINA + "/" + titles.get(i).select("a").attr("href"));

                searchItems.add(searchItem);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return searchItems;
    }
}