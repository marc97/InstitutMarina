package cat.institutmarina.insmarina.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONException;

import java.io.IOException;

import cat.institutmarina.insmarina.Config;
import cat.institutmarina.insmarina.R;
import cat.institutmarina.insmarina.activities.MainActivity;
import cat.institutmarina.insmarina.adapters.PicasaAlbumsGridAdapter;
import cat.institutmarina.insmarina.picasa.PicasaApiReader;
import cat.institutmarina.insmarina.picasa.model.PicasaAlbum;
import cat.institutmarina.insmarina.ui.widget.PageLoading;

/**
 * Created by marcpacheco on 6/11/14.
 */
public class PicasaAlbumsFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    private GridView grd_picasa;
    private PageLoading mPageLoading;

    public PicasaAlbumsFragment() {

    }

    public static PicasaAlbumsFragment newInstance() {
        return new PicasaAlbumsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_picasa_albums, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        grd_picasa = (GridView) view.findViewById(R.id.grd_picasa_albums);
        grd_picasa.setOnItemClickListener(this);
        setAbsListViewForEnableDisableSwipeRefresh(grd_picasa);

        mPageLoading = getPageLoading();
        mPageLoading.setViewToShow(grd_picasa);

        executeRequest();
    }

    @Override
    protected void onPreExecuteAsyncTask() {
        mPageLoading.showLoading();
    }

    @Override
    protected Object doAsyncTask() {
        PicasaApiReader picasaApi = new PicasaApiReader(getActivity(), Config.INSTITUT_MARINA_PICASA_USER_ID);
        PicasaAlbumsGridAdapter adapter = null;
        try {
            adapter = new PicasaAlbumsGridAdapter(getActivity(), picasaApi.getAlbums());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return adapter;
    }

    @Override
    protected void onAsyncTaskExecuted(Object result) {
        super.onAsyncTaskExecuted(result);
        if (result != null) {
            grd_picasa.setAdapter((PicasaAlbumsGridAdapter) result);
            mPageLoading.hideLoading();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PicasaAlbum album = (PicasaAlbum) parent.getItemAtPosition(position);
        ((MainActivity) getActivity()).addFragment(
                PicasaPhotosFragment.newInstance(album.getAlbumId(), album.getAlbumTitle()));
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.nav_photos));
    }
}