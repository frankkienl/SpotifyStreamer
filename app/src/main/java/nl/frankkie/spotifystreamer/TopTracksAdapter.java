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

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import nl.frankkie.spotifystreamer.model.MyTrack;

/**
 * Created by FrankkieNL on 19-6-2015.
 */
public class TopTracksAdapter extends BaseAdapter {

    Context context;
    ArrayList<MyTrack> myTracks = new ArrayList<MyTrack>();

    public TopTracksAdapter(Context context){
        this.context = context;
    }

    public void setTracks(Tracks tracks) {
        myTracks.clear();
        for (Track track : tracks.tracks){
            MyTrack myTrack = new MyTrack(track,context);
            myTracks.add(myTrack);
        }
        notifyDataSetChanged();
    }

    public void setMyTracksArray(Parcelable[] myTracksArray){
        myTracks.clear();
        if (myTracksArray != null && myTracksArray.length != 0){
            for (Parcelable track : myTracksArray){
                myTracks.add((MyTrack) track);
            }
        }
        notifyDataSetChanged();
    }

    public ArrayList<MyTrack> getMyTracks() {
        return myTracks;
    }

    @Override
    public int getCount() {
        return myTracks.size();
    }

    @Override
    public Object getItem(int position) {
        if (myTracks == null) {
            return null;
        }
        return myTracks.get(position);
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
            convertView = inflater.inflate(R.layout.toptracks_list_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) convertView.getTag();
        MyTrack track = (MyTrack) getItem(position);
        viewHolder.title.setText(track.title);
        viewHolder.album.setText(track.album);

        //I'm not a huge fan of this chaining.
        //I call this 'Breiwerk' Dutch for 'Stitching'.
        Picasso.with(context)
                .load(track.image)
                .placeholder(R.drawable.ic_artist_image_error)
                .error(R.drawable.ic_artist_image_error)
                .into(viewHolder.image);
        return convertView;
    }

    public class ViewHolder {
        ImageView image;
        TextView title;
        TextView album;

        public ViewHolder(View view) {
            this.image = (ImageView) view.findViewById(R.id.track_image);
            this.title = (TextView) view.findViewById(R.id.track_name);
            this.album = (TextView) view.findViewById(R.id.track_album);
        }
    }
}
