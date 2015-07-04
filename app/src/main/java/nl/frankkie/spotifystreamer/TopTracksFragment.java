package nl.frankkie.spotifystreamer;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.DialogInterface;
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
import nl.frankkie.spotifystreamer.model.MyTrack;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by FrankkieNL on 16-6-2015.
 */
public class TopTracksFragment extends ListFragment {

    public static final String TAG = "SpotifyStreamer";
    public static final String ARG_ARTIST_NAME = "artist_name";
    public static final String ARG_ARTIST_ID = "artist_id";
    public static final String SAVED_TRACKS = "saved_tracks";
    ListView mListView;
    TopTracksAdapter mAdapter;
    String mArtistName;
    String mArtistId;
    Handler mHandler = new Handler();
    Callbacks mCallbacks;

    public interface Callbacks {
        public void onTrackSelected(String trackId, String trackName, String artistName, String albumName, String albumImage);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null){
            mAdapter = new TopTracksAdapter(getActivity());
            mAdapter.setMyTracksArray(savedInstanceState.getParcelableArray(SAVED_TRACKS));
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCallbacks = (Callbacks) getActivity();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mAdapter != null){
            outState.putParcelableArray(SAVED_TRACKS,mAdapter.getMyTracks().toArray(new MyTrack[mAdapter.getCount()]));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.toptracks_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mListView = getListView();
        if (mAdapter == null) {
            //Don't recreate if already created by savedInstanceState
            mAdapter = new TopTracksAdapter(getActivity());
        }
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getActivity(), getString(R.string.not_implemented), Toast.LENGTH_LONG).show();
                MyTrack track = (MyTrack) mAdapter.getItem(position);
                mCallbacks.onTrackSelected(track.id,);
            }
        });

        if (mAdapter.getCount() == 0) {
            //Don't re-download when already filled by savedInstanceState
            //Only download when list is empty.
            searchTopTracks();
        }
    }

    public void searchTopTracks() {
        mArtistName = getArguments().getString(ARG_ARTIST_NAME);
        mArtistId = getArguments().getString(ARG_ARTIST_ID);

        if ("".equals(mArtistId)){
            //reset list
            mAdapter.setMyTracksArray(new MyTrack[0]);
            return;
        }

        //Get chosen location for SharedPreferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String countryCode = prefs.getString("country", getString(R.string.default_countrycode)); //default is dependant on system-language
        SpotifyService spotifyService = new SpotifyApi().getService();
        //Add location as parameter for the SpotifyService
        Map<String, Object> spotifyParameters = new HashMap<String, Object>();
        spotifyParameters.put("country", countryCode);
        spotifyService.getArtistTopTrack(mArtistId, spotifyParameters, new SpotifyCallback<Tracks>() {
            @Override
            public void failure(final SpotifyError spotifyError) {
                Log.v(TAG, "SpotifyError " + spotifyError.getErrorDetails());
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        //check for common errors
                        if (RetrofitError.Kind.NETWORK == spotifyError.getRetrofitError().getKind()) {
                            //No network connection
                            Util.handleNoNetwork(getActivity());
                            return;
                        }
                        if ("Unavailable country".equalsIgnoreCase(spotifyError.getErrorDetails().message)) {
                            Util.handleUnavailableCountry(getActivity());
                            return;
                        }
                        //Other errors, just show and hope users can fix it.
                        if (spotifyError.getErrorDetails() != null) {
                            Toast.makeText(getActivity(), getString(R.string.spotify_error) + ":\n(" + spotifyError.getErrorDetails().status + ") " + spotifyError.getErrorDetails().message, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getActivity(), getString(R.string.spotify_error) + ":\n" + spotifyError.getRetrofitError().getMessage(), Toast.LENGTH_LONG).show();
                        }
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

