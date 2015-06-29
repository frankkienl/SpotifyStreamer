package nl.frankkie.spotifystreamer;

import android.content.Context;
import android.os.Parcel;
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

/**
 * Created by FrankkieNL on 9-6-2015.
 */
public class SearchArtistAdapter extends BaseAdapter {

    Context context;
    ArrayList<MyArtist> artists;

    public SearchArtistAdapter(Context context) {
        this.context = context;
    }

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

    /**
     * Creating my own Artist object,
     * as the Artist object from the library is not Parcelable.
     */
    public static class MyArtist implements Parcelable {

        String artistName;
        String image;
        String id;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(artistName);
            dest.writeString(image);
            dest.writeString(id);
        }

        public static final Parcelable.Creator<MyArtist> CREATOR =
                new Parcelable.Creator<MyArtist>() {

                    @Override
                    public MyArtist createFromParcel(Parcel source) {
                        return new MyArtist(source);
                    }

                    @Override
                    public MyArtist[] newArray(int size) {
                        return new MyArtist[size];
                    }
                };

        private MyArtist(Parcel in) {
            //recreate from parcel
            artistName = in.readString();
            image = in.readString();
            id = in.readString();
        }

        public MyArtist(Artist artist, Context context) {
            //create from Artist-object from library
            artistName = artist.name;
            image = Util.getImageWithBestSize(artist.images, (int) (48 * context.getResources().getDisplayMetrics().density));
            id = artist.id;
        }
    }
}
