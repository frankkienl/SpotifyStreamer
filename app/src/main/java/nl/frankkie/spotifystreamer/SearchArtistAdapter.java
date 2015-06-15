package nl.frankkie.spotifystreamer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;

/**
 * Created by FrankkieNL on 9-6-2015.
 */
public class SearchArtistAdapter extends BaseAdapter {

    ArtistsPager artistsPager;
    Context context;

    public SearchArtistAdapter(Context context){
        this.context = context;
    }

    public void setArtistsPager(ArtistsPager artistsPager){
        this.artistsPager = artistsPager;
        notifyDataSetChanged();
    }

    public ArtistsPager getArtistsPager(){
        return artistsPager;
    }

    @Override
    public int getCount() {
        if (artistsPager == null){return 0;}
        return artistsPager.artists.items.size();
    }

    @Override
    public Object getItem(int position) {
        return artistsPager.artists.items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.artist_list_item,parent,false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }

        viewHolder = (ViewHolder) convertView.getTag();
        Artist artist = (Artist) getItem(position);
        viewHolder.artistName.setText(artist.name);
        return convertView;
    }

    public static class ViewHolder {

        public final ImageView imageView;
        public final TextView artistName;

        public ViewHolder(View view){
            imageView = (ImageView) view.findViewById(R.id.artist_image);
            artistName = (TextView) view.findViewById(R.id.artist_name);
        }
    }
}