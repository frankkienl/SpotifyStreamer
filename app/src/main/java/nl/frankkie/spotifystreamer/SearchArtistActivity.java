package nl.frankkie.spotifystreamer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import kaaes.spotify.webapi.android.models.Artist;


public class SearchArtistActivity extends ActionBarActivity implements SearchArtistFragment.Callbacks {

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true; //true as in: yes, use menu.
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings){
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
    public void onItemSelected(Artist artist) {
        if (mTwoPane){
            Bundle arguments = new Bundle();
            arguments.putString(TopTracksFragment.ARG_ARTIST_NAME, artist.name);
            arguments.putString(TopTracksFragment.ARG_ARTIST_ID, artist.id);
            TopTracksFragment fragment = new TopTracksFragment();
            fragment.setArguments(arguments);
            getFragmentManager().beginTransaction()
                    .replace(R.id.toptracks_fragment_container, fragment)
                    .commit();
        } else {
            Intent detailIntent = new Intent(this, TopTracksActivity.class);
            detailIntent.putExtra(TopTracksFragment.ARG_ARTIST_NAME, artist.name);
            detailIntent.putExtra(TopTracksFragment.ARG_ARTIST_ID, artist.id);
            startActivity(detailIntent);
        }
    }
}

