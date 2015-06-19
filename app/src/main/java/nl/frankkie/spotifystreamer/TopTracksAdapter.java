package nl.frankkie.spotifystreamer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;

/**
 * Created by FrankkieNL on 19-6-2015.
 */
public class TopTracksAdapter extends BaseAdapter {

    Context context;
    Tracks mTracks;

    public TopTracksAdapter(Context context){
        this.context = context;
    }

    public void setTracks(Tracks tracks) {
        mTracks = tracks;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mTracks == null) {
            return 0;
        }
        return mTracks.tracks.size();
    }

    @Override
    public Object getItem(int position) {
        if (mTracks == null) {
            return null;
        }
        return mTracks.tracks.get(position);
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
        Track track = (Track) getItem(position);
        viewHolder.title.setText(track.name);
        viewHolder.album.setText(track.album.name);

        String url = SearchArtistAdapter.getImageWithBestSize(track.album.images, (int) (48 * context.getResources().getDisplayMetrics().density));
        //I'm not a huge fan of this chaining.
        //I call this 'Breiwerk' Dutch for 'Stitching'.
        Picasso.with(context)
                .load(url)
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
