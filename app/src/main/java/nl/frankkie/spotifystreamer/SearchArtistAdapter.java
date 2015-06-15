package nl.frankkie.spotifystreamer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Image;

/**
 * Created by FrankkieNL on 9-6-2015.
 */
public class SearchArtistAdapter extends BaseAdapter {

    ArtistsPager artistsPager;
    Context context;

    public SearchArtistAdapter(Context context) {
        this.context = context;
    }

    public void setArtistsPager(ArtistsPager artistsPager) {
        this.artistsPager = artistsPager;
        notifyDataSetChanged();
    }

    public ArtistsPager getArtistsPager() {
        return artistsPager;
    }

    @Override
    public int getCount() {
        if (artistsPager == null) {
            return 0;
        }
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
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.artist_list_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }

        viewHolder = (ViewHolder) convertView.getTag();
        Artist artist = (Artist) getItem(position);
        viewHolder.artistName.setText(artist.name);

        String url = getImageWithBestSize(artist.images, (int) (48 * context.getResources().getDisplayMetrics().density));
        //I'm not a huge fan of this chaining.
        //I call this 'Breiwerk' Dutch for 'Stitching'.
        Picasso.with(context)
                .load(url)
                .placeholder(R.drawable.ic_artist_image_error)
                .error(R.drawable.ic_artist_image_error)
                .into(viewHolder.imageView);
        return convertView;
    }

    /**
     * This will find the image that has the height that closest matched the preference.
     * The image can be bigger or smaller though.
     * @param images list of images to check
     * @param preferredHeight the height you would like the best
     * @return url of an image that best matches the preferred height.
     */
    public static String getImageWithBestSize(List<Image> images, int preferredHeight) {
        if (images == null || images.size() == 0){
            //no images
            return null;
        }
        int index = -1;
        int lowestDiff = Integer.MAX_VALUE;
        for (int i = 0; i < images.size(); i++){
            int imageHeight = images.get(i).height;
            int diffWithPreferred = Math.abs(preferredHeight - imageHeight);
            if (diffWithPreferred < lowestDiff){
                lowestDiff = diffWithPreferred;
                index = i;
            }
        }
        return images.get(index).url;
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
