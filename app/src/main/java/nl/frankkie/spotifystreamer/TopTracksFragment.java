package nl.frankkie.spotifystreamer;

import android.app.ListFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by FrankkieNL on 16-6-2015.
 */
public class TopTracksFragment extends ListFragment {

    public static final String ARG_ARTIST_NAME = "artist_name";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.toptracks_fragment, container, false);
        return view;
    }
}
