package nl.frankkie.spotifystreamer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.ArrayList;

import nl.frankkie.spotifystreamer.model.MyTrack;

/**
 * Created by FrankkieNL on 16-6-2015.
 */
public class TopTracksActivity extends ActionBarActivity implements TopTracksFragment.Callbacks {

    Toolbar mToolbar;
    String artistName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get params
        Intent myIntent = getIntent();
        artistName = myIntent.getStringExtra(TopTracksFragment.ARG_ARTIST_NAME);

        initUI(savedInstanceState);
        initToolbar();
    }

    public void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(getString(R.string.top_10_tracks));
        mToolbar.setSubtitle(artistName);
        setSupportActionBar(mToolbar);
        //enable back-arrow in top-left.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void initUI(Bundle savedInstanceState) {
        //No need for a twopane version of top tracks
        setContentView(R.layout.activity_toptracks);

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(TopTracksFragment.ARG_ARTIST_NAME,
                    getIntent().getStringExtra(TopTracksFragment.ARG_ARTIST_NAME));
            arguments.putString(TopTracksFragment.ARG_ARTIST_ID,
                    getIntent().getStringExtra(TopTracksFragment.ARG_ARTIST_ID));
            TopTracksFragment fragment = new TopTracksFragment();
            fragment.setArguments(arguments);
            getFragmentManager().beginTransaction()
                    .add(R.id.toptracks_fragment_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpTo(this, new Intent(this, SearchArtistActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTrackSelected(ArrayList<MyTrack> tracks, int index, String artistName) {
        //User selected track to play, launch Player,
        //by starting PlayerActivity, as this is not twopane-mode
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(PlayerFragment.TRACK_MYTRACKS, tracks);
        bundle.putString(PlayerFragment.TRACK_ARTIST, artistName);
        bundle.putInt(PlayerFragment.TRACK_POSITION, index);

        Intent i = new Intent(this, PlayerActivity.class);
        i.putExtras(bundle);
        startActivity(i);
    }
}
