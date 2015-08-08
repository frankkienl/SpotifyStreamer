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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

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
    public static String TRACK_MYTRACKS = "track_mytracks";
    public static String TRACK_ARTIST = "track_artist";
    public static String TRACK_POSITION = "track_position";
    ArrayList<MyTrack> myTracks;
    int currentPosition;
    String currentArtists;
    MyTrack currentMyTrack;
    Track mTrack;
    MediaPlayer mediaPlayer;
    boolean mediaPlayerIsPrepared = false;
    boolean startWhenPrepared = false;
    String mediaUrl;
    Handler mHandler = new Handler();
    //UI Elements
    TextView tvElapsedTime;
    TextView tvTotalTime;
    SeekBar seekBar;
    TextView tvAlbum;
    TextView tvArtist;
    TextView tvTrackname;
    ImageButton previousBtn;
    ImageButton nextBtn;
    ImageButton playPauseBtn;
    //
    Runnable runElapsedTime = new Runnable() {
        @Override
        public void run() {
            if (mediaPlayer == null) {
                //this happens when pressing next/previous while isPlaying.
                return;
            }
            long elapsedTime = mediaPlayer.getCurrentPosition(); //milliseconds
            int elapsedSeconds = (int) (elapsedTime / 1000);
            String timeString = "0:"; //fix for songs longer than 1 minute!!!
            timeString += (elapsedSeconds < 10) ? ("0" + elapsedSeconds) : elapsedSeconds;
            tvElapsedTime.setText(timeString);
            tvTotalTime.setText("0:" + (mediaPlayer.getDuration() / 1000));
            seekBar.setMax((int) mediaPlayer.getDuration());
            seekBar.setProgress((int) elapsedTime);
            seekBar.invalidate();
            if (mediaPlayer.isPlaying()) {
                mHandler.postDelayed(runElapsedTime, 100); //every 1/10th of a second.
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.player_fragment, container, false);
        myTracks = getArguments().getParcelableArrayList(TRACK_MYTRACKS);
        currentPosition = getArguments().getInt(TRACK_POSITION);
        currentMyTrack = myTracks.get(currentPosition);
        currentArtists = getArguments().getString(TRACK_ARTIST);
        initUI(v);
        prepareTrack();
        return v;
    }

    public void initUI(View v) {
        tvAlbum = (TextView) v.findViewById(R.id.player_album);
        tvArtist = (TextView) v.findViewById(R.id.player_artist);
        tvTrackname = (TextView) v.findViewById(R.id.player_trackname);
        previousBtn = (ImageButton) v.findViewById(R.id.player_btn_previous);
        nextBtn = (ImageButton) v.findViewById(R.id.player_btn_next);
        playPauseBtn = (ImageButton) v.findViewById(R.id.player_btn_play_pause);
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
                .load(currentMyTrack.imageBig)
                .placeholder(R.drawable.ic_artist_image_error)
                .error(R.drawable.ic_artist_image_error)
                .into((ImageView) v.findViewById(R.id.player_album_image));
        //
        playPauseBtn.setOnClickListener(new View.OnClickListener() {
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
                        Toast.makeText(getActivity(), R.string.mediaplayer_not_prepared, Toast.LENGTH_SHORT).show();
                        startWhenPrepared = true;
                    }
                }
            }
        });
        previousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPosition--;
                currentMyTrack = myTracks.get(currentPosition);
                prepareTrack();
            }
        });
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPosition++;
                currentMyTrack = myTracks.get(currentPosition);
                prepareTrack();
            }
        });
    }

    public void prepareTrack() {
        //Disable previous and next button when reached end of list
        previousBtn.setEnabled((currentPosition != 0));
        nextBtn.setEnabled((currentPosition != myTracks.size() - 1));
        playPauseBtn.setImageDrawable(getActivity().getResources().getDrawable(android.R.drawable.ic_media_play));
        //Stop currently playing
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
        seekBar.setProgress(0);
        tvAlbum.setText(currentMyTrack.album);
        tvArtist.setText(currentArtists);
        tvTrackname.setText(currentMyTrack.title);

        SpotifyApi api = new SpotifyApi();
        SpotifyService service = api.getService();
        service.getTrack(currentMyTrack.trackId, new Callback<Track>() {
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
        mediaPlayer = new MediaPlayer();
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
                if (startWhenPrepared) {
                    mediaPlayer.start();
                }
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