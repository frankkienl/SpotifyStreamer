package nl.frankkie.spotifystreamer.model;

import android.content.Context;
import android.graphics.Point;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.WindowManager;

import kaaes.spotify.webapi.android.models.Track;
import nl.frankkie.spotifystreamer.Util;

/**
 * Created by FrankkieNL on 29-6-2015.
 */
public class MyTrack implements Parcelable {

    public String trackId;
    public String title;
    public String album;
    public String imageSmall;
    public String imageBig;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(trackId);
        dest.writeString(title);
        dest.writeString(album);
        dest.writeString(imageSmall);
        dest.writeString(imageBig);
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
        trackId = in.readString();
        title = in.readString();
        album = in.readString();
        imageSmall = in.readString();
        imageBig = in.readString();
    }

    public MyTrack(Track track, Context context){
        trackId = track.id;
        title = track.name;
        album = track.album.name;
        imageSmall = Util.getImageWithBestSize(track.album.images,(int) (48 * context.getResources().getDisplayMetrics().density));
        Point displaySize = new Point();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSize(displaySize);
        //Get image that would fill width of screen
        imageBig = Util.getImageWithBestSize(track.album.images,(int) (displaySize.x * context.getResources().getDisplayMetrics().density));
    }
}
