package nl.frankkie.spotifystreamer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import nl.frankkie.spotifystreamer.model.MyArtist;
import nl.frankkie.spotifystreamer.model.MyTrack;

public class SearchArtistActivity extends ActionBarActivity implements SearchArtistFragment.Callbacks, TopTracksFragment.Callbacks{

    public static final String TOP_TRACKS_FRAGMENT_TAG = "top_tracks_fragment_tag";
    private CharSequence mTitle;
    Toolbar mToolbar;
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();

        //check if songs_fragment_container is present.
        //This is present when the twopane layout is shown.
        if (findViewById(R.id.toptracks_fragment_container) != null) {
            mTwoPane = true;

            //Turn on the activate state in the listview, when in twopane mode
            ((SearchArtistFragment) getFragmentManager().findFragmentById(R.id.search_artist_fragment)).setActivateOnItemClick(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true; //true as in: yes, use menu.
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
            return true; //true as in: handled.
        }
        return super.onOptionsItemSelected(item);
    }

    public void initToolbar() {
        mTitle = getTitle();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(mTitle);
        setSupportActionBar(mToolbar);
    }

    @Override
    public void onItemSelected(MyArtist artist) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            if (artist == null) {
                //To reset the TopTracks Fragment
                arguments.putString(TopTracksFragment.ARG_ARTIST_NAME, "");
                arguments.putString(TopTracksFragment.ARG_ARTIST_ID, "");
                mToolbar.setSubtitle("");
            } else {
                arguments.putString(TopTracksFragment.ARG_ARTIST_NAME, artist.artistName);
                arguments.putString(TopTracksFragment.ARG_ARTIST_ID, artist.id);
                mToolbar.setSubtitle(artist.artistName);
            }
            TopTracksFragment fragment;
            fragment = (TopTracksFragment) getFragmentManager().findFragmentByTag(TOP_TRACKS_FRAGMENT_TAG);
            //Don't re-create fragment just use existing when possible.
            //Thanks Udacity Reviewer !!
            if (fragment == null) {
                fragment = new TopTracksFragment();
                fragment.setArguments(arguments);
                getFragmentManager().beginTransaction()
                        .replace(R.id.toptracks_fragment_container, fragment, TOP_TRACKS_FRAGMENT_TAG)
                        .commit();
            } else {
                //Existing, just replace arguments.
                //http://stackoverflow.com/questions/10364478/got-exception-fragment-already-active
                fragment.getArguments().putAll(arguments);
                //make the fragment refresh results
                fragment.searchTopTracks();
            }
        } else {
            if (artist == null) {
                //Don't launch empty screen on new search
                return;
            }
            Intent detailIntent = new Intent(this, TopTracksActivity.class);
            detailIntent.putExtra(TopTracksFragment.ARG_ARTIST_NAME, artist.artistName);
            detailIntent.putExtra(TopTracksFragment.ARG_ARTIST_ID, artist.id);
            startActivity(detailIntent);
        }
    }

    @Override
    public void onTrackSelected(MyTrack track, String artistName) {
        //This is twopane mode, otherwise TopTracksActivity would have gotten this callback
        Bundle bundle = new Bundle();
        bundle.putParcelable(PlayerFragment.TRACK_MYTRACK, track);
        bundle.putString(PlayerFragment.TRACK_ARTIST, artistName);

        PlayerFragment fragment;
        fragment = new PlayerFragment();
        fragment.setArguments(bundle);
        fragment.show(getFragmentManager(),"player"); //Show like a Dialog
    }
}

