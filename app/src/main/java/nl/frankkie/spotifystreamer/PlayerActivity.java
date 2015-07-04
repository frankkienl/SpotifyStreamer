package nl.frankkie.spotifystreamer;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

/**
 * Host activity for the player when not in TwoPane-mode
 * Created by FrankkieNL on 4-7-2015.
 */
public class PlayerActivity extends ActionBarActivity {

    CharSequence mTitle;
    Toolbar mToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        initToolbar();
        initUI(savedInstanceState);
    }

    public void initToolbar() {
        mTitle = getTitle();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(mTitle);
        setSupportActionBar(mToolbar);
        //enable 'up'-button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void initUI(Bundle savedInstanceState){

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = getIntent().getExtras(); //this is shorter than doing it manully for each parameter
            PlayerFragment fragment = new PlayerFragment();
            fragment.setArguments(arguments);
            getFragmentManager().beginTransaction()
                    .add(R.id.player_fragment_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            //Return to SearchArtistActivity
            NavUtils.navigateUpTo(this, new Intent(this, SearchArtistActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
