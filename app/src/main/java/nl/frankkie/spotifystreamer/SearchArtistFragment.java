package nl.frankkie.spotifystreamer;

import android.app.ListFragment;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyCallback;
import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import retrofit.client.Response;

/**
 * Created by FrankkieNL on 9-6-2015.
 */
public class SearchArtistFragment extends ListFragment {

    public static final String TAG = "SpotifyStreamer";
    ListView mListView;
    SearchArtistAdapter mAdapter;
    SpotifyApi mSpotifyApi;
    Handler mHandler = new Handler();
    //Don't refresh on EVERY keypress, to not run out of rate-limit.
    boolean refreshSearchResultOnTextChanged = false;
    Callbacks mCallbacks;

    public interface Callbacks {
        public void onItemSelected(Artist artist);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_artist_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCallbacks = (Callbacks) getActivity();
    }

    @Override
    public void onViewCreated(View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //init UI
        mListView = getListView();

        if (mAdapter == null) {
            //Important: this fragment has RetainInstance turned on,
            //So check if the instance-variable is already not null
            //before overriding it with a new empty adapter.
            mAdapter = new SearchArtistAdapter(this.getActivity());
        }
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO: properly handle item click
                mCallbacks.onItemSelected((Artist) mAdapter.getItem(position));
            }
        });

        EditText searchBox = (EditText) view.findViewById(R.id.search_artist_edittext);

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //nothing to do here
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //nothing to do here
            }

            @Override
            public void afterTextChanged(Editable s) {
                //Refresh search results
                if (refreshSearchResultOnTextChanged) {
                    String searchQuery = s.toString();
                    refreshSearchResults(searchQuery);
                }
            }
        });

        searchBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String searchQuery = v.getText().toString();
                refreshSearchResults(searchQuery);
                //False = let someone else handle it.
                //True = we got this.
                return true;
            }
        });
    }

    public void refreshSearchResults(String searchQuery) {
        Toast.makeText(getActivity(), String.format(getString(R.string.searching_for),searchQuery),Toast.LENGTH_SHORT).show();
        if (mSpotifyApi == null) {
            //Create SpotifyInstance only when needed
            //This could be an expensive call,
            //so we hold on to it and don't recreate it every time the users presses a button.
            mSpotifyApi = new SpotifyApi();
            //not setting accessToken, as we'll only use endpoints that do not require authentication
        }
        SpotifyService spotifyService = mSpotifyApi.getService();
        spotifyService.searchArtists(searchQuery, new SpotifyCallback<ArtistsPager>() {
            @Override
            public void success(final ArtistsPager artistsPager, Response response) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        //Using mHandler.post, as this needs to be on the on the UI-thread
                        mAdapter.setArtistsPager(artistsPager);
                    }
                });
                Log.v(TAG, "Spotify Response received");
                Log.v(TAG, artistsPager.toString());
                Log.v(TAG, response.toString()); //Set breakpoint here
            }

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
        });
    }
}
