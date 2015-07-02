package cat.institutmarina.insmarina.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import cat.institutmarina.insmarina.R;
import cat.institutmarina.insmarina.activities.MainActivity;
import cat.institutmarina.insmarina.utils.Utils;


/**
 * Created by marcpacheco on 19/10/14.
 */
public class ContactFragment extends Fragment {
    private static View view;
    static final LatLng LA_LLAGOSTA = new LatLng(41.51386, 2.19309);
    static final LatLng INSTITUT_MARINA = new LatLng(41.51109, 2.19784);
    private GoogleMap map;

    private static ContactFragment contactFragment;

    public static ContactFragment newInstance() {
        contactFragment = new ContactFragment();
        return contactFragment;
    }

    public static ContactFragment getInstance() {
        if (contactFragment == null) {
            return newInstance();
        } else {
            return contactFragment;
        }
    }

    public ContactFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fragment_contact, container, false);
        } catch (InflateException e) {
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // For contact TextView info.
        TextView txv_contact_email = (TextView) view.findViewById(R.id.txv_fragment_contact_email);
        final String contactInfo = "<a href=\"mailto:iesmarina@xtec.cat\">iesmarina@xtec.cat</a>";
        txv_contact_email.setText(Html.fromHtml(contactInfo));
        txv_contact_email.setMovementMethod(LinkMovementMethod.getInstance());

        TextView txv_contact_phone = (TextView) view.findViewById(R.id.txv_fragment_contact_phone_number);
        final String phoneNumber = "<a href=\"tel:+34-935-607-331\"> 93 560 73 31</a>";
        txv_contact_phone.setText(Html.fromHtml(phoneNumber));
        txv_contact_phone.setMovementMethod(LinkMovementMethod.getInstance());

        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.fragment_contact_map))
                .getMap();
        if (map != null) {
            setUpMap();
        }
    }

    private void setUpMap() {
        Marker marina = map.addMarker(new MarkerOptions()
                .position(INSTITUT_MARINA)
                .title("Ins Marina")
                .snippet("Institut d'Educació Secundaria, Batxillerat i Mòduls")
                .icon(BitmapDescriptorFactory.fromBitmap(Utils.scaleImage(getActivity(), R.drawable.ic_launcher, 30))));
        marina.showInfoWindow();

        // Move the camera instantly to INS MARINA with a zoom of 15.
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(INSTITUT_MARINA, 15));

        // Zoom in, animating the camera.
        map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.nav_contact));
    }
}