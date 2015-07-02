package cat.institutmarina.insmarina.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import cat.institutmarina.insmarina.Config;
import cat.institutmarina.insmarina.R;
import cat.institutmarina.insmarina.activities.MainActivity;
import cat.institutmarina.insmarina.activities.PicasaPhotoActivity;
import cat.institutmarina.insmarina.adapters.PicasaPhotosGridAdapter;
import cat.institutmarina.insmarina.picasa.PicasaApiReader;
import cat.institutmarina.insmarina.ui.widget.PageLoading;

/**
 * Created by marcpacheco on 11/11/14.
 */
public class PicasaPhotosFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    public static final String ARG_PICASA_ALBUM_ID = "arg_picasa_album_id";
    public static final String ARG_TITLE = "arg_title";

    private GridView grd_picasa_photos;
    private String albumId;

    private PageLoading mPageLoading;

    public PicasaPhotosFragment() {

    }

    public static PicasaPhotosFragment newInstance(String albumId, String title) {
        PicasaPhotosFragment fragment = new PicasaPhotosFragment();

        Bundle args = new Bundle();
        args.putString(ARG_PICASA_ALBUM_ID, albumId);
        args.putString(ARG_TITLE, title);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_picasa_album_photos, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        albumId = getArguments().getString(ARG_PICASA_ALBUM_ID);
        grd_picasa_photos = (GridView) view.findViewById(R.id.grd_picasa_photos);
        setAbsListViewForEnableDisableSwipeRefresh(grd_picasa_photos);

        mPageLoading = (PageLoading)view.findViewById(R.id.page_loading);
        mPageLoading.setViewToShow(grd_picasa_photos);
        mPageLoading.showLoading();

        grd_picasa_photos.setOnItemClickListener(this);

        executeRequest();
    }

    @Override
    protected Object doAsyncTask() {
        PicasaApiReader picasaApi = new PicasaApiReader(getActivity(), Config.INSTITUT_MARINA_PICASA_USER_ID);
        PicasaPhotosGridAdapter adapter = null;
        try {
            adapter = new PicasaPhotosGridAdapter(getActivity(), picasaApi.getPhotos(albumId));
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
        if (result != null)
            mPageLoading.hideLoading();
            grd_picasa_photos.setAdapter((PicasaPhotosGridAdapter) result);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) getActivity()).setActionBarTitle(getArguments().getString(ARG_TITLE));
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Set the nav_photos title
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.nav_photos));
    }

    @Override
    public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
        ArrayList photos =
                (ArrayList)((PicasaPhotosGridAdapter) grd_picasa_photos.getAdapter()).getPhotos();
        Intent intent = new Intent(getActivity(), PicasaPhotoActivity.class);

        Bundle bundle = new Bundle();
        bundle.putString(PicasaPhotoActivity.EXTRA_PHOTO_TITLE, getArguments().getString(ARG_TITLE));
        bundle.putSerializable(PicasaPhotoActivity.EXTRA_PHOTO_LIST_PICASA_PHOTOS, photos);
        bundle.putInt(PicasaPhotoActivity.EXTRA_PHOTO_ADAPTER_POSITION, position);

        intent.putExtras(bundle);

        startActivity(intent);
    }
}