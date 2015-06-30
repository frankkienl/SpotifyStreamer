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
import kaaes.spotify.webapi.android.models.ArtistsPager;
import nl.frankkie.spotifystreamer.model.MyArtist;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by FrankkieNL on 9-6-2015.
 */
public class SearchArtistFragment extends ListFragment {

    public static final String TAG = "SpotifyStreamer";
    public static final String SAVED_ARTISTS = "saved_artists";
    ListView mListView;
    SearchArtistAdapter mAdapter;
    SpotifyApi mSpotifyApi;
    Handler mHandler = new Handler();
    //Don't refresh on EVERY keypress, to not run out of rate-limit.
    boolean refreshSearchResultOnTextChanged = false;
    Callbacks mCallbacks;
    //save lastest Toast-message, to be able to cancel it when needed.
    private Toast latestToast;

    public interface Callbacks {
        public void onItemSelected(MyArtist artist);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRetainInstance(true);
        //Now using savedInstanceState instead of setRetainInstance
        //Thanks Udacity Reviewer !!
        if (savedInstanceState != null){
            mAdapter = new SearchArtistAdapter(getActivity());
            mAdapter.setMyArtistArray(savedInstanceState.getParcelableArray(SAVED_ARTISTS));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mAdapter != null){
            //Put in bundle as array
            outState.putParcelableArray(SAVED_ARTISTS, mAdapter.getArtists().toArray(new MyArtist[mAdapter.getCount()]));
        }
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
                mCallbacks.onItemSelected((MyArtist) mAdapter.getItem(position));
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
        if (latestToast != null) {
            //Cancel previous toast if needed.
            latestToast.cancel();
            //Thanks code-reviewer for the idea!
            /*
            (Nitpick) "Notice, what happens when you search many times for artists.
            (The user can make a mistake and want to correct input. )
            The user has to wait until the long sequence of toasts is displayed.
            It can be considered annoying and decrease the quality of user experience.
            It is also very simple to implement, the method .cancel() makes a toast disappear
            (it's important to check whether the toast is null and to declare an appropriate
            member variable)."
             */
        }
        latestToast = Toast.makeText(getActivity(), String.format(getString(R.string.searching_for), searchQuery), Toast.LENGTH_SHORT);
        latestToast.show();
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
                        //check common errors
                        if (RetrofitError.Kind.NETWORK == spotifyError.getRetrofitError().getKind()){
                            //No network connection
                            Util.handleNoNetwork(getActivity());
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
        });
    }
}
