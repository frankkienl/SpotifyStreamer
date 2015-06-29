package nl.frankkie.spotifystreamer;

import android.content.Context;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import nl.frankkie.spotifystreamer.model.MyArtist;

/**
 * Created by FrankkieNL on 9-6-2015.
 */
public class SearchArtistAdapter extends BaseAdapter {

    Context context;
    ArrayList<MyArtist> artists;

    public SearchArtistAdapter(Context context) {
        this.context = context;
    }

    //Setting the Artists from the Spotify API
    public void setArtistsPager(ArtistsPager artistsPager) {
        artists = new ArrayList<MyArtist>();
        for (Artist artist : artistsPager.artists.items) {
            MyArtist myArtist = new MyArtist(artist, context);
            artists.add(myArtist);
        }

        notifyDataSetChanged();
        if (artists.isEmpty()) {
            //No search results
            Toast.makeText(context, context.getString(R.string.no_search_results_refine), Toast.LENGTH_LONG).show();
        }
    }

    //Setting the Artist from savedInstanceState
    public void setMyArtistArray(Parcelable[] myArtistArray) {
        artists = new ArrayList<MyArtist>();
        if (myArtistArray != null && myArtistArray.length != 0) {
            for (Parcelable artist : myArtistArray) {
                artists.add((MyArtist) artist);
            }
        }
        notifyDataSetChanged();
    }

    public ArrayList<MyArtist> getArtists() {
        return artists;
    }

    @Override
    public int getCount() {
        if (artists == null) {
            return 0;
        }
        return artists.size();
    }

    @Override
    public Object getItem(int position) {
        return artists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.artist_list_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }

        viewHolder = (ViewHolder) convertView.getTag();
        MyArtist artist = (MyArtist) getItem(position);
        viewHolder.artistName.setText(artist.artistName);

        //I'm not a huge fan of this chaining.
        //I call this 'Breiwerk' Dutch for 'Stitching'.
        Picasso.with(context)
                .load(artist.image)
                .placeholder(R.drawable.ic_artist_image_error)
                .error(R.drawable.ic_artist_image_error)
                .into(viewHolder.imageView);
        return convertView;
    }

    public static class ViewHolder {

        public final ImageView imageView;
        public final TextView artistName;

        public ViewHolder(View view) {
            imageView = (ImageView) view.findViewById(R.id.artist_image);
            artistName = (TextView) view.findViewById(R.id.artist_name);
        }
    }
}
