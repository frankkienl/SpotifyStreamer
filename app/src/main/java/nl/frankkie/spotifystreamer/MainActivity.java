package nl.frankkie.spotifystreamer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import kaaes.spotify.webapi.android.models.Artist;


public class MainActivity extends ActionBarActivity implements SearchArtistFragment.Callbacks {

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
        }
    }

    public void initToolbar() {
        mTitle = getTitle();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(mTitle);
        setSupportActionBar(mToolbar);
    }

    @Override
    public void onItemSelected(String artistName, Artist artist) {
        if (mTwoPane){
            Bundle arguments = new Bundle();
            arguments.putString(TopTracksFragment.ARG_ARTIST_NAME, artistName);
            TopTracksFragment fragment = new TopTracksFragment();
            fragment.setArguments(arguments);
            getFragmentManager().beginTransaction()
                    .replace(R.id.toptracks_fragment_container, fragment)
                    .commit();
        } else {
            Intent detailIntent = new Intent(this, TopTracksActivity.class);
            detailIntent.putExtra(TopTracksFragment.ARG_ARTIST_NAME, artistName);
            startActivity(detailIntent);
        }
    }
}
