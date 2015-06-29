package nl.frankkie.spotifystreamer.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import kaaes.spotify.webapi.android.models.Artist;
import nl.frankkie.spotifystreamer.Util;

/**
 * Creating my own Artist object,
 * as the Artist object from the library is not Parcelable.
 */
public class MyArtist implements Parcelable {

    public String artistName;
    public String image;
    public String id;

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