package nl.frankkie.spotifystreamer;

import android.app.ListFragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyCallback;
import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.client.Response;

/**
 * Created by FrankkieNL on 16-6-2015.
 */
public class TopTracksFragment extends ListFragment {

    public static final String TAG = "SpotifyStreamer";
    public static final String ARG_ARTIST_NAME = "artist_name";
    public static final String ARG_ARTIST_ID = "artist_id";
    ListView mListView;
    TopTracksAdapter mAdapter;
    String mArtistName;
    String mArtistId;
    Handler mHandler = new Handler();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.toptracks_fragment, container, false);
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mListView = (ListView) getListView();
        mAdapter = new TopTracksAdapter(getActivity());
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), getString(R.string.not_implemented), Toast.LENGTH_LONG).show();
            }
        });

        mArtistName = getArguments().getString(ARG_ARTIST_NAME);
        mArtistId = getArguments().getString(ARG_ARTIST_ID);

        searchTopTracks();
    }

    public void searchTopTracks() {
        //Get chosen location for SharedPreferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String countryCode = prefs.getString("country", getString(R.string.default_countrycode)); //default is dependant on system-language
        SpotifyService spotifyService = new SpotifyApi().getService();
        //Add location as parameter for the SpotifyService
        Map<String,Object> spotifyParameters = new HashMap<String, Object>();
        spotifyParameters.put("country",countryCode);
        spotifyService.getArtistTopTrack(mArtistId, spotifyParameters, new SpotifyCallback<Tracks>() {
            @Override
            public void failure(final SpotifyError spotifyError) {
                Log.v(TAG, "SpotifyError " + spotifyError.getErrorDetails());
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "SpotifyError " + spotifyError.getErrorDetails(), Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void success(final Tracks tracks, Response response) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.setTracks(tracks);
                    }
                });
                Log.v(TAG, "Spotify Response received");
                Log.v(TAG, tracks.toString());
                Log.v(TAG, response.toString()); //Set breakpoint here
            }
        });
    }
}

