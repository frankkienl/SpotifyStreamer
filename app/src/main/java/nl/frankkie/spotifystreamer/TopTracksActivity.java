package nl.frankkie.spotifystreamer;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by FrankkieNL on 16-6-2015.
 */
public class TopTracksActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI(savedInstanceState);
    }

    public void initUI(Bundle savedInstanceState){
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
            TopTracksFragment fragment = new TopTracksFragment();
            fragment.setArguments(arguments);
            getFragmentManager().beginTransaction()
                    .add(R.id.toptracks_fragment_container, fragment)
                    .commit();
        }
    }
}
