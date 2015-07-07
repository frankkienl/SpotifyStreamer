package nl.frankkie.spotifystreamer;

import android.app.DialogFragment;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import nl.frankkie.spotifystreamer.model.MyTrack;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * http://developer.android.com/reference/android/app/DialogFragment.html
 * Created by FrankkieNL on 1-7-2015.
 */
public class PlayerFragment extends DialogFragment {

    public static final String TAG = "SpotifyStreamer";
    public static String TRACK_MYTRACK = "track_mytrack";
    public static String TRACK_ARTIST = "track_artist";
    MyTrack myTrack;
    Track mTrack;
    MediaPlayer mediaPlayer = new MediaPlayer();
    boolean mediaPlayerIsPrepared = false;
    String mediaUrl;
    Handler mHandler = new Handler();
    TextView tvElapsedTime;
    TextView tvTotalTime;
    SeekBar seekBar;
    Runnable runElapsedTime = new Runnable() {
        @Override
        public void run() {
            long elapsedTime = mediaPlayer.getCurrentPosition(); //milliseconds
            int elapsedSeconds = (int) (elapsedTime / 1000);
            String timeString = "0:"; //fix for songs longer than 1 minute!!!
            timeString += (elapsedSeconds < 10) ? ("0" + elapsedSeconds) : elapsedSeconds;
            tvElapsedTime.setText(timeString);
            tvTotalTime.setText("0:" + (mediaPlayer.getDuration() / 1000));
            seekBar.setMax((int)mediaPlayer.getDuration());
            seekBar.setProgress((int)elapsedTime);
            seekBar.invalidate();
            if (mediaPlayer.isPlaying()) {
                mHandler.postDelayed(runElapsedTime, 100); //every 1/10th of a second.
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.player_fragment, container, false);
        myTrack = getArguments().getParcelable(TRACK_MYTRACK);
        initUI(v);
        downloadTrackInfo();
        return v;
    }

    public void initUI(View v) {
        ((TextView) v.findViewById(R.id.player_album)).setText(myTrack.album);
        ((TextView) v.findViewById(R.id.player_artist)).setText(getArguments().getString(TRACK_ARTIST));
        ((TextView) v.findViewById(R.id.player_trackname)).setText(myTrack.title);
        tvElapsedTime = (TextView) v.findViewById(R.id.player_seekbar_time_elapsed);
        tvTotalTime = (TextView) v.findViewById(R.id.player_seekbar_time_total);
        seekBar = (SeekBar) v.findViewById(R.id.player_seekbar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //TODO: do something!!
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        Picasso.with(getActivity())
                .load(myTrack.imageBig)
                .placeholder(R.drawable.ic_artist_image_error)
                .error(R.drawable.ic_artist_image_error)
                .into((ImageView) v.findViewById(R.id.player_album_image));
        //
        v.findViewById(R.id.player_btn_play_pause).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Play / Pause
                if (mediaPlayer.isPlaying()) {
                    //mediaPlayer.stop();
                    mediaPlayer.pause();
                    ((ImageView) view).setImageDrawable(getActivity().getResources().getDrawable(android.R.drawable.ic_media_play));
                } else {
                    if (mediaPlayerIsPrepared) {
                        mediaPlayer.start();
                        ((ImageView) view).setImageDrawable(getActivity().getResources().getDrawable(android.R.drawable.ic_media_pause));
                        //Start update loop
                        mHandler.post(runElapsedTime);
                    } else {
                        Toast.makeText(getActivity(), "MediaPlayer is not prepared yet. Please wait...", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void downloadTrackInfo() {
        SpotifyApi api = new SpotifyApi();
        SpotifyService service = api.getService();
        service.getTrack(myTrack.trackId, new Callback<Track>() {
            @Override
            public void success(Track track, Response response) {
                mTrack = track;
                initMediaPlayer();
            }

            @Override
            public void failure(final RetrofitError error) {
                Log.v(TAG, "RetrofitError " + error.getMessage());
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        //check common errors
                        if (RetrofitError.Kind.NETWORK == error.getKind()) {
                            //No network connection
                            Util.handleNoNetwork(getActivity());
                            return;
                        }
                        //Other errors, just show and hope users can fix it.
                        Toast.makeText(getActivity(), getString(R.string.spotify_error) + ":\n" + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }

    public void initMediaPlayer() {
        mediaUrl = mTrack.preview_url;
        try {
            mediaPlayer.setDataSource(getActivity(), Uri.parse(mediaUrl));
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), getString(R.string.spotify_error) + ":\n" + getString(R.string.cannot_play), Toast.LENGTH_LONG).show();
            return;
        }
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mediaPlayerIsPrepared = true;
            }
        });
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Toast.makeText(getActivity(), getString(R.string.spotify_error) + ":\n" + getString(R.string.cannot_play), Toast.LENGTH_LONG).show();
                return false;
            }
        });
        mediaPlayer.prepareAsync();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
