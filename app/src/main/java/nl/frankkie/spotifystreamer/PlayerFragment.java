package nl.frankkie.spotifystreamer;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import nl.frankkie.spotifystreamer.model.MyTrack;

/**
 * http://developer.android.com/reference/android/app/DialogFragment.html
 * Created by FrankkieNL on 1-7-2015.
 */
public class PlayerFragment extends DialogFragment {

    public static String TRACK_MYTRACK = "track_mytrack";
    public static String TRACK_ARTIST = "track_artist";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.player_fragment, container, false);
        ((TextView) v.findViewById(R.id.player_album)).setText(((MyTrack)getArguments().getParcelable(TRACK_MYTRACK)).album);
        ((TextView) v.findViewById(R.id.player_artist)).setText(getArguments().getString(TRACK_ARTIST));
        ((TextView) v.findViewById(R.id.player_trackname)).setText(((MyTrack)getArguments().getParcelable(TRACK_MYTRACK)).title);
        Picasso.with(getActivity())
                .load(((MyTrack)getArguments().getParcelable(TRACK_MYTRACK)).imageBig)
                .placeholder(R.drawable.ic_artist_image_error)
                .error(R.drawable.ic_artist_image_error)
                .into((ImageView) v.findViewById(R.id.player_album_image));
        return v;
    }
}
