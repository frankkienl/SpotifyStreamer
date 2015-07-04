package nl.frankkie.spotifystreamer;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * http://developer.android.com/reference/android/app/DialogFragment.html
 * Created by FrankkieNL on 1-7-2015.
 */
public class PlayerFragment extends DialogFragment {

    public static String TRACK_ID = "track_id";
    public static String TRACK_ARTIST = "track_artist";
    public static String TRACK_ALBUM = "track_album";
    public static String TRACK_ALBUM_IMAGE = "track_album_image";
    public static String TRACK_NAME = "track_name";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.player_fragment, container, false);
        return v;
    }
}
