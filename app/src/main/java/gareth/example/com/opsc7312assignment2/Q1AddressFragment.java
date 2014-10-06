package gareth.example.com.opsc7312assignment2;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Gareth on 13/08/2014.
 */
public class Q1AddressFragment extends Fragment {

    public Q1AddressFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflate the fragment
        return inflater.inflate(R.layout.fragement_address_info,container,false);
    }
}
