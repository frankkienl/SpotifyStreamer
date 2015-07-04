package nl.frankkie.spotifystreamer.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import kaaes.spotify.webapi.android.models.Track;
import nl.frankkie.spotifystreamer.Util;

/**
 * Created by FrankkieNL on 29-6-2015.
 */
public class MyTrack implements Parcelable {

    public String title;
    public String album;
    public String image;
    public String trackId;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(album);
        dest.writeString(image);
        dest.writeString(trackId);
    }

    public static final Parcelable.Creator<MyTrack> CREATOR =
            new Parcelable.Creator<MyTrack>() {

                @Override
                public MyTrack createFromParcel(Parcel source) {
                    return new MyTrack(source);
                }

                @Override
                public MyTrack[] newArray(int size) {
                    return new MyTrack[size];
                }
            };

    private MyTrack(Parcel in){
        //recreate from parcel
        title = in.readString();
        album = in.readString();
        image = in.readString();
        trackId = in.readString();
    }

    public MyTrack(Track track, Context context){
        title = track.name;
        album = track.album.name;
        image = Util.getImageWithBestSize(track.album.images,(int) (48 * context.getResources().getDisplayMetrics().density));
        trackId = track.id;
    }
}
