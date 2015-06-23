package nl.frankkie.spotifystreamer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by FrankkieNL on 20-6-2015.
 */
public class SettingsActivity extends ActionBarActivity {

    private CharSequence mTitle;
    Toolbar mToolbar;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initToolbar();
        initUI();
    }

    public void initToolbar() {
        mTitle = getTitle();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(mTitle);
        setSupportActionBar(mToolbar);
        //enable 'up'-button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void initUI(){
        //http://developer.android.com/guide/topics/ui/controls/spinner.html
        spinner = (Spinner) findViewById(R.id.choose_country_spinner);
        //As spinner is basically a ListView that allows you to select an option.
        MySpinnerAdapter adapter = new MySpinnerAdapter();
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);
                String countryCode = ((String[])parent.getAdapter().getItem(position))[0];
                prefs.edit().putString("country", countryCode).commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //nothing to do here.
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String countryCode = prefs.getString("country", getString(R.string.default_countrycode));
        int position = Util.getIndexByCountryCode(countryCode);
        spinner.setSelection(position);
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

    public class MySpinnerAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return Util.iso3166_1_alpha_2_countryCodes.length;
        }

        @Override
        public Object getItem(int position) {
            return Util.iso3166_1_alpha_2_countryCodes[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            String[] country = (String[]) getItem(position);
            if (convertView == null){
                convertView = getLayoutInflater().inflate(android.R.layout.simple_spinner_dropdown_item,parent,false);
            }
            ((TextView)convertView).setText(country[1]);
            return convertView;
        }
    }
}
