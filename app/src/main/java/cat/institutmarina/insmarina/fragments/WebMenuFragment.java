package cat.institutmarina.insmarina.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import cat.institutmarina.insmarina.R;
import cat.institutmarina.insmarina.activities.MainActivity;
import cat.institutmarina.insmarina.adapters.WebMenuListViewAdapter;
import cat.institutmarina.insmarina.model.WebMenuItem;
import cat.institutmarina.insmarina.ui.widget.HierarchyPositionViewer;
import cat.institutmarina.insmarina.utils.Utils;

/**
 * Created by marcpacheco on 8/6/15.
 */
public class WebMenuFragment extends Fragment implements AdapterView.OnItemClickListener {
    private ListView _lsv;
    private WebMenuListViewAdapter _adapter;
    private HierarchyPositionViewer _hierarchyPositionViewer;
    private ArrayList<String> mHierarchyTitles;
    private String mTitle;

    public static final String ARG_DATA = "ARG_DATA";
    public static final String ARG_HIERARCHY_TITLES = "ARG_HIERARCHY_TITLES";

    public static WebMenuFragment newInstance(List<WebMenuItem> data, ArrayList<String> title) {
        WebMenuFragment frag = new WebMenuFragment();

        Bundle args = new Bundle();
        args.putSerializable(ARG_DATA, (ArrayList) data);
        args.putStringArrayList(ARG_HIERARCHY_TITLES, title);
        frag.setArguments(args);

        return frag;
    }


    public WebMenuFragment() {

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mHierarchyTitles.remove(mHierarchyTitles.size() - 1);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_web_menu, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        _hierarchyPositionViewer = new HierarchyPositionViewer(getActivity(),
                (ViewGroup) view.findViewById(R.id.hierarchy_position_viewer_container));

        Bundle args = getArguments();
        if (args != null) {
            mHierarchyTitles = args.getStringArrayList(ARG_HIERARCHY_TITLES);
            ArrayList<WebMenuItem> data = (ArrayList<WebMenuItem>) args.getSerializable(ARG_DATA);
            _adapter = new WebMenuListViewAdapter(getActivity(), data);
        } else {
            _adapter = new WebMenuListViewAdapter(getActivity(), getWebMenuItems());
        }

        if (mHierarchyTitles == null) {
            mHierarchyTitles = new ArrayList<String>();
            mHierarchyTitles.add(getString(R.string.nav_menu));
        }

        for (int i = 0; i < mHierarchyTitles.size(); i++) {
            _hierarchyPositionViewer.addDeepLevel(mHierarchyTitles.get(i));
        }
        mTitle = mHierarchyTitles.get(mHierarchyTitles.size() - 1);
        ((MainActivity) getActivity()).setActionBarTitle(mTitle);

        _lsv = (ListView) view.findViewById(R.id.lsv_fragment_web_menu);

        _lsv.setAdapter(_adapter);
        _lsv.setOnItemClickListener(this);
    }

    /****** This code looks so bad :P  ******/

    private List<WebMenuItem> getWebMenuItems() {
        AssetManager assetManager = getActivity().getAssets();
        InputStream is = null;

        try {
            is = assetManager.open("menu_data.json");
        } catch (IOException e) {
            e.printStackTrace();
        }

        String data = getStringFromInputStream(is);
        List<WebMenuItem> items = null;
        try {
            JSONObject json = new JSONObject(data);
            items = readWebMenuItems(json.getJSONArray("items"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return items;
    }

    private List<WebMenuItem> readWebMenuItems(JSONArray array) {
        List<WebMenuItem> items = new ArrayList<WebMenuItem>();

        if (array != null) {
            try {
                for (int i = 0; i < array.length(); i++) {
                    WebMenuItem item = new WebMenuItem();

                    JSONObject object = array.getJSONObject(i);

                    item.setTitle(object.optString("title"));
                    item.setUrl(object.optString("url"));
                    item.setItems(readWebMenuItems(object.optJSONArray("items")));

                    items.add(item);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            return null;
        }

        return items;
    }

    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }

    /**********/

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        WebMenuItem webMenuItem = (WebMenuItem) parent.getItemAtPosition(position);
        String url = webMenuItem.getUrl();
        String title = webMenuItem.getTitle();

        if (url != null && !url.equals("")) {
            Utils.startWebViewActivity(getActivity(), title, url);
        } else {
            mHierarchyTitles.add(title);
            WebMenuFragment frag = WebMenuFragment.newInstance(webMenuItem.getItems(), mHierarchyTitles);
                    ((MainActivity) getActivity()).addFragment(frag);
            /*
            _adapter.setItems(webMenuItem.getItems());
            _adapter.notifyDataSetChanged();

            _hierarchyPositionViewer.addDeepLevel(title);
            ((MainActivity) getActivity()).setActionBarTitle(title);
            */
        }
    }
}