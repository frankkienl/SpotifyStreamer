package nl.frankkie.spotifystreamer;

import android.app.DialogFragment;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
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
public class PlayerFragment extends DialogFragment implements PlayerService.MediaCallbacks {

    public static final String TAG = "SpotifyStreamer";
    public static String TRACK_MYTRACKS = "track_mytracks";
    public static String TRACK_ARTIST = "track_artist";
    public static String TRACK_POSITION = "track_position";
    public static String RESTORE_CURRENT_TRACK_POSITION = "restore_current_track_position";
    ArrayList<MyTrack> myTracks;
    int currentPosition = -1;
    String currentArtists;
    MyTrack currentMyTrack;
    Track mTrack;
    //http://developer.android.com/guide/topics/media/mediaplayer.html
    boolean startWhenPrepared = true;
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
    ImageView albumImage;
    //
    Runnable runElapsedTime = new Runnable() {
        @Override
        public void run() {
            if (getMediaPlayer() == null) {
                //this happens when pressing next/previous while isPlaying.
                return;
            }
            long elapsedTime = getMediaPlayer().getCurrentPosition(); //milliseconds
            int elapsedSeconds = (int) (elapsedTime / 1000);
            String timeString = "0:"; //fix for songs longer than 1 minute!!!
            timeString += (elapsedSeconds < 10) ? ("0" + elapsedSeconds) : elapsedSeconds;
            tvElapsedTime.setText(timeString);
            tvTotalTime.setText("0:" + (getMediaPlayer().getDuration() / 1000));
            seekBar.setMax((int) getMediaPlayer().getDuration());
            seekBar.setProgress((int) elapsedTime);
            seekBar.invalidate();
            if (getMediaPlayer().isPlaying()) {
                mHandler.postDelayed(runElapsedTime, 100); //every 1/10th of a second.
            }
        }
    };
    //
    PlayerService.LocalBinder mBinder;
    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBinder = (PlayerService.LocalBinder) service;
            mBinder.setCallbacks(PlayerFragment.this);
            if (mTrack != null) {
                initMediaPlayer();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //oh dear..
        }
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        //http://stackoverflow.com/questions/15313598/once-for-all-how-to-correctly-save-instance-state-of-fragments-in-back-stack
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            //Restore the fragment's state here
            currentPosition = savedInstanceState.getInt(RESTORE_CURRENT_TRACK_POSITION);
            currentMyTrack = myTracks.get(currentPosition);
            prepareTrack(false);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //http://stackoverflow.com/questions/15313598/once-for-all-how-to-correctly-save-instance-state-of-fragments-in-back-stack
        //super.onSaveInstanceState(outState);
        outState.putInt(RESTORE_CURRENT_TRACK_POSITION, currentPosition);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unbindService(serviceConnection);
    }

    public MediaPlayer getMediaPlayer() {
        if (mBinder == null) {
            return null;
        }
        return mBinder.getMediaPlayer();
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        if (mBinder == null) {
            return;
        }
        mBinder.setMediaPlayer(mediaPlayer);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().startService(new Intent(getActivity(), PlayerService.class));
        getActivity().bindService(new Intent(getActivity(), PlayerService.class), serviceConnection, 0);
        //
        View v = inflater.inflate(R.layout.player_fragment, container, false);
        myTracks = getArguments().getParcelableArrayList(TRACK_MYTRACKS);
        if (currentPosition == -1) {
            //only set from intent (bundle) if not rotating screen
            currentPosition = getArguments().getInt(TRACK_POSITION);
        }
        currentMyTrack = myTracks.get(currentPosition);
        currentArtists = getArguments().getString(TRACK_ARTIST);
        initUI(v);
        prepareTrack(false);
        return v;
    }

    public boolean isMediaPlayerPrepared() {
        return (mBinder != null) ? mBinder.isMediaPrepared() : false;
    }

    public void initUI(View v) {
        tvAlbum = (TextView) v.findViewById(R.id.player_album);
        tvArtist = (TextView) v.findViewById(R.id.player_artist);
        tvTrackname = (TextView) v.findViewById(R.id.player_trackname);
        albumImage = (ImageView) v.findViewById(R.id.player_album_image);
        previousBtn = (ImageButton) v.findViewById(R.id.player_btn_previous);
        nextBtn = (ImageButton) v.findViewById(R.id.player_btn_next);
        playPauseBtn = (ImageButton) v.findViewById(R.id.player_btn_play_pause);
        tvElapsedTime = (TextView) v.findViewById(R.id.player_seekbar_time_elapsed);
        tvTotalTime = (TextView) v.findViewById(R.id.player_seekbar_time_total);
        seekBar = (SeekBar) v.findViewById(R.id.player_seekbar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (!fromUser) {
                    return;
                }
                if (getMediaPlayer() == null) {
                    return;
                }
                //long elapsedTime = getMediaPlayer().getCurrentPosition(); //milliseconds
                int ms = (int) Util.map(progress, 0, seekBar.getMax(), 0, getMediaPlayer().getDuration());
                int elapsedSeconds = (int) (ms / 1000);
                String timeString = "0:"; //fix for songs longer than 1 minute!!!
                timeString += (elapsedSeconds < 10) ? ("0" + elapsedSeconds) : elapsedSeconds;
                tvElapsedTime.setText(timeString);
                tvTotalTime.setText("0:" + (getMediaPlayer().getDuration() / 1000));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (getMediaPlayer() != null) {
                    if (getMediaPlayer().isPlaying()) {
                        getMediaPlayer().pause();
                        playPauseBtn.setImageDrawable(getActivity().getResources().getDrawable(android.R.drawable.ic_media_play));
                    }
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (getMediaPlayer() != null) {
                    if (isMediaPlayerPrepared()) {
                        int ms = (int) Util.map(seekBar.getProgress(), 0, seekBar.getMax(), 0, getMediaPlayer().getDuration());
                        getMediaPlayer().seekTo(ms);
                        getMediaPlayer().start();
                        playPauseBtn.setImageDrawable(getActivity().getResources().getDrawable(android.R.drawable.ic_media_pause));
                        //Start update loop
                        mHandler.post(runElapsedTime);
                    }
                }
            }
        });
        //
        playPauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Play / Pause
                if (getMediaPlayer() == null) {
                    return; //prevent NullPointerException
                }
                if (getMediaPlayer().isPlaying()) {
                    //getMediaPlayer().stop();
                    getMediaPlayer().pause();
                    ((ImageView) view).setImageDrawable(getActivity().getResources().getDrawable(android.R.drawable.ic_media_play));
                } else {
                    if (isMediaPlayerPrepared()) {
                        getMediaPlayer().start();
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
                prepareTrack(true);
            }
        });
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPosition++;
                currentMyTrack = myTracks.get(currentPosition);
                prepareTrack(true);
            }
        });

        if (getMediaPlayer() != null){
            if (getMediaPlayer().isPlaying()){
                playbackStarted();
            }
        }
    }

    public void prepareTrack(boolean nextSong) {
        //Disable previous and next button when reached end of list
        previousBtn.setEnabled((currentPosition != 0));
        nextBtn.setEnabled((currentPosition != myTracks.size() - 1));
        playPauseBtn.setImageDrawable(getActivity().getResources().getDrawable(android.R.drawable.ic_media_play));
        //Stop currently playing
        if (nextSong) {
            if (getMediaPlayer() != null) {
                if (getMediaPlayer().isPlaying()) {
                    getMediaPlayer().stop();
                }
                getMediaPlayer().release();
                setMediaPlayer(null);
            }
        }
        seekBar.setProgress(0);
        tvElapsedTime.setText("0:00");
        tvAlbum.setText(currentMyTrack.album);
        tvArtist.setText(currentArtists);
        tvTrackname.setText(currentMyTrack.title);
        //
        Picasso.with(getActivity())
                .load(currentMyTrack.imageBig)
                .placeholder(R.drawable.ic_artist_image_error)
                .error(R.drawable.ic_artist_image_error)
                .into(albumImage);
        //
        SpotifyApi api = new SpotifyApi();
        SpotifyService service = api.getService();
        service.getTrack(currentMyTrack.trackId, new Callback<Track>() {
            @Override
            public void success(Track track, Response response) {
                mTrack = track;
                if (mBinder != null) {
                    initMediaPlayer();
                }
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
        if (!isMediaPlayerPrepared()) {
            mediaUrl = mTrack.preview_url;
            mBinder.initMediaPlayer(mediaUrl);
        } else {
            if (getMediaPlayer().isPlaying()){
                mHandler.post(runElapsedTime);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (false) {
            if (getMediaPlayer() != null) {
                if (getMediaPlayer().isPlaying()) {
                    getMediaPlayer().stop();
                }
                getMediaPlayer().release();
                setMediaPlayer(null);
            }
        }
    }

    @Override
    public void playbackStarted() {
        playPauseBtn.performClick();
    }
}